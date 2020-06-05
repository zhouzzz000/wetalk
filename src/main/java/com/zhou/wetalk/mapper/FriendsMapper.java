package com.zhou.wetalk.mapper;

import com.zhou.wetalk.pojo.Friends;

import com.zhou.utils.MyMapper;

import java.util.List;

public interface FriendsMapper extends MyMapper<Friends> {
    int deleteByPrimaryKey(String id);
    List getFriendsListById(String userId);
}