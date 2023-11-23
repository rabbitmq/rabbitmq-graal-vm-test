/*
 * Copyright (c) 2018-2023 Broadcom. All Rights Reserved. The term Broadcom refers to Broadcom Inc. and/or its subsidiaries.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * RPC server that replies to one request before exiting.
 */
public class SingleUseRpcServer extends RpcServer {

    static final String QUEUE = "graalvm.test.queue";

    public SingleUseRpcServer(Channel channel) throws IOException {
        super(channel, QUEUE);
    }

    public static void main(String[] args) throws Exception {
        ConnectionFactory cf = new ConnectionFactory();
        try (Connection c = cf.newConnection()) {
            Channel ch = c.createChannel();
            ch.queueDeclare(QUEUE, false, false, true, null);
            ch.confirmSelect();
            CountDownLatch latch = new CountDownLatch(1);
            ch.addConfirmListener((dTag, multiple) -> latch.countDown(), ((deliveryTag, multiple) -> {
            }));
            SingleUseRpcServer server = new SingleUseRpcServer(ch);
            new Thread(() -> {
                try {
                    server.mainloop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            latch.await();
        }
        System.exit(0);
    }

    @Override
    public byte[] handleCall(Delivery request, AMQP.BasicProperties replyProperties) {
        String input = new String(request.getBody());
        return ("*** " + input + " ***").getBytes();
    }

}
