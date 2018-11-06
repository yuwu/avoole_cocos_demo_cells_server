package com.avoole.mm;

import io.netty.channel.Channel;

public class DisconnectChannelEvent {
    private Channel channel;

    public DisconnectChannelEvent(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
