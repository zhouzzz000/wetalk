package com.zhou.wetalk.controller;

import com.zhou.wetalk.exception.http.ForbiddenException;
import com.zhou.wetalk.mapper.UserMapper;
import com.zhou.wetalk.pojo.User;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/7
 * @Time 17:08
 * @ClassName TestController
 * @see
 */
@Log4j2
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    UserMapper userMapper;
    @GetMapping("/test1")
    @ResponseBody
    public int test()
    {
       User user = User.builder().id("3")
               .username("宙宙宙组")
               .password("123")
               .faceImage("z")
               .faceImageFull("x")
               .nickname("x")
               .qrcode("xx")
               .cid("zzz").build();
//       throw new ForbiddenException(10001);

       return userMapper.insert(user);

    }
}