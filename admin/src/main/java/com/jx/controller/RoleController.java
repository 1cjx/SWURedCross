package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddRoleDto;
import com.jx.domain.dto.ChangeRoleStatusDto;
import com.jx.domain.dto.ListRoleDto;
import com.jx.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
@Api(tags = "角色相关接口")
public class RoleController {
    @Autowired
    RoleService roleService;
    @ApiOperation("分页查询角色信息")
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ListRoleDto listRoleDto){
        return roleService.listRoles(pageNum,pageSize,listRoleDto);
    }
    @ApiOperation("修改角色状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto){
        return roleService.changeStatus(changeRoleStatusDto);
    }
    @ApiOperation("新增角色")
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }
    @ApiOperation("查询选中角色详情信息")
    @GetMapping("/{id}")
    public ResponseResult getRoleDetail(@PathVariable("id") Long id){
        return roleService.getRoleDetail(id);
    }
    @ApiOperation("更新角色信息")
    @PutMapping
    public ResponseResult updateRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.updateRole(addRoleDto);
    }
    @ApiOperation("删除角色")
    @DeleteMapping
    public ResponseResult Delete(@RequestBody List<Long> roleIds){
        return roleService.deleteRoles(roleIds);
    }

}
