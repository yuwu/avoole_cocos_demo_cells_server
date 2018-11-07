package com.avoole.cells.handler;

import com.avoole.cells.ClientManager;
import com.avoole.cells.data.Message;

public class DisconnectMessageHandler implements MessageHandler {


    private ClientManager clientManager;

    public DisconnectMessageHandler(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void handleMessage(Message message) {

    }
}
