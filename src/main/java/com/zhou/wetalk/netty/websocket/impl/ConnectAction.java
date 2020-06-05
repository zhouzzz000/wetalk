package com.zhou.wetalk.netty.websocket.impl;

import com.zhou.wetalk.dto.DataContentDto;
import com.zhou.wetalk.netty.websocket.BaseAction;
import com.zhou.wetalk.netty.websocket.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:49
 * @ClassName ConnectAction
 * @see
 */
public class ConnectAction implements BaseAction {
    @Override
    public void handleAction(DataContentDto dataContent, ChannelGroup users, Channel currentChannel) {
        String sendUserId = dataContent.getMessage().getSendUserId();
        UserChannelRel.put(sendUserId, currentChannel);
//        for (Channel c : users)
//        {
//            System.out.println(c.id().asLongText());
//        }
//        UserChannelRel.printRel();
    }
}