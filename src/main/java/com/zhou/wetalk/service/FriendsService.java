package com.zhou.wetalk.service;

import com.zhou.wetalk.enums.SearchFriendsStatusEnum;
import com.zhou.wetalk.pojo.User;

import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 13:18
 * @InterfaceName FriendsService
 * @Description
 */
public interface FriendsService extends BaseService{

    Integer preconditionOfSearchFriends(String muUserId, String friendsUsername);

    boolean judgeRelativeByUserId(String myId, String otherId);

    Integer addNewFriendRelative(String sendUserId, String acceptUserId);

    List getFriendsListById(String userId);
}
