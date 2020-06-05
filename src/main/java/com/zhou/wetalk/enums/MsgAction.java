package com.zhou.wetalk.enums;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:33
 * @ClassName MsgAction
 * @see
 */
public enum MsgAction {
    CONNECT(1,"第一次（或重连）初始化连接"),
    CHAT(2,"聊天消息"),
    SIGNED(3,"消息签收"),
    REFRESH_FRIEND(5,"刷新好友列表"),
    KEEPALIVE(4,"客户端保持心跳");

    public final Integer type;
    public final String content;

    MsgAction(Integer type, String content)
    {
        this.type = type;
        this.content = content;
    }
}