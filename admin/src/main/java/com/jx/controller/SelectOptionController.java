package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.ResponseResult;
import com.jx.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/select")
@Api(tags = "下拉选择项获取相关接口")
public class SelectOptionController {
    @Autowired
    TitleService titleService;
    @Autowired
    ActivityAssignmentTypeService activityAssignmentTypeService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    LocationService locationService;
    @Autowired
    PostService postService;
    @Autowired
    TimeSlotService timeSlotService;
    @SystemLog(businessName = "查询所有班次类型",type="2")
    @GetMapping("/listAllActivityAssignmentType")
    public ResponseResult listAllActivityAssignmentType(){
        return activityAssignmentTypeService.listAllActivityAssignmentType();
    }
    @SystemLog(businessName = "查询所有职称",type="2")
    @GetMapping("/listAllTitle")
    public ResponseResult listAllTitle(){
        return titleService.listAllTitle();
    }
    @SystemLog(businessName = "查询所有学院",type="2")
    @GetMapping("/listAllCollege")
    public ResponseResult listAllCollege(){
        return collegeService.listAllCollege();
    }
    @SystemLog(businessName = "查询所有部门",type="2")
    @GetMapping("/listAllDepartment")
    public ResponseResult listAllDepartment(){
        return departmentService.listAllDepartment();
    }
    @SystemLog(businessName = "查询所有活动地点",type="2")
    @GetMapping("/listAllLocation")
    public ResponseResult listAllLocation(){
        return locationService.listAllLocation();
    }
    @SystemLog(businessName = "查询当前活动分类下的所有岗位",type="2")
    @GetMapping("/listAllPost/{categoryId}")
    public ResponseResult listAllPost(@PathVariable("categoryId") Long categoryId){
        return postService.listAllPost(categoryId);
    }
    @SystemLog(businessName = "查询所有活动时间段",type="2")
    @GetMapping("/listAllTimeSlot")
    public ResponseResult listAllTimeSlot(){
        return timeSlotService.listAllTimeSlot();
    }
    @SystemLog(businessName = "查询所有活动分类",type="2")
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
}
