package com.avoole.cells.handler;


import com.avoole.cells.data.Message;

public interface MessageHandler {

    /**
     * Handle message from client
     *
     * @param message
     */
    void handleMessage(Message message);
}
