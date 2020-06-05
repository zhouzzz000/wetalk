package com.zhou.wetalk.controller;

import com.zhou.wetalk.dto.UploadMomentDto;
import com.zhou.wetalk.global.hock.UnifyJsonResult;
import com.zhou.wetalk.pojo.User;
import com.zhou.wetalk.service.MomentService;
import com.zhou.wetalk.utils.FastDFSClient;
import com.zhou.wetalk.utils.FileUtils;
import com.zhou.wetalk.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/28
 * @Time 17:47
 * @ClassName MomentController
 * @see
 */
@RestController
@RequestMapping("/moment")
public class MomentController {
    @Autowired
    private MomentService momentService;
    @Autowired
    private FastDFSClient fastDFSClient;

    @GetMapping("/all")
    @ResponseBody
    public UnifyJsonResult selectMomentsByUserId(@RequestParam @NotBlank String userId)
    {
        List list = momentService.selectMomentsByUserId(userId);
        return UnifyJsonResult.ok(list);
    }

    @GetMapping("/friend")
    @ResponseBody
    public UnifyJsonResult selectFriendsMomentsByUserId(@RequestParam @NotBlank String userId)
    {
        List list = momentService.selectFriendMomentsByUserId(userId);
        return UnifyJsonResult.ok(list);
    }

    @PostMapping("/upload")
    @ResponseBody
    public UnifyJsonResult uploadMoment(@RequestBody UploadMomentDto uploadMomentDto) throws Exception {
        ArrayList<String> imagesBase64Data = uploadMomentDto.getImagesBase64Data();
        int i = 0;
        ArrayList<String> imgsUrl = new ArrayList<>();
        for (String item : imagesBase64Data) {
            String imagePath= "H:\\avators\\" + uploadMomentDto.getUserId() + "moment"+i+".png";
            String originPath = uploadImageByBase64Data(item,imagePath);
            if (originPath != null)
            {
                i++;
                imgsUrl.add(originPath);
            }
        }
        int res = momentService.uploadMoment(uploadMomentDto.getUserId(),uploadMomentDto.getContent(),
                                    imgsUrl);
        if (res > 0)
        {
            return UnifyJsonResult.ok();
        }else{
            return UnifyJsonResult.errorMsg("发表失败...");
        }
    }


    public String uploadImageByBase64Data(String base64Data, String filePath) throws Exception {
        FileUtils.base64ToFile(filePath, base64Data);
        MultipartFile imgFile = FileUtils.fileToMultipart(filePath);
        if (imgFile == null) {
            return null;
        }
        //上传文件到fastdfs
        String originUrl = fastDFSClient.uploadBase64NoThrumb(imgFile);
        System.out.println(originUrl);
        return originUrl;
    }
}