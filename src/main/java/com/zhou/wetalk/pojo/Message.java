package com.zhou.wetalk.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String content;

    private Integer state;

    private Integer type;

    private String sendUserAvator;

    private String acceptUserAvator;

    private Date createTime;

    private java.util.Date updateTime;

    private java.util.Date deleteTime;


}