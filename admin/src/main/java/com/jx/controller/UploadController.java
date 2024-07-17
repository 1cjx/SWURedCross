package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "图片上传接口")
@RequestMapping("/system")
public class UploadController {

    @Autowired
    private UploadService uploadService;
    @SystemLog(businessName = "上传图片",type="2")
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile multipartFile,String path) {
        return uploadService.uploadImg(multipartFile,path);
    }
}