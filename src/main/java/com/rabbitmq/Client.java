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
import com.rabbitmq.client.impl.ClientVersion;

/**
 * RPC client that sends a single request.
 */
public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Using RabbitMQ AMQP Client " + ClientVersion.VERSION);
        ConnectionFactory cf = new ConnectionFactory();
        try (Connection c = cf.newConnection()) {
            Channel ch = c.createChannel();
            RpcClient client = new RpcClient(
                    new RpcClientParams().channel(ch).exchange("")
                            .routingKey(SingleUseRpcServer.QUEUE)
                            .timeout(5000));
            String request = "hello";
            System.out.println("Sending: " + request);
            byte[] response = client.primitiveCall(request.getBytes());
            System.out.println("Received: " + new String(response));
        }
    }

}
