package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.Player;
import com.avoole.cells.storage.WorldStore;
import com.avoole.cells.util.MessageUtil;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerJoinMessageHandler implements MessageHandler {

    private WorldStore store;

    public PlayerJoinMessageHandler(WorldStore store) {
        this.store = store;
    }

    @Override
    public void handleMessage(Message message) {
        Client client = message.getClient();
        if (client == null || !client.isConnected()) {
            return;
        }

        Player targetPlayer = (Player)message.getPayload();
        targetPlayer.setId(client.getId());
        targetPlayer.setClient(client);
        store.addPlayer(targetPlayer);

        List<Player> players = store.getPlayers();

        Message newMessage = new Message();
        newMessage.setPayload(targetPlayer);

        for(Player player : players){
            WebSocketFrame frame = MessageUtil.getMessagePlayerJoin(newMessage);
            player.getClient().getCtx().writeAndFlush(frame);
        }
        //ReferenceCountUtil.release(frame);

    }
}
