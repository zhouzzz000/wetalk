package com.zhou.wetalk.netty.websocket.impl;

import com.zhou.wetalk.dto.DataContentDto;
import com.zhou.wetalk.netty.websocket.BaseAction;
import com.zhou.wetalk.service.MessageService;
import com.zhou.wetalk.utils.SpringUtil;
import com.zhou.wetalk.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 18:50
 * @ClassName SignedAction
 * @see
 */
public class SignedAction implements BaseAction {
    @Override
    public void handleAction(DataContentDto dataContent, ChannelGroup users, Channel currentChannel) {
        MessageService messageService = (MessageService) SpringUtil.getBean("messageServiceImpl");
        // 拓展字段在signed类型的消息中，代表需要签收的消息id，逗号间隔
        String[] ids = dataContent.getExtend().split(",");
        List<String> idsList = new ArrayList<>();
        for (String id : ids) {
            if (!StringUtils.isBlank(id))
            {
                idsList.add(id);
            }
        }
        if (!idsList.isEmpty()) {
            // 批量签收
            messageService.updateMsgSigned(idsList);
        }
    }
}