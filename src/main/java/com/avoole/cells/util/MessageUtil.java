package com.avoole.cells.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.avoole.cells.data.Cell;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.MessageType;
import com.avoole.cells.data.Player;
import com.avoole.cells.handler.MessageHandler;
import com.avoole.common.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {

    private static Map<MessageType, Class> type2payload = new HashMap<>();

    static {
        type2payload.put(MessageType.CellJoin, Cell.class);
        type2payload.put(MessageType.CellDeath, Cell.class);

        type2payload.put(MessageType.PlayerUpdate, Player.class);
        type2payload.put(MessageType.PlayerJoin, Player.class);
        type2payload.put(MessageType.PlayerDeath, Player.class);
    }

    public static Message getMessage(WebSocketFrame webSocketFrame) {
        if (!(webSocketFrame instanceof TextWebSocketFrame)) return null;

        TextWebSocketFrame frame = (TextWebSocketFrame)webSocketFrame;

        String text = frame.text();
        JSONObject json = JSON.parseObject(text);

        int type = json.getInteger("type");
        Map headers = json.getObject("headers", HashMap.class);

        MessageType messageType = MessageType.valueOf(type);
        Class payloadClass = type2payload.getOrDefault(messageType, HashMap.class);
        Object payload = json.getObject("payload", payloadClass);

        Message message = new Message();
        message.setType(messageType);
        message.setHeaders(headers);
        message.setPayload(payload);

        return message;
    }

    public static WebSocketFrame getMessageWorld(Message message) {
        message.setType(MessageType.World);
        String json = JSON.toJSONString(message);
        TextWebSocketFrame frame = new TextWebSocketFrame(json);
        return frame;
    }

    public static WebSocketFrame getMessagePlayerJoin(Message message) {
        message.setType(MessageType.PlayerJoin);
        String json = JSON.toJSONString(message);
        TextWebSocketFrame frame = new TextWebSocketFrame(json);
        return frame;
    }

    public static WebSocketFrame getMessagePlayerUpdate(Message message) {
        message.setType(MessageType.PlayerUpdate);
        String json = JSON.toJSONString(message);
        TextWebSocketFrame frame = new TextWebSocketFrame(json);
        return frame;
    }

    public static WebSocketFrame getMessagePlayerDeath(Message message) {
        message.setType(MessageType.PlayerDeath);
        String json = JSON.toJSONString(message);
        TextWebSocketFrame frame = new TextWebSocketFrame(json);
        return frame;
    }
}
