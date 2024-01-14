package com.jx.controller;

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
    RoleService roleService;
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
    @ApiOperation("查询所有班次类型")
    @GetMapping("/listAllActivityAssignmentType")
    public ResponseResult listAllActivityAssignmentType(){
        return activityAssignmentTypeService.listAllActivityAssignmentType();
    }
    @ApiOperation("查询所有角色")
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
    @ApiOperation("查询所有学院")
    @GetMapping("/listAllCollege")
    public ResponseResult listAllCollege(){
        return collegeService.listAllCollege();
    }
    @ApiOperation("查询所有部门")
    @GetMapping("/listAllDepartment")
    public ResponseResult listAllDepartment(){
        return departmentService.listAllDepartment();
    }
    @ApiOperation("查询所有活动地点")
    @GetMapping("/listAllLocation")
    public ResponseResult listAllLocation(){
        return locationService.listAllLocation();
    }
    @ApiOperation("查询当前活动分类下的所有岗位")
    @GetMapping("/listAllPost/{categoryId}")
    public ResponseResult listAllPost(@PathVariable("categoryId") Long categoryId){
        return postService.listAllPost(categoryId);
    }
    @ApiOperation("查询所有活动时间段")
    @GetMapping("/listAllTimeSlot")
    public ResponseResult listAllTimeSlot(){
        return timeSlotService.listAllTimeSlot();
    }
    @ApiOperation("查询所有活动分类")
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
}
