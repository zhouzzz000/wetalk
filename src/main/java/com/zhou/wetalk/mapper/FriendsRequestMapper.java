package com.zhou.wetalk.mapper;

import com.zhou.wetalk.pojo.FriendsRequest;

import com.zhou.utils.MyMapper;
import com.zhou.wetalk.vo.FriendRequestSendVo;

import java.util.List;

public interface FriendsRequestMapper extends MyMapper<FriendsRequest> {

    int deleteByPrimaryKey(String id);

    List<FriendRequestSendVo> queryFriendRequestList(String acceptUserId);
}