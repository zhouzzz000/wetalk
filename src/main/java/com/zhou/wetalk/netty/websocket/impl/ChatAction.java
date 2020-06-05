package com.zhou.wetalk.netty.websocket.impl;

import com.zhou.utils.JsonUtils;
import com.zhou.wetalk.dto.DataContentDto;
import com.zhou.wetalk.dto.MessageDto;
import com.zhou.wetalk.idworker.Sid;
import com.zhou.wetalk.netty.websocket.BaseAction;
import com.zhou.wetalk.netty.websocket.UserChannelRel;
import com.zhou.wetalk.pojo.Message;
import com.zhou.wetalk.service.MessageService;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Date;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:49
 * @ClassName ChatAction
 * @see
 */
public class ChatAction implements BaseAction {
    @Override
    public void handleAction(DataContentDto dataContent, ChannelGroup users,Channel currentChannel) {
        MessageService messageService = (MessageService)SpringUtil.getBean("messageServiceImpl");
        String msgId = messageService.addOneMessage(dataContent.getMessage());
        String acceptUserId = dataContent.getMessage().getAcceptUserId();
        dataContent.getMessage().setId(msgId);
        // 发送消息
        Channel acceptChannel = UserChannelRel.get(acceptUserId);
        if (acceptChannel != null)
        {
           Channel findChannel = users.find(acceptChannel.id());
           if (findChannel != null)
           {
               // 用户在线
               findChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
           }else{
               // 用户离线推送
           }
        }else{
            // 用户从未登录过，此时可以通过第三方平台做消息推送
        }
    }
}