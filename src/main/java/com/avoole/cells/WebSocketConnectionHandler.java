package com.avoole.cells;

import com.avoole.mm.DisconnectChannelEvent;
import com.avoole.mm.connection.client.Client;
import com.avoole.mm.protocol.mqtt.data.MqttClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketConnectionHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConnectionHandler.class);

    private ClientManager clientManager;

    public WebSocketConnectionHandler(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if (evt instanceof DisconnectChannelEvent) {
            DisconnectChannelEvent disconnectChannelEvent = (DisconnectChannelEvent) evt;
            doDisconnect(disconnectChannelEvent.getChannel());
        }else if (evt instanceof IdleStateEvent) {
           IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
           if (IdleState.ALL_IDLE.equals(idleStateEvent.state())) {
               doDisconnect(ctx.channel());
           }
       }
        ctx.fireUserEventTriggered(evt);
    }

    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Client client = clientManager.get(ctx.channel());
        String clientId = client != null ? client.getId() : "null";
        doDisconnect(ctx.channel());
        log.debug("clientId:{} netty exception caught from {}", clientId, ctx.channel(), cause);
    }

    /**
     * disconnect the channel, save the Subscription of the client which the channel belongs to if CleanSession
     * if set to <b>false</b>, otherwise discard them
     *
     * @param channel
     */
    private void doDisconnect(Channel channel) {
        if (channel == null) {
            return;
        }
        MqttClient client = (MqttClient) clientManager.get(channel);
        if (client != null) {
            clientManager.remove(channel);
        }
        channel.close();
    }
}
