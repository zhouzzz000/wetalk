package com.zhou.wetalk.netty.websocket.impl;

import com.zhou.wetalk.dto.DataContentDto;
import com.zhou.wetalk.netty.websocket.BaseAction;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:50
 * @ClassName KeepAliveAction
 * @see
 */
public class KeepAliveAction implements BaseAction {
    @Override
    public void handleAction(DataContentDto dataContent, ChannelGroup users, Channel currentChannel) {

    }
}