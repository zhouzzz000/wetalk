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
@Table(name = "moment")
public class Moment {
    @Id
    private String id;

    private String userId;

    private String content;

    private String images;

    private String userNickname;

    private String userAvator;

    private Date createTime;

    private java.util.Date updateTime;

    private java.util.Date deleteTime;


}