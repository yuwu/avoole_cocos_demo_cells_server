package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.Player;
import com.avoole.cells.storage.WorldStore;
import com.avoole.cells.util.MessageUtil;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

public class PlayerDeathMessageHandler implements MessageHandler {

    private WorldStore store;

    public PlayerDeathMessageHandler(WorldStore store) {
        this.store = store;
    }

    @Override
    public void handleMessage(Message message) {
        Client client = message.getClient();
        if (client == null || !client.isConnected()) {
            return;
        }

        Player targetPlayer = (Player)message.getPayload();
        if(!store.hasPlayer(targetPlayer)){
            return;
        }
        store.removePlayer(targetPlayer);

        Message newMessage = new Message();
        newMessage.setPayload(targetPlayer);

        List<Player> players = store.getPlayers();
        for(Player player : players){
            WebSocketFrame frame = MessageUtil.getMessagePlayerDeath(newMessage);
            player.getClient().getCtx().writeAndFlush(frame);
        }
        //ReferenceCountUtil.release(frame);
    }
}
