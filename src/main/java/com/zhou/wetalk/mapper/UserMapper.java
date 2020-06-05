package com.zhou.wetalk.mapper;

import com.zhou.wetalk.pojo.User;

import com.zhou.utils.MyMapper;
import com.zhou.wetalk.vo.FriendRequestSendVo;

import java.util.List;


public interface UserMapper extends MyMapper<User> {

    int deleteByPrimaryKey(String id);


}