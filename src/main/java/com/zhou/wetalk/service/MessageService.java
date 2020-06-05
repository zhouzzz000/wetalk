package com.zhou.wetalk.service;

import com.zhou.wetalk.dto.MessageDto;
import com.zhou.wetalk.pojo.Message;

import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 19:16
 * @InterfaceName MessageService
 * @Description
 */
public interface MessageService extends BaseService{
    String addOneMessage(MessageDto messageDto);

    void updateMsgSigned(List<String> idsList);

    public List<Message> getUnreadMessageList(String myId);
}
