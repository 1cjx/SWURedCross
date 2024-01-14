package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.service.CollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

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
    @ApiOperation("根据学院名模糊查询学院")
    @GetMapping("/select")
    public ResponseResult list(String name){
        return collegeService.selectColleges(name);
    }
}
