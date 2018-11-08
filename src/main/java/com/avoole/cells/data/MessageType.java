package com.avoole.cells.data;

public enum MessageType {

    /**
     * 请求连接
     */
    Connect(1),

    /**
     * 连接回复
     */
    Connack(2),

    /**
     * 断开连接
     */
    Disconnect(3),

    /**
     * 心跳请求
     */
    Pingreq(4),

    /**
     * 心跳回复
     */
    Pingresp(5),

    /**
     * World 基本信息. width, height, players, cells
     */
    World(6),

    /**
     * 传入昵称新建一个Player
     */
    PlayerJoin(7),

    /**
     * 更新Player信息. 比如颜色，坐标等
     */
    PlayerUpdate(8),

    /**
     * Player死亡
     */
    PlayerDeath(9),

    /**
     * 生成新的cell集合
     */
    CellJoin(10),

    /**
     * cell死亡
     */
    CellDeath(11);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static MessageType valueOf(int type) {
        for (MessageType t : values()) {
            if (t.value == type) {
                return t;
            }
        }
        throw new IllegalArgumentException("unknown message type: " + type);
    }
}
