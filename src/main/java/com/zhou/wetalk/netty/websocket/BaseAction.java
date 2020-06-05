package com.zhou.wetalk.netty.websocket;

import com.zhou.wetalk.dto.DataContentDto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:43
 * @InterfaceName BaseAction
 * @Description
 */
public interface BaseAction {
    void handleAction(DataContentDto dataContent, ChannelGroup users, Channel currentChannel);
}
