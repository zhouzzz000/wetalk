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
@Table(name = "friends")
public class Friends {
    @Id
    private String id;

    private String userId;

    private String friendUserId;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;


}