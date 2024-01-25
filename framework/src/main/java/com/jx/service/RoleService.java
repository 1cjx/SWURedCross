package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddRoleDto;
import com.jx.domain.dto.ChangeRoleStatusDto;
import com.jx.domain.dto.ListRoleDto;
import com.jx.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-10-14 15:12:32
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRoles(Integer pageNum, Integer pageSize, ListRoleDto listRoleDto);

    ResponseResult changeStatus(ChangeRoleStatusDto changeRoleStatusDto);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleDetail(Long id);

    ResponseResult updateRole(AddRoleDto addRoleDto);

    ResponseResult listAllRole();

    ResponseResult deleteRoles(List<Long> roleIds);
}

