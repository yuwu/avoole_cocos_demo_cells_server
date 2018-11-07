package com.avoole.cells.data;

import com.avoole.cells.Client;

public class Player extends Cell {

    public Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vec2 velocity;

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }
}
