package com.zhou.wetalk.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/22
 * @Time 15:42
 * @ClassName MessageDto
 * @see
 */
@Data
public class MessageDto implements Serializable {
    private static final  long serialVersionUID = 3611169682695799175L;
    private String sendUserId;
    private String acceptUserId;
    private String sendUserAvator;
    private String acceptUserAvator;
    private String content;
    private String id;
    private Date date;
    private int type;
}