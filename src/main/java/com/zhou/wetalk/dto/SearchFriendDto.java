package com.zhou.wetalk.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 14:46
 * @ClassName SearchFriendDto
 * @see
 */
@Data
@Validated
public class SearchFriendDto {
    @NotBlank
    private String myUserId;

    private String friendUserName;

    private String friendUserId;
}