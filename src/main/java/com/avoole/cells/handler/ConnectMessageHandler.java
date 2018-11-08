package com.avoole.cells.handler;

import com.avoole.cells.Client;
import com.avoole.cells.ClientManager;
import com.avoole.cells.data.Message;

public class ConnectMessageHandler implements MessageHandler {

    private ClientManager clientManager;

    public ConnectMessageHandler(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void handleMessage(Message message) {
        Client client = message.getClient();
        if (client == null) {
            return;
        }
        client.setConnected(true);
    }
}
