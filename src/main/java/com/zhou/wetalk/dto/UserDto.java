package com.zhou.wetalk.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/9
 * @Time 13:09
 * @ClassName UserDto
 * @see
 */
@Data
@Validated
public class UserDto {
    private String id;
    private String username;
    private String password;
    private String cid;
    private String nickname;
    private String momentBackgroundImg;
}