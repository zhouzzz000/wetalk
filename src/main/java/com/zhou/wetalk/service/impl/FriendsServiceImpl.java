package com.zhou.wetalk.service.impl;

import com.zhou.wetalk.enums.SearchFriendsStatusEnum;
import com.zhou.wetalk.idworker.Sid;
import com.zhou.wetalk.mapper.FriendsMapper;
import com.zhou.wetalk.pojo.Friends;
import com.zhou.wetalk.pojo.User;
import com.zhou.wetalk.service.FriendsService;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 13:19
 * @ClassName FriendsServiceImpl
 * @see
 */
@Service
public class FriendsServiceImpl implements FriendsService {
    @Autowired
    private FriendsMapper friendsMapper;

    @Autowired
    private UserService userService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditionOfSearchFriends(String myUserId, String friendsUsername) {
        User otherUser = userService.queryUserByName(friendsUsername);
        if (otherUser == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        if (otherUser.getId().equals(myUserId))
        {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        if (judgeRelativeByUserId(myUserId, otherUser.getId()))
        {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }

        return SearchFriendsStatusEnum.SUCCESS.status;
    }


    @Override
    public boolean judgeRelativeByUserId(String myId, String otherId) {
        if (myId.equals(otherId))
        {
            return true;
        }
        Example fe = new Example(Friends.class);
        Example.Criteria fc = fe.createCriteria();
        fc.andEqualTo("userId",myId);
        fc.andEqualTo("friendUserId",otherId);
        Friends friends = friendsMapper.selectOneByExample(fe);
        return friends != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer addNewFriendRelative(String sendUserId, String acceptUserId) {
        if (judgeRelativeByUserId(acceptUserId, sendUserId))
        {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }
        // 添加两条记录，双方都是对方的好友
        Friends friends = Friends.builder().id(Sid.nextShort()).userId(acceptUserId).friendUserId(sendUserId).build();
        Friends friends1 = Friends.builder().id(Sid.nextShort()).userId(sendUserId).friendUserId(acceptUserId).build();
        friendsMapper.insertSelective(friends);
        friendsMapper.insertSelective(friends1);
        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List getFriendsListById(String userId) {
        List list = friendsMapper.getFriendsListById(userId);
        if (list.size() < 1)
        {
            return null;
        }else{
            return list;
        }
    }
}