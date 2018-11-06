package com.avoole.cells;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    ClientManager clientManager;

    public MessageHandler(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        System.out.print("MessageHandler: class:" + msg.getClass().getName() + " msg:" + msg);
        if (!(msg instanceof TextWebSocketFrame)) return;
        TextWebSocketFrame frame = (TextWebSocketFrame)msg;
        logger.debug("MessageHandler-read:" + frame.text());

        String content = "我操你二大爷" + frame.text();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(content));
    }
}
