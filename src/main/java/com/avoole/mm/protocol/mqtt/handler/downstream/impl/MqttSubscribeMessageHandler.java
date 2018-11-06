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

import java.util.ArrayList;
import java.util.List;

import com.avoole.common.codec.mqtt.MqttSubAckMessage;
import com.avoole.common.codec.mqtt.MqttSubAckPayload;
import com.avoole.common.codec.mqtt.MqttSubscribeMessage;
import com.avoole.common.codec.mqtt.MqttTopicSubscription;
import com.avoole.mm.common.data.Message;
import com.avoole.mm.connection.client.Client;
import com.avoole.mm.protocol.mqtt.data.MqttClient;
import com.avoole.mm.protocol.mqtt.data.Subscription;
import com.avoole.mm.protocol.mqtt.handler.MessageHandler;
import com.avoole.mm.storage.subscription.SubscriptionStore;
import com.avoole.mm.common.util.MessageUtil;

public class MqttSubscribeMessageHandler implements MessageHandler {

    private SubscriptionStore subscriptionStore;

    public MqttSubscribeMessageHandler(SubscriptionStore subscriptionStore) {
        this.subscriptionStore = subscriptionStore;
    }

    /**
     * handle the SUBSCRIBE message from the client
     * <ol>
     * <li>validate the topic filters in each subscription</li>
     * <li>set actual qos of each filter</li>
     * <li>get the topics matching given filters</li>
     * <li>check the client authorization of each topic</li>
     * <li>generate SUBACK message which includes the subscription result for each TopicFilter</li>
     * <li>send SUBACK message to the client</li>
     * </ol>
     *
     * @param message the message wrapping MqttSubscriptionMessage
     * @return
     */
    @Override public void handleMessage(Message message) {
        Client client = message.getClient();
        MqttSubscribeMessage subscribeMessage = (MqttSubscribeMessage) message.getPayload();
        List<MqttTopicSubscription> topicSubscriptions = subscribeMessage.payload().topicSubscriptions();
        List<Integer> grantQoss = new ArrayList<>();
        topicSubscriptions.forEach(s -> {
            String topic = s.topicName();
            int actualQos = MessageUtil.actualQos(s.qualityOfService().value());
            grantQoss.add(actualQos);
            subscriptionStore.append(
                topic,
                Subscription.Builder.newBuilder()
                    .client((MqttClient) client)
                    .qos(actualQos)
                    .build()
            );
        });
        MqttSubAckMessage subackMessage = MessageUtil.getMqttSubackMessage(subscribeMessage, new MqttSubAckPayload(grantQoss));
        client.getCtx().writeAndFlush(subackMessage);
    }
}
