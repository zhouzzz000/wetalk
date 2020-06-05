package com.zhou.wetalk.vo;

import lombok.Data;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 17:09
 * @ClassName FriendRequestVo
 * @see
 */
@Data
public class FriendRequestSendVo {
    private String sendUserId;
    private String sendFaceImage;
    private String sendUsername;
    private String sendUserNickname;
}