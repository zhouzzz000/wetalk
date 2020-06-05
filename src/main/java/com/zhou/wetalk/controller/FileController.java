package com.zhou.wetalk.controller;

import com.zhou.wetalk.dto.UserAvatorDto;
import com.zhou.wetalk.global.hock.UnifyJsonResult;
import com.zhou.wetalk.mapper.UserMapper;
import com.zhou.wetalk.pojo.User;
import com.zhou.wetalk.service.UserService;
import com.zhou.wetalk.utils.FastDFSClient;
import com.zhou.wetalk.utils.FileUtils;
import com.zhou.wetalk.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/12
 * @Time 14:05
 * @ClassName FileController
 * @see
 */
@PropertySource("classpath:config/api.properties")
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FastDFSClient fastDFSClient;
    @Value("${fastdfs.base_image_url}")
    private String baseFileUrl;
    @Autowired
    private UserService userService;
    @PostMapping("/image/uploadFaceBase64")
    public UnifyJsonResult imageUpload(@RequestBody @Validated UserAvatorDto avatorDto) throws Exception {
        String base64Data = avatorDto.getFaceBase64Data();
        String userAvatorPath = "H:\\avators\\" + avatorDto.getUserId() + "useravatorbase64.png";
        FileUtils.base64ToFile(userAvatorPath, base64Data);
        MultipartFile avatorFile = FileUtils.fileToMultipart(userAvatorPath);
        if (avatorFile == null) {
            return UnifyJsonResult.errorMsg("图片上传失败");
        }
        //上传文件到fastdfs
        String originUrl = fastDFSClient.uploadBase64(avatorFile);
        System.out.println(originUrl);

        //获取缩略图的url
        // 缩略图的后缀
        String thump = "_80x80.";
        String[] arr = originUrl.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];
        User user = User.builder().id(avatorDto.getUserId())
                        .faceImageFull(originUrl).faceImage(thumpImgUrl)
                        .build();

        UserVo userVo = new UserVo();
        User res = userService.updateUserInfo(user);
        BeanUtils.copyProperties(res,userVo);
        return UnifyJsonResult.ok(userVo);
    }

    @PostMapping("/image/upload_moment_img")
    public UnifyJsonResult momentBackgrounImageUpload(@RequestBody @Validated UserAvatorDto avatorDto) throws Exception {
        String base64Data = avatorDto.getFaceBase64Data();
        String userAvatorPath = "H:\\avators\\" + avatorDto.getUserId() + "usermomentbacbase64.png";
        FileUtils.base64ToFile(userAvatorPath, base64Data);
        MultipartFile avatorFile = FileUtils.fileToMultipart(userAvatorPath);
        if (avatorFile == null) {
            return UnifyJsonResult.errorMsg("图片上传失败");
        }
        //上传文件到fastdfs
        String originUrl = fastDFSClient.uploadBase64NoThrumb(avatorFile);
        System.out.println(originUrl);

        User user = User.builder().id(avatorDto.getUserId())
                .momentBackgroundImg(originUrl)
                .build();

        UserVo userVo = new UserVo();
        User res = userService.updateUserInfo(user);
        BeanUtils.copyProperties(res,userVo);
        return UnifyJsonResult.ok(userVo);
    }

    @PostMapping(value = "/audio/upload",headers = "content-type=multipart/form-data")
    public UnifyJsonResult uploadAudio(@Validated @NonNull MultipartFile audioFile) throws Exception {

        if (audioFile == null) {
            return UnifyJsonResult.errorMsg("语音上传失败");
        }
        //上传文件到fastdfs
        String originUrl = fastDFSClient.uploadBase64Audio(audioFile);
        String fullUrl = baseFileUrl + originUrl;
        return UnifyJsonResult.ok(fullUrl);
    }

    @PostMapping(value = "/image/upload",headers = "content-type=multipart/form-data")
    public UnifyJsonResult uploadImage(@Validated @NonNull MultipartFile file) throws Exception {

        if (file == null) {
            return UnifyJsonResult.errorMsg("图片上传失败");
        }
        //上传文件到fastdfs
        String originUrl = fastDFSClient.uploadBase64Audio(file);
        String fullUrl = baseFileUrl + originUrl;
        return UnifyJsonResult.ok(fullUrl);
    }
}