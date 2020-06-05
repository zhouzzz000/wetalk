package com.zhou.wetalk.service.impl;

import com.zhou.wetalk.dto.UserDto;
import com.zhou.wetalk.idworker.Sid;
import com.zhou.wetalk.mapper.UserMapper;
import com.zhou.wetalk.pojo.User;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.utils.FastDFSClient;
import com.zhou.wetalk.utils.FileUtils;
import com.zhou.wetalk.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/9
 * @Time 13:27
 * @ClassName UserServiceImpl
 * @see
 */
@Service
@PropertySource("classpath:config/api.properties")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Value("${fastdfs.base_image_url}")
    private String imageBaseUrl;
    @Value("${default_faceImage_url}")
    private String defaultFaceImageUrl;
    @Value("${default_faceImageFull_url}")
    private String defaultFaceImageFullUrl;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean usernameIsExist(String username) {
        User user = User.builder().username(username).build();
        return userMapper.selectOne(user)!=null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User userLogin(UserDto userDto)
    {
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username",userDto.getUsername());
        criteria.andEqualTo("password",userDto.getPassword());

        User res = userMapper.selectOneByExample(userExample);
        return res;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User userRegister(User user) {
        String userId = sid.nextShort();

        String qrCodePath = "H:\\avators\\" + userId + "qrcode.png";
        qrCodeUtils.createQRCode(qrCodePath,"zhou_qrcode:"+userId);
        MultipartFile qrcodeFile = FileUtils.fileToMultipart(qrCodePath);

        String qrCodeUrl = "";
        try {
           qrCodeUrl = fastDFSClient.uploadQRCode(qrcodeFile);
           qrCodeUrl = imageBaseUrl + qrCodeUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // zhou_qrcode:[username]
        user.setQrcode(qrCodeUrl);
        user.setId(userId);
        user.setFaceImageFull(defaultFaceImageFullUrl);
        user.setFaceImage(defaultFaceImageUrl);
        userMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User updateUserInfo(User user)
    {
        if (user.getFaceImageFull() != null){
            user.setFaceImage(imageBaseUrl+user.getFaceImage());
            user.setFaceImageFull(imageBaseUrl+user.getFaceImageFull());
        }if (user.getMomentBackgroundImg() != null)
        {
            user.setMomentBackgroundImg(imageBaseUrl+user.getMomentBackgroundImg());
        }
        int x = userMapper.updateByPrimaryKeySelective(user);
        return queryUserById(user.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User queryUserById(String userId)
    {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserByName(String userName) {
        Example ue = new Example(User.class);
        Example.Criteria criteria = ue.createCriteria();
        criteria.andEqualTo("username",userName);
        return userMapper.selectOneByExample(ue);
    }
}