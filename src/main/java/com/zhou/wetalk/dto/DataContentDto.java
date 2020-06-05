package com.zhou.wetalk.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/23
 * @Time 17:52
 * @ClassName DataContentDto
 * @see
 */
@Data
public class DataContentDto implements Serializable {
    private static final  long serialVersionUID = 8021381444738260454L;
    private Integer action;
    private MessageDto message;
    // 扩展字段
    private String extend;
}