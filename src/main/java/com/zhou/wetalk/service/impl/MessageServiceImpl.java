package com.zhou.wetalk.service.impl;

import com.zhou.wetalk.dto.MessageDto;
import com.zhou.wetalk.enums.MsgSign;
import com.zhou.wetalk.idworker.Sid;
import com.zhou.wetalk.mapper.MessageMapper;
import com.zhou.wetalk.pojo.Message;
import com.zhou.wetalk.service.MessageService;
import com.zhou.wetalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 19:18
 * @ClassName MessageServiceImpl
 * @see
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserService userService;
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String addOneMessage(MessageDto messageDto) {
        String content = messageDto.getContent();
        String acceptUserId = messageDto.getAcceptUserId();
        String sendUserId = messageDto.getSendUserId();
        Integer type = messageDto.getType();
        String sendUserAvator = messageDto.getSendUserAvator();
        String acceptUserAvator = messageDto.getAcceptUserAvator();
        if (acceptUserAvator == null)
        {
            acceptUserAvator = userService.queryUserById(acceptUserId).getFaceImage();
            messageDto.setAcceptUserAvator(acceptUserAvator);
        }
        if (sendUserAvator == null)
        {
            sendUserAvator = userService.queryUserById(sendUserId).getFaceImage();
            messageDto.setSendUserAvator(sendUserAvator);
        }
        Message message = Message.builder().sendUserId(sendUserId)
                .acceptUserId(acceptUserId)
                .content(content)
                .id(Sid.nextShort())
                .sendUserAvator(sendUserAvator)
                .acceptUserAvator(acceptUserAvator)
                .state(MsgSign.UNSIGN.type)
                .createTime(new Date())
                .type(type)
                .build();
         messageDto.setDate(message.getCreateTime());
         messageMapper.insert(message);
         return message.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> idsList) {
        messageMapper.batchUpdateMsgSigned(idsList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Message> getUnreadMessageList(String myId) {
        Example mexp = new Example(Message.class);
        Example.Criteria mcra = mexp.createCriteria();
        mcra.andEqualTo("acceptUserId",myId);
        mcra.andEqualTo("state",0);
        return messageMapper.selectByExample(mexp);
    }

}