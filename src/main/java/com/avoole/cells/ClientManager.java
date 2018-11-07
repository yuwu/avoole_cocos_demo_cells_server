package com.avoole.cells;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

    public ClientManager() {
    }

    private ConcurrentHashMap<Channel, Client> channel2Client = new ConcurrentHashMap<>();

    public Client get(Channel channel) {
        return channel2Client.get(channel);
    }

    public void put(Channel channel, Client client) {
        channel2Client.put(channel, client);
    }

    public Client remove(Channel channel) {
        return channel2Client.remove(channel);
    }
}
