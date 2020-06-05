package com.zhou.wetalk.controller;

import com.zhou.utils.MD5Utils;
import com.zhou.wetalk.dto.UserDto;
import com.zhou.wetalk.global.hock.UnifyJsonResult;
import com.zhou.wetalk.pojo.FriendsRequest;
import com.zhou.wetalk.pojo.Message;
import com.zhou.wetalk.pojo.User;
import com.zhou.wetalk.service.FriendsRequestService;
import com.zhou.wetalk.service.FriendsService;
import com.zhou.wetalk.service.MessageService;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.utils.StringUtils;
import com.zhou.wetalk.utils.VerifyImage;
import com.zhou.wetalk.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/8
 * @Time 18:34
 * @ClassName UserController
 * @see
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VerifyImage verifyImage;

    @Autowired
    private FriendsRequestService friendsRequestService;

    @Transactional(propagation = Propagation.REQUIRED)
    @PostMapping("/login")
    public UnifyJsonResult login(@RequestBody UserDto userDto) throws Exception {
        if (StringUtils.isBlank(userDto.getUsername()) || StringUtils.isBlank(userDto.getPassword()))
        {
            return UnifyJsonResult.errorMsg("用户名和密码不能为空");
        }
        boolean isExist = userService.usernameIsExist(userDto.getUsername());
        User userRes= null;
        if (isExist){
            userDto.setPassword(MD5Utils.getMD5Str(userDto.getPassword()));
            userRes = userService.userLogin(userDto);
            if(userRes == null)
            {
                return UnifyJsonResult.errorMsg("用户名或密码不正确");
            }
        }else {
            User user = User.builder().username(userDto.getUsername()).cid(userDto.getCid())
                        .nickname(userDto.getUsername()).faceImage("")
                        .faceImageFull("").password(MD5Utils.getMD5Str(userDto.getPassword()))
                        .build();
            userRes = userService.userRegister(user);
            if (userRes == null)
            {
                return UnifyJsonResult.errorMsg("注册失败");
            }
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userRes,userVo);
        return UnifyJsonResult.ok(userVo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @PostMapping("/update/nickname")
    public UnifyJsonResult updataNickname(@RequestBody UserDto userDto) throws Exception {
        User user = User.builder().id(userDto.getId())
                        .nickname(userDto.getNickname()).build();
        User res = userService.updateUserInfo(user);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(res,userVo);
        return UnifyJsonResult.ok(userVo);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @GetMapping("/friend_request_list")
    @ResponseBody
    public UnifyJsonResult friendRequestList(@RequestParam @NotBlank String acceptUserId)
    {
        List requestList = friendsRequestService.queryFriendRequestList(acceptUserId);
        if (requestList == null)
        {
            return UnifyJsonResult.errorMsg("当前没有好友请求...");
        }
        return UnifyJsonResult.ok(requestList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @GetMapping("/seach_id")
    @ResponseBody
    public UnifyJsonResult searchUserById(@RequestParam @NotBlank String userId)
    {
       User user = userService.queryUserById(userId);
       if (user == null)
       {
           return UnifyJsonResult.errorMsg("用户不存在");
       }
       UserVo userVo = new UserVo();
       BeanUtils.copyProperties(user, userVo);
       return UnifyJsonResult.ok(userVo);
    }

    @GetMapping("/getUnreadMsgList")
    @ResponseBody
    public UnifyJsonResult getUnreadMsgList(@RequestParam @NotBlank String userId)
    {
        List<Message> unreadList = messageService.getUnreadMessageList(userId);
        return UnifyJsonResult.ok(unreadList);
    }

    @GetMapping("/verify_image")
    public void createImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            verifyImage.getRandcode(request, response);//输出验证码图片
            //将生成的随机验证码存放到redis中
        } catch (Exception e) {
            log.error("获取二维码失败:"+e.getMessage());
            e.printStackTrace();
        }
    }
}