package com.avoole.cells.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avoole.cells.data.Message;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.HashMap;
import java.util.Map;

public class MessageUtil {

    public static Message getMessage(WebSocketFrame webSocketFrame) {
        if (!(webSocketFrame instanceof TextWebSocketFrame)) return null;

        TextWebSocketFrame frame = (TextWebSocketFrame)webSocketFrame;

        String text = frame.text();

        JSONObject json = JSON.parseObject(text);

        String id = json.getString("id");
        int type = json.getInteger("type");
        Map<String, String> headers = json.getObject("headers", HashMap.class);
        Object payload = json.getObject("payload", HashMap.class);

        Message message = new Message();
        message.setId(id);
        message.setType(type);
        message.setHeaders(headers);
        message.setPayload(payload);

        return message;
    }
}
