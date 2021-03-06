/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.avoole.mm.protocol.mqtt.handler.downstream.impl;

import com.avoole.common.codec.mqtt.MqttConnAckMessage;
import com.avoole.common.codec.mqtt.MqttConnectMessage;
import com.avoole.common.codec.mqtt.MqttConnectReturnCode;
import io.netty.channel.ChannelHandlerContext;
import com.avoole.mm.common.configuration.ChannelConfiguration;
import com.avoole.mm.common.data.Message;
import com.avoole.mm.connection.client.ClientManager;
import com.avoole.mm.protocol.mqtt.data.MqttClient;
import com.avoole.mm.protocol.mqtt.event.DisconnectChannelEvent;
import com.avoole.mm.protocol.mqtt.handler.MessageHandler;
import com.avoole.mm.common.util.MessageUtil;

public class MqttConnectMessageHandler implements MessageHandler {

    private static final int MIN_AVAILABLE_VERSION = 3;
    private static final int MAX_AVAILABLE_VERSION = 4;

    private ClientManager clientManager;

    public MqttConnectMessageHandler(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override public void handleMessage(Message message) {
        MqttClient client = (MqttClient) message.getClient();
        MqttConnectMessage connectMessage = (MqttConnectMessage) message.getPayload();

        MqttConnectReturnCode returnCode;
        MqttConnAckMessage ackMessage;

        ChannelHandlerContext ctx = client.getCtx();

        if (!isClientIdValid(connectMessage.payload().clientIdentifier())) {
            returnCode = MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED;
        } else if (!checkUsername(connectMessage.payload().userName()) || !checkPassword(connectMessage.payload().passwordInBytes())) {
            returnCode = MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD;
        } else if (!isAuthorized(connectMessage)) {
            returnCode = MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED;
        } else if (client.isConnected()) {
            // protocol violation and Disconnect
            ctx.fireUserEventTriggered(new DisconnectChannelEvent(ctx.channel()));
            return;
        } else if (!isServiceAviable(connectMessage)) {
            returnCode = MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE;
        } else {
            client.setId(connectMessage.payload().clientIdentifier());
            client.setConnected(true);
            client.setCleanSession(connectMessage.variableHeader().isCleanSession());
            ctx.channel().attr(ChannelConfiguration.CHANNEL_IDLE_TIME_ATTRIBUTE_KEY).set(connectMessage.variableHeader().keepAliveTimeSeconds());
            returnCode = MqttConnectReturnCode.CONNECTION_ACCEPTED;
        }

        ackMessage = MessageUtil.getMqttConnackMessage(connectMessage, returnCode);
        ctx.writeAndFlush(ackMessage);
    }

    private boolean isServiceAviable(MqttConnectMessage connectMessage) {
        int version = connectMessage.variableHeader().version();
        return version >= MIN_AVAILABLE_VERSION && version <= MAX_AVAILABLE_VERSION;
    }

    private boolean checkPassword(byte[] bytes) {
        return true;
    }

    private boolean checkUsername(String s) {
        return true;
    }

    private boolean isAuthorized(MqttConnectMessage message) {
        return true;
    }

    private boolean isClientIdValid(String s) {
        return true;
    }
}
