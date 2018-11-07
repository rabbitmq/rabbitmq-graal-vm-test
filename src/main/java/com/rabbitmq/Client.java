/*
 * Copyright (c) 2018 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;

/**
 * RPC client that sends a single request.
 */
public class Client {

    public static void main(String[] args) throws Exception {
        ConnectionFactory cf = new ConnectionFactory();
        try (Connection c = cf.newConnection()) {
            Channel ch = c.createChannel();
            RpcClient client = new RpcClient(ch, "", SingleUseRpcServer.QUEUE, 5000);
            String request = "hello";
            System.out.println("Sending: " + request);
            byte[] response = client.primitiveCall(request.getBytes());
            System.out.println("Received: " + new String(response));
        }
    }

}
