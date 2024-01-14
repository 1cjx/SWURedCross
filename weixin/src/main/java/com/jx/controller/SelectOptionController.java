package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.service.CategoryService;
import com.jx.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "下拉选择框相关接口")
public class SelectOptionController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    LocationService locationService;
    @ApiOperation("查询所有活动分类")
    @GetMapping("/category/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.listAllCategory();
    }
    @ApiOperation("查询所有活动地点")
    @GetMapping("/location/getLocationList")
    public ResponseResult getLocationList(){
        return locationService.listAllLocation();
    }
}
