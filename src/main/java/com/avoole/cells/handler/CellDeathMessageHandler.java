package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.Player;
import com.avoole.cells.storage.WorldStore;
import com.avoole.cells.util.MessageUtil;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

public class CellDeathMessageHandler implements MessageHandler {

    private WorldStore store;

    public CellDeathMessageHandler(WorldStore store) {
        this.store = store;
    }

    @Override
    public void handleMessage(Message message) {
        Client client = message.getClient();
        if (client == null || !client.isConnected()) {
            return;
        }

        Cell targetCell = (Cell)message.getPayload();
        if(!store.hasCell(targetCell)){
            return;
        }
        store.removeCell(targetCell);

        Message newMessage = new Message();
        newMessage.setPayload(targetCell);

        List<Player> players = store.getPlayers();
        for(Player player : players){
            WebSocketFrame frame = MessageUtil.getMessageCellDeath(newMessage);
            player.getClient().getCtx().writeAndFlush(frame);
        }
    }
}
