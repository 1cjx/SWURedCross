package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddRoleDto;
import com.jx.domain.dto.ChangeRoleStatusDto;
import com.jx.domain.dto.ListRoleDto;
import com.jx.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
@Api(tags = "角色相关接口")
public class RoleController {
    @Autowired
    RoleService roleService;
    @SystemLog(businessName = "分页查询角色信息",type="2")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    public ResponseResult list(Long pageNum, Long pageSize, ListRoleDto listRoleDto){
        return roleService.listRoles(pageNum,pageSize,listRoleDto);
    }
    @SystemLog(businessName = "修改角色状态",type="2")
    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('system:role:changeStatus')")
    public ResponseResult changeRoleStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        return roleService.changeStatus(changeRoleStatusDto);
    }
    @SystemLog(businessName = "新增角色",type="2")
    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }
    @SystemLog(businessName = "查询选中角色详情信息",type="2")
    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    public ResponseResult getRoleDetail(@PathVariable("id") Long id){
        return roleService.getRoleDetail(id);
    }
    @SystemLog(businessName = "更新角色信息",type="2")
    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    public ResponseResult updateRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.updateRole(addRoleDto);
    }
    @SystemLog(businessName = "删除角色",type="2")
    @DeleteMapping
    @PreAuthorize("@ps.hasPermission('system:role:remove')")
    public ResponseResult Delete(@RequestBody List<Long> roleIds){
        return roleService.deleteRoles(roleIds);
    }

}
