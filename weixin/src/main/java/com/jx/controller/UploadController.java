package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "上传相关接口")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @ApiOperation("上传头像")
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img,String path) {
        return uploadService.uploadImg(img,path);
    }
}