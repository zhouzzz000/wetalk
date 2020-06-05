package com.zhou.wetalk.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 15:51
 * @ClassName FriendsRequestDto
 * @see
 */
@Data
@Validated
public class FriendsRequestDto {
    @NotBlank
    private String sendUserId;
    @NotBlank
    private String acceptUserId;

    private Date requestDateTime;
}