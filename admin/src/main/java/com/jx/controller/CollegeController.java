package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.service.CollegeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "学院相关接口")
@RestController
@RequestMapping("/system/college")
public class CollegeController {
    @Autowired
    CollegeService collegeService;

    /**
     * 关键词搜索
     * @param name
     * @return
     */
    @SystemLog(businessName = "根据学院名模糊查询学院",type="2")
    @GetMapping("/select")
    public ResponseResult list(String name){
        return collegeService.selectColleges(name);
    }
}
