package com.zhou.wetalk.service.impl;

import com.zhou.wetalk.idworker.Sid;
import com.zhou.wetalk.mapper.MomentMapper;
import com.zhou.wetalk.pojo.Friends;
import com.zhou.wetalk.pojo.Moment;

import com.zhou.wetalk.pojo.User;
import com.zhou.wetalk.service.FriendsService;
import com.zhou.wetalk.service.MomentService;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
 * @Date 2020/4/28
 * @Time 17:43
 * @ClassName MomentServiceImpl
 * @see
 */
@Service
@PropertySource("classpath:config/api.properties")
public class MomentServiceImpl implements MomentService {
    @Autowired
    private MomentMapper momentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendsService friendsService;
    @Value("${fastdfs.base_image_url}")
    private String imageBaseUrl;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Moment> selectMomentsByUserId(String userId) {
        Example mex = new Example(Moment.class);
        Example.Criteria mcra = mex.createCriteria();
        mcra.andEqualTo("userId",userId);
        List friendsList = friendsService.getFriendsListById(userId);
        ArrayList<String> friendIdList=new ArrayList<>();
        for (int i = 0; i < friendsList.size(); i++) {
            UserVo friend = (UserVo) friendsList.get(i);
            friendIdList.add(friend.getId());
        }
        mcra.orIn("userId", friendIdList);
        mex.setOrderByClause("create_time desc");
        return momentMapper.selectByExample(mex);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Moment> selectFriendMomentsByUserId(String userId) {
        Example mex = new Example(Moment.class);
        Example.Criteria mcra = mex.createCriteria();
        mcra.andEqualTo("userId",userId);
        mex.setOrderByClause("create_time desc");
        return momentMapper.selectByExample(mex);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public int uploadMoment(String userId, String content, ArrayList<String> imgUrls) {
        StringBuilder imgUrlsStr = new StringBuilder();
        for (int i = 0; i <imgUrls.size() ; i++) {
            imgUrlsStr.append(imageBaseUrl).append(imgUrls.get(i)).append(",");
        }
        User user = userService.queryUserById(userId);
        Moment moment = Moment.builder().id(Sid.nextShort())
                              .content(content).images(imgUrlsStr.toString())
                              .createTime(new Date()).userAvator(user.getFaceImage())
                              .userNickname(user.getNickname()).userId(userId).build();
        return momentMapper.insert(moment);
    }
}