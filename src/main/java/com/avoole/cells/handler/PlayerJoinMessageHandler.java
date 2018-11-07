package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Message;
import com.avoole.cells.storage.WorldStore;

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


    }
}
