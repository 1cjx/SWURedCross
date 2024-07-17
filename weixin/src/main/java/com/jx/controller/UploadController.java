package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "上传相关接口")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @SystemLog(businessName = "上传头像",type="1")
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img,String path) {
        return uploadService.uploadImg(img,path);
    }
}