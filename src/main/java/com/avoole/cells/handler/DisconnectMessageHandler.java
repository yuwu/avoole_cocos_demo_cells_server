package com.avoole.cells.handler;

import com.avoole.cells.Client;
import com.avoole.cells.ClientManager;
import com.avoole.cells.data.Message;

public class DisconnectMessageHandler implements MessageHandler {

    private ClientManager clientManager;

    public DisconnectMessageHandler(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void handleMessage(Message message) {
        Client client = message.getClient();
        if (client == null) {
            return;
        }
        client.setConnected(false);

        clientManager.remove(message.getClient().getCtx().channel());
        message.getClient().getCtx().disconnect();
    }
}
