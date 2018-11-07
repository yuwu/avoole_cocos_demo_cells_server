package com.avoole.cells.data;

import com.avoole.cells.Client;

import java.util.Map;

public class Message {

    public enum Type {
        /**
         * 请求连接
         */
        Connect,

        /**
         * 断开连接
         */
        Disconnect,

        /**
         * 心跳请求
         */
        Ping,

        /**
         * 心跳回复
         */
        Pong,

        /**
         * world基本信息， 大小
         */
        GetWorld,

        /**
         * 获取所有cell
         */
        GetCells,

        /**
         * 获取所有player
         */
        GetPlayers,

        /**
         * 传入昵称新建一个Player
         */
        PlayerJoin,

        /**
         * 更新Player信息. 比如颜色，坐标等
         */
        PlayerUpdate,

        /**
         * Player死亡
         */
        PlayerDeath,

        /**
         * 生成新的cell集合
         */
        CellsJoin,

        /**
         * cell死亡
         */
        CellDeath,
    }

    private String id;
    private Map<String, String> headers;
    private Type type;
    private Object payload;
    private Client client;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
