package com.jx.service;

import com.jx.domain.bean.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    ResponseResult uploadImg(MultipartFile img,String path);
}
