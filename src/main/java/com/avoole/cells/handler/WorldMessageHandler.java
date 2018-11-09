package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.MessageType;
import com.avoole.cells.data.Player;
import com.avoole.cells.storage.WorldStore;
import com.avoole.cells.util.MessageUtil;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMessageHandler implements MessageHandler {

    private WorldStore store;

    public WorldMessageHandler(WorldStore store) {
        this.store = store;
    }

    @Override
    public void handleMessage(Message message) {
        Client client = message.getClient();
        if (client == null || !client.isConnected()) {
            return;
        }

        List<Cell> cells =  store.getCells();
        List<Player> players = store.getPlayers();
        int width = store.getWidth();
        int height = store.getHeight();

        Map<String, Object> payload = new HashMap<>();
        payload.put("width", width);
        payload.put("height", height);
        payload.put("cells", cells);
        payload.put("players", players);

        Message newMessage = new Message();
        newMessage.setType(MessageType.World);
        newMessage.setPayload(payload);

        WebSocketFrame frame = MessageUtil.getMessageWorld(newMessage);
        message.getClient().getCtx().writeAndFlush(frame);
    }
}
