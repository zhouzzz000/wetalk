package com.zhou.wetalk.service;

import com.zhou.wetalk.pojo.Moment;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/28
 * @Time 17:51
 * @ClassName MomentService
 * @see
 */
public interface MomentService extends BaseService{
    List<Moment> selectMomentsByUserId(String userId);
    List<Moment>  selectFriendMomentsByUserId(String userId);
    int uploadMoment(String userId, String content, ArrayList<String> imgUrls);
}