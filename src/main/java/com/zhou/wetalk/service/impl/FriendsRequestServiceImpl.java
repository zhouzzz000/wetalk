package com.zhou.wetalk.service.impl;

import com.zhou.wetalk.idworker.Sid;
import com.zhou.wetalk.mapper.FriendsRequestMapper;
import com.zhou.wetalk.mapper.UserMapper;
import com.zhou.wetalk.pojo.FriendsRequest;

import com.zhou.wetalk.service.FriendsRequestService;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.vo.FriendRequestSendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 15:59
 * @ClassName FriendsRequestImpl
 * @see
 */
@Service
public class FriendsRequestServiceImpl implements FriendsRequestService {
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer addFriendRequest(String sendUserId, String acceptUserId) {
        Example afre = new Example(FriendsRequest.class);
        Example.Criteria afrc = afre.createCriteria();
        afrc.andEqualTo("sendUserId",sendUserId);
        afrc.andEqualTo("acceptUserId",acceptUserId);
        FriendsRequest fr1 = friendsRequestMapper.selectOneByExample(afre);
        if (fr1 != null)
        {
            return -1;
        }
        FriendsRequest newFr = FriendsRequest.builder().id(Sid.nextShort())
                                .sendUserId(sendUserId).acceptUserId(acceptUserId)
                                .requestDateTime(new Date())
                                .build();
        return friendsRequestMapper.insert(newFr);
    }

    @Override
    public List<FriendRequestSendVo> queryFriendRequestList(String acceptUserId)
    {
        List<FriendRequestSendVo> requestList = friendsRequestMapper.queryFriendRequestList(acceptUserId);
        if (requestList.size() < 1)
        {
            return null;
        }
        return requestList;
    }

    @Override
    public boolean deleteFriendRequest(String sendUserId, String acceptUserId) {
        Example fe = new Example(FriendsRequest.class);
        Example.Criteria fc = fe.createCriteria();
        fc.andEqualTo("sendUserId", sendUserId);
        fc.andEqualTo("acceptUserId", acceptUserId);
        return friendsRequestMapper.deleteByExample(fe) > 0;
    }
}