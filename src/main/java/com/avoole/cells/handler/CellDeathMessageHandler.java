package com.avoole.cells.handler;


import com.avoole.cells.Client;
import com.avoole.cells.data.Message;
import com.avoole.cells.storage.WorldStore;

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
    }
}
