package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.*;
import com.jx.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/department")
@Api(tags = "部门相关接口")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    /**
     * 分页查询部门信息
     * @param pageNum
     * @param pageSize
     * @param listDepartmentDto
     * @return
     */
    @ApiOperation("分页查询部门")
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ListDepartmentDto listDepartmentDto){
        return departmentService.listDepartment(pageNum,pageSize,listDepartmentDto);
    }
    @ApiOperation("查询选中部门详情信息")
    @GetMapping("/{id}")
    public ResponseResult getDepartmentDetail(@PathVariable("id") Long id){
        return departmentService.getDepartmentDetail(id);
    }
    @ApiOperation("更新部门信息")
    @PutMapping
    public ResponseResult updateDepartment(@RequestBody AddDepartmentDto addDepartmentDto){
        return departmentService.updateDepartment(addDepartmentDto);
    }
    @ApiOperation("修改部门状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody AddDepartmentDto addDepartmentDto){
        return  departmentService.changeStatus(addDepartmentDto);
    }
    @ApiOperation("新增部门")
    @PostMapping
    public ResponseResult addDepartment(@RequestBody AddDepartmentDto addDepartmentDto){
        return departmentService.addDepartment(addDepartmentDto);
    }
    @ApiOperation("删除部门")
    @DeleteMapping
    public ResponseResult deleteDepartment(@RequestBody List<Long> departmentIds){
        return departmentService.deleteDepartment(departmentIds);
    }
}
