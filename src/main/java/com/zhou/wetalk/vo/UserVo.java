package com.zhou.wetalk.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/9
 * @Time 14:00
 * @ClassName UserVo
 * @see
 */
@Data
public class UserVo {
    private String id;

    private String username;

    private String faceImage;

    private String faceImageFull;

    private String nickname;

    private String qrcode;

    private String momentBackgroundImg;
}