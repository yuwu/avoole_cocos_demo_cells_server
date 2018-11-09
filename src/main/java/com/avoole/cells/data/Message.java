package com.avoole.cells.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.avoole.cells.Client;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Message {
    private static final AtomicInteger atomic = new AtomicInteger(0);

    private String id;
    private Map<String, String> headers;
    private MessageType type;
    private Object payload;

    @JSONField(serialize=false)
    private Client client;

    public Message() {
        id = String.valueOf(atomic.getAndIncrement());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @JSONField(name = "type")
    public int getMessageType() {
        return type.value();
    }

    @JSONField(name = "type")
    public void setMessageType(int type) {
        this.type = MessageType.valueOf(type);
    }

    @JSONField(serialize = false)
    public MessageType getType() {
        return type;
    }

    @JSONField(serialize = false)
    public void setType(MessageType type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
