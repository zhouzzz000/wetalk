package com.zhou.wetalk.service;

import com.zhou.wetalk.vo.FriendRequestSendVo;

import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 15:58
 * @InterfaceName FriendsRequestService
 * @Description
 */
public interface FriendsRequestService extends BaseService {
    Integer addFriendRequest(String sendUserId, String acceptUserId);

    List<FriendRequestSendVo> queryFriendRequestList(String acceptUserId);

    boolean deleteFriendRequest(String sendUserId, String acceptUserId);
}
