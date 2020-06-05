package com.zhou.wetalk.service;

import com.zhou.wetalk.dto.UserDto;
import com.zhou.wetalk.pojo.User;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/9
 * @Time 13:47
 * @InterfaceName UserService
 * @Description
 */
public interface UserService extends BaseService{
    public boolean usernameIsExist(String username);

    public User userLogin(UserDto userDto);

    public User userRegister(User user);

    public User updateUserInfo(User user);

    public User queryUserById(String userId);

    public User queryUserByName(String userName);
}
