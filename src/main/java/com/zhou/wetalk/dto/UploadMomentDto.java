package com.zhou.wetalk.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/28
 * @Time 18:02
 * @ClassName UploadMomentDto
 * @see
 */
@Data
@Validated
public class UploadMomentDto {
    @NotBlank
    private String userId;
    private String content;
    private ArrayList<String> imagesBase64Data;
}