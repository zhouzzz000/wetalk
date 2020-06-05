package com.zhou.wetalk;

import com.zhou.wetalk.controller.FriendsController;
import com.zhou.wetalk.dto.FriendsRequestDto;
import com.zhou.wetalk.global.hock.UnifyJsonResult;
import com.zhou.wetalk.pojo.FriendsRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
@Slf4j
class WetalkApplicationTests {

    @Autowired
    private FriendsController friendsController;

    @Test
    void contextLoads() {
    }
    private ArrayList<FriendTestDto> testArrList = new ArrayList<FriendTestDto>(){{
        add(new FriendTestDto("200413C3Z39PSAY8","200413C3Z39PSAY8"));
        add(new FriendTestDto("200413C3Z39PSAY8","200413C3Z39PSAY10"));
        add(new FriendTestDto("200413C3Z39PSAY8",""));
        add(new FriendTestDto("","200413C3Z39PSAY8"));
        add(new FriendTestDto("200413C3Z39PSAY8","200424FAR07MMB7C"));
        add(new FriendTestDto("200413C3Z39PSAY8","200504F7KK5838PH"));
        add(new FriendTestDto("200413C3Z39PSAY8","200504F7KK5838PH"));
    }};
    @Test
    void addFriendTest(){
        UnifyJsonResult res = null;
        FriendsRequestDto friendsRequestDto = new FriendsRequestDto();
        for (int i = 0; i < testArrList.size(); i++) {
            FriendTestDto testDto = testArrList.get(i);
            friendsRequestDto.setSendUserId(testDto.getSendUserId());
            friendsRequestDto.setAcceptUserId(testDto.getAcceptUserId());
            res = friendsController.add(friendsRequestDto);
            log.info("测试结果:"+"状态码-"+res.getStatus() + "  结果信息-" + res.getMsg());
        }
    }
    @Data
    private class FriendTestDto{
        private String sendUserId;
        private String acceptUserId;
        FriendTestDto( String sendUserId, String acceptUserId)
        {
            this.sendUserId = sendUserId;
            this.acceptUserId = acceptUserId;
        }
    }
}
