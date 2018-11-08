package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.Player;
import com.avoole.cells.storage.WorldStore;
import com.avoole.cells.util.MessageUtil;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

public class PlayerUpdateMessageHandler implements MessageHandler {

    private WorldStore store;

    public PlayerUpdateMessageHandler(WorldStore store) {
        this.store = store;
    }

    @Override
    public void handleMessage(Message message) {
        Client client = message.getClient();
        if (client == null || !client.isConnected()) {
            return;
        }

        Message newMessage = new Message();
        List<Player> players = store.getPlayers();
        for(Player player : players){
            newMessage.setPayload(player);
            WebSocketFrame frame = MessageUtil.getMessagePlayerUpdate(newMessage);
            player.getClient().getCtx().writeAndFlush(frame);
        }
    }
}
