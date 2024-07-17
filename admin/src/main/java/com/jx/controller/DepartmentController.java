package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.*;
import com.jx.service.DepartmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @SystemLog(businessName = "分页查询部门",type="2")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:department:query')")
    public ResponseResult list(Integer pageNum, Integer pageSize, ListDepartmentDto listDepartmentDto){
        return departmentService.listDepartment(pageNum,pageSize,listDepartmentDto);
    }
    @SystemLog(businessName = "查询选中部门详情信息",type="2")
    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:department:edit')")
    public ResponseResult getDepartmentDetail(@PathVariable("id") Long id){
        return departmentService.getDepartmentDetail(id);
    }
    @SystemLog(businessName = "更新部门信息",type="2")
    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:department:edit')")
    public ResponseResult updateDepartment(@RequestBody AddDepartmentDto addDepartmentDto){
        return departmentService.updateDepartment(addDepartmentDto);
    }
    @SystemLog(businessName = "修改部门状态",type="2")
    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('content:department:changeStatus')")
    public ResponseResult changeStatus(@RequestBody AddDepartmentDto addDepartmentDto){
        return  departmentService.changeStatus(addDepartmentDto);
    }
    @SystemLog(businessName = "新增部门",type="2")
    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:department:add')")
    public ResponseResult addDepartment(@RequestBody AddDepartmentDto addDepartmentDto){
        return departmentService.addDepartment(addDepartmentDto);
    }
    @SystemLog(businessName = "删除部门",type="2")
    @DeleteMapping
    @PreAuthorize("@ps.hasPermission('content:department:remove')")
    public ResponseResult deleteDepartment(@RequestBody List<Long> departmentIds){
        return departmentService.deleteDepartment(departmentIds);
    }
}
