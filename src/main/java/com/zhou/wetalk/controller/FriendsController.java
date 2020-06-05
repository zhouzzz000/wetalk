package com.zhou.wetalk.controller;

import com.zhou.utils.JsonUtils;
import com.zhou.wetalk.dto.DataContentDto;
import com.zhou.wetalk.dto.FriendsRequestDto;
import com.zhou.wetalk.dto.SearchFriendDto;
import com.zhou.wetalk.enums.MsgAction;
import com.zhou.wetalk.enums.SearchFriendsStatusEnum;
import com.zhou.wetalk.global.hock.UnifyJsonResult;
import com.zhou.wetalk.netty.websocket.UserChannelRel;
import com.zhou.wetalk.pojo.User;
import com.zhou.wetalk.service.FriendsRequestService;
import com.zhou.wetalk.service.FriendsService;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.utils.StringUtils;
import com.zhou.wetalk.vo.UserVo;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/14
 * @Time 12:54
 * @ClassName FriendsController
 * @see
 */
@Validated
@RestController
@RequestMapping("/friends")
public class FriendsController {
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendsRequestService friendsRequestService;

    @PostMapping("/search")
    @ResponseBody
    public UnifyJsonResult searchUser(@RequestBody @Validated SearchFriendDto searchFriendDto)
    {
        String myUserId = searchFriendDto.getMyUserId();
        String friendUserName = searchFriendDto.getFriendUserName();
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUserName))
        {
            return UnifyJsonResult.errorMsg("参数错误...");
        }
        Integer status = friendsService.preconditionOfSearchFriends(myUserId, friendUserName);
       if (status.equals(SearchFriendsStatusEnum.SUCCESS.status))
       {
           User frinend = userService.queryUserByName(friendUserName);
           UserVo userVo = new UserVo();
           BeanUtils.copyProperties(frinend, userVo);
           return UnifyJsonResult.ok(userVo);
       }else{
           String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
           return UnifyJsonResult.errorMsg(errorMsg);
       }
    }

    @PostMapping("/search_id")
    @ResponseBody
    public UnifyJsonResult searchUserById(@RequestBody @Validated SearchFriendDto searchFriendDto)
    {
        String myUserId = searchFriendDto.getMyUserId();
        String friendUserId = searchFriendDto.getFriendUserId();
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUserId))
        {
            return UnifyJsonResult.errorMsg("参数错误...");
        }
        User friendUser = userService.queryUserById(friendUserId);
        if (friendUser == null)
        {
            return UnifyJsonResult.errorMsg("该用户不存在...");
        }
        String friendUserName = friendUser.getUsername();
        Integer status = friendsService.preconditionOfSearchFriends(myUserId, friendUserName);
        if (status.equals(SearchFriendsStatusEnum.SUCCESS.status))
        {
            User frinend = userService.queryUserByName(friendUserName);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(frinend, userVo);
            return UnifyJsonResult.ok(userVo);
        }else{
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return UnifyJsonResult.errorMsg(errorMsg);
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public UnifyJsonResult add(@RequestBody @Validated FriendsRequestDto friendsRequestDto)
    {
        String myUserId = friendsRequestDto.getSendUserId();
        String friendUserId = friendsRequestDto.getAcceptUserId();
        if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUserId))
        {
            return UnifyJsonResult.errorMsg("参数错误:请求发送者和接受者Id不能为空");
        }
        User friendUser = userService.queryUserById(friendUserId);
        if (friendUser == null)
        {
            return UnifyJsonResult.errorMsg("该用户不存在...");
        }
        String friendUserName = friendUser.getUsername();
        Integer status = friendsService.preconditionOfSearchFriends(myUserId, friendUserName);
        if (status.equals(SearchFriendsStatusEnum.SUCCESS.status))
        {
            Integer resNum = friendsRequestService.addFriendRequest(myUserId, friendUserId);
            if(resNum == -1)
            {
                return UnifyJsonResult.errorMsg("请求已发送，等待对方接受...");
            }
            if (resNum != 1)
            {
                return UnifyJsonResult.errorMsg("请求发送失败...");
            }
            return UnifyJsonResult.ok("好友请求发送成功...");
        }else{
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return UnifyJsonResult.errorMsg(errorMsg);
        }

    }

    @PostMapping("/accept")
    @ResponseBody
    public UnifyJsonResult accept(@RequestBody @Validated FriendsRequestDto friendsRequestDto)
    {
        String sendUserId = friendsRequestDto.getSendUserId();
        String acceptUserId = friendsRequestDto.getAcceptUserId();
        Integer x = friendsService.addNewFriendRelative(sendUserId, acceptUserId);
        if (x.equals(SearchFriendsStatusEnum.SUCCESS.status))
        {
            friendsRequestService.deleteFriendRequest(sendUserId,acceptUserId);
            // 给好友请求发送方发送一条好友通过的消息，用于刷新好友列表
            Channel sendChannel = UserChannelRel.get(sendUserId);
            if (sendChannel != null) {
                DataContentDto dataContent = new DataContentDto();
                dataContent.setAction(MsgAction.REFRESH_FRIEND.type);
                sendChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
            }
            return UnifyJsonResult.ok();
        }
        return UnifyJsonResult.errorMsg("添加失败...");
    }
    @PostMapping("/refuse")
    @ResponseBody
    public UnifyJsonResult refuse(@RequestBody @Validated FriendsRequestDto friendsRequestDto)
    {
        String sendUserId = friendsRequestDto.getSendUserId();
        String acceptUserId = friendsRequestDto.getAcceptUserId();
        if (friendsRequestService.deleteFriendRequest(sendUserId,acceptUserId)) {
            return UnifyJsonResult.ok();
        }
        return UnifyJsonResult.errorMsg("删除失败...");
    }

    @GetMapping("/all")
    @ResponseBody
    public UnifyJsonResult getFriends(@RequestParam @NotBlank String userId)
    {
        List list = friendsService.getFriendsListById(userId);
        return UnifyJsonResult.ok(list);
    }
}