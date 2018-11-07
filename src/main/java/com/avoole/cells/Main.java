package com.avoole.cells;

import com.avoole.cells.data.Message;
import com.avoole.cells.handler.ConnectMessageHandler;
import com.avoole.cells.handler.DisconnectMessageHandler;
import com.avoole.cells.handler.PlayerJoinMessageHandler;
import com.avoole.cells.storage.WorldStore;
import com.avoole.cells.storage.impl.InMemoryWorldStore;
import com.avoole.mm.common.configuration.MQTTBridgeConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    private ServerBootstrap serverBootstrap;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ClientManager clientManager;
    private MessageDispatcher messageDispatcher;
    private WorldStore worldStore;
    private WSConnectionHandler connectionHandler;

    public Main() {
        init();
    }

    private void  init() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(32);
        serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .localAddress(MQTTBridgeConfiguration.host(), MQTTBridgeConfiguration.port())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, MQTTBridgeConfiguration.socketBacklog())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("http-codec",new HttpServerCodec());
                        pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
                        pipeline.addLast("ws-codec", new WebSocketServerProtocolHandler("/ws"));
                        pipeline.addLast("message-handler", messageDispatcher);
                        pipeline.addLast("connection-manager", connectionHandler);
                    }
                });

        worldStore = new InMemoryWorldStore();
        clientManager = new ClientManager();
        messageDispatcher = new MessageDispatcher(clientManager);
        connectionHandler = new WSConnectionHandler(clientManager, worldStore);
        registerMessageHandlers();
    }

    private void registerMessageHandlers() {
        messageDispatcher.registerHandler(Message.Type.Connect, new ConnectMessageHandler(clientManager));
        messageDispatcher.registerHandler(Message.Type.Disconnect, new DisconnectMessageHandler(clientManager));

        messageDispatcher.registerHandler(Message.Type.PlayerJoin, new PlayerJoinMessageHandler(worldStore));
        messageDispatcher.registerHandler(Message.Type.PlayerUpdate, new PlayerJoinMessageHandler(worldStore));
    }

    public void start() {
        try {
            ChannelFuture channelFuture = null;
            channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("fail to start the MQTTServer." + e);
        } finally {
            logger.info("shutdown the MQTTServer");
            shutdown();
        }
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String [] args) {
        Main server = new Main();
        server.start();
        logger.info("start the webserver");
    }
}
