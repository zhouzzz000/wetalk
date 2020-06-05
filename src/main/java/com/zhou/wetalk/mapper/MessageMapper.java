package com.zhou.wetalk.mapper;

import com.zhou.wetalk.pojo.Message;

import com.zhou.utils.MyMapper;

import java.util.List;

public interface MessageMapper extends MyMapper<Message> {

    int deleteByPrimaryKey(String id);

    public void batchUpdateMsgSigned(List<String> idsList);
}