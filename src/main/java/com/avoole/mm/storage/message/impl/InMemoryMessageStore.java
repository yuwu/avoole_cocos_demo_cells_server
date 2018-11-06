package com.avoole.mm.storage.message.impl;

import com.avoole.mm.common.data.Message;
import com.avoole.mm.protocol.mqtt.data.MqttClient;
import com.avoole.mm.storage.message.MessageStore;

import java.util.List;

public class InMemoryMessageStore implements MessageStore {

    public InMemoryMessageStore() {
    }

    @Override
    public Message get(String id) {
        return null;
    }

    @Override
    public String put(Message message) {
        return null;
    }

    @Override
    public void prepare(String messageId, List<String> clientIds, int qos) {

    }

    @Override
    public void ack(Message message, MqttClient client) {

    }

    @Override
    public void ack(Message message, List<MqttClient> clients) {

    }

    @Override
    public void expire(String id) {

    }

    @Override
    public void start() {

    }

    @Override
    public List<Message> getOfflineMessages(MqttClient client) {
        return null;
    }

    @Override
    public void shutdown() {

    }
}
