package com.zhou.wetalk.dto;

import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/12
 * @Time 14:08
 * @ClassName UserAvatorDto
 * @see
 */
@Data
@Validated
public class UserAvatorDto {
    @NonNull
    private String userId;
    @NonNull
    private String faceBase64Data;
}