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
@Table(name = "friends_request")
public class FriendsRequest {
    @Id
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private Date requestDateTime;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;


}