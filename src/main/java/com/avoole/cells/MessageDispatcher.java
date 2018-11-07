package com.avoole.cells;

import com.avoole.cells.data.Message;
import com.avoole.cells.handler.MessageHandler;
import com.avoole.cells.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.HashMap;
import java.util.Map;

public class MessageDispatcher extends SimpleChannelInboundHandler<WebSocketFrame> {
    private Map<Message.Type, MessageHandler> type2handler = new HashMap<>();

    ClientManager clientManager;

    public MessageDispatcher(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void registerHandler(Message.Type type, MessageHandler handler) {
        type2handler.put(type, handler);
    }

    private void dispatch(Message message) {
        Message.Type type = message.getType();
        if (!type2handler.containsKey(type)) {
            return;
        }
        type2handler.get(type).handleMessage(message);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
        if (!(msg instanceof WebSocketFrame)) return;

        Client client = clientManager.get(ctx.channel());
        if (client == null) {
            client = new Client();
            client.setCtx(ctx);
            clientManager.put(ctx.channel(), client);
        }

        TextWebSocketFrame frame = (TextWebSocketFrame)msg;
        Message message = MessageUtil.getMessage(frame);
        message.setClient(client);
        dispatch(message);
    }
}
