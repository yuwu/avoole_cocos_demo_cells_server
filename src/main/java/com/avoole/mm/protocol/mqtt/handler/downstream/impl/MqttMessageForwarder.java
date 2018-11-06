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

import com.avoole.codec.mqtt.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import com.avoole.mm.common.data.Message;
import com.avoole.mm.protocol.mqtt.data.Subscription;
import com.avoole.mm.protocol.mqtt.handler.MessageHandler;
import com.avoole.mm.storage.subscription.SubscriptionStore;
import com.avoole.mm.common.util.MessageUtil;

import java.util.List;

public class MqttMessageForwarder implements MessageHandler {

    private SubscriptionStore subscriptionStore;

    public MqttMessageForwarder(SubscriptionStore subscriptionStore) {
        this.subscriptionStore = subscriptionStore;
    }

    /**
     * handle PUBLISH message from client
     *
     * @param message
     * @return whether the message is handled successfully
     */
    @Override public void handleMessage(Message message) {
        MqttPublishMessage publishMessage = (MqttPublishMessage) message.getPayload();
        String topic = publishMessage.variableHeader().topicName();
        if (!subscriptionStore.hasTopic(topic)) {
            subscriptionStore.addTopic(topic);
        }
        final List<Subscription> subscriptions = subscriptionStore.get(publishMessage.variableHeader().topicName());
        for (Subscription subscription : subscriptions) {
            ByteBuf buf = Unpooled.buffer();
            byte [] bytes = new byte[publishMessage.payload().readableBytes()];
            publishMessage.payload().getBytes(0, bytes);
            buf.writeBytes(bytes);
            MqttPublishMessage msg = new MqttPublishMessage(
                new MqttFixedHeader(
                    MqttMessageType.PUBLISH,
                    publishMessage.fixedHeader().isDup(),
                    MqttQoS.valueOf(MessageUtil.actualQos(publishMessage.fixedHeader().qosLevel().value())),
                    publishMessage.fixedHeader().isRetain(),
                    publishMessage.fixedHeader().remainingLength()
                ),
                new MqttPublishVariableHeader(
                    publishMessage.variableHeader().topicName(),
                    publishMessage.variableHeader().packetId()
                ),
                buf
            );
            subscription.getClient().getCtx().writeAndFlush(msg);
        }
    }
}
