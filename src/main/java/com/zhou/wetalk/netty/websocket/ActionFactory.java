package com.zhou.wetalk.netty.websocket;

import com.zhou.wetalk.dto.DataContentDto;
import com.zhou.wetalk.enums.MsgAction;
import com.zhou.wetalk.netty.websocket.impl.ChatAction;
import com.zhou.wetalk.netty.websocket.impl.ConnectAction;
import com.zhou.wetalk.netty.websocket.impl.KeepAliveAction;
import com.zhou.wetalk.netty.websocket.impl.SignedAction;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:44
 * @ClassName ActionFactory
 * @see
 */
public class ActionFactory {
    public static void handleActionFactory(DataContentDto dataContent, ChannelGroup users, Channel currentChannel)
    {
        if (dataContent == null)
        {
            return;
        }
        Integer action = dataContent.getAction();
        BaseAction actionHandle = null;
        if (action.equals(MsgAction.CONNECT.type))
        {
            actionHandle = new ConnectAction();
        }else if(action.equals(MsgAction.CHAT.type))
        {
            actionHandle = new ChatAction();
        }else if(action.equals(MsgAction.SIGNED.type))
        {
            actionHandle = new SignedAction();
        }else if(action.equals(MsgAction.KEEPALIVE.type))
        {
            actionHandle = new KeepAliveAction();
        }
        if (actionHandle != null) {
            actionHandle.handleAction(dataContent,users,currentChannel);
        }
    }
}