package com.zhou.wetalk.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    private String id;

    private String username;

    private String password;

    private String faceImage;

    private String faceImageFull;

    private String nickname;

    private String qrcode;

    private String cid;

    private String momentBackgroundImg;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;

}