package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.dto.ListRoleDto;
import com.jx.domain.entity.Role;
import com.jx.domain.vo.ListRoleVo;
import com.jx.domain.vo.RoleVo;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-14 15:12:31
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long userId);

    List<ListRoleVo> getRoleList(ListRoleDto listRoleDto);
}

