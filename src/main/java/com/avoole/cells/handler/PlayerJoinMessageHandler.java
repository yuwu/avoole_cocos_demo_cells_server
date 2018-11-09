package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.Player;
import com.avoole.cells.storage.WorldStore;
import com.avoole.cells.util.MessageUtil;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

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
        targetPlayer.setClient(message.getClient());
        store.addPlayer(targetPlayer);

        List<Cell> cells = store.getCells();
        List<Player> players = store.getPlayers();
        int width = store.getWidth();
        int hegiht = store.getHeight();

        Map<String, Object> payload = new HashMap<>();
        payload.put("width", width);
        payload.put("hegiht", hegiht);
        payload.put("cells", cells);
        payload.put("players", players);

        Message newMessage = new Message();
        newMessage.setPayload(payload);

        WebSocketFrame frame = MessageUtil.getMessagePlayerJoin(newMessage);
        for(Player player : players){
            player.getClient().getCtx().writeAndFlush(frame);
        }
    }
}
