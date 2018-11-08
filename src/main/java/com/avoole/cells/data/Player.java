package com.avoole.cells.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.avoole.cells.Client;

public class Player extends Cell {

    public Vec2 velocity;

    public String nickname;

    @JSONField(serialize=false)
    public Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
