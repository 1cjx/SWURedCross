package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddRoleDto;
import com.jx.domain.dto.ChangeRoleStatusDto;
import com.jx.domain.dto.ListRoleDto;
import com.jx.domain.entity.Role;
import com.jx.domain.entity.RoleMenu;
import com.jx.domain.vo.PageVo;
import com.jx.domain.vo.RoleVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.RoleMapper;
import com.jx.service.RoleMenuService;
import com.jx.service.RoleService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-10-14 15:12:32
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(SecurityUtils.isAdmin()){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);

    }

    @Override
    public ResponseResult listRoles(Integer pageNum, Integer pageSize, ListRoleDto listRoleDto) {
        //分页查询
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(listRoleDto.getStatus()),Role::getStatus,listRoleDto.getStatus());
        wrapper.like(StringUtils.hasText(listRoleDto.getRoleName()),Role::getRoleName,listRoleDto.getRoleName());
        wrapper.orderByDesc(Role::getRoleSort);
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,wrapper);
        //封装数据返回
        List<RoleVo>roleVos = BeanCopyUtils.copyBeanList(page.getRecords(),RoleVo.class);
        PageVo pageVo = new PageVo(roleVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(ChangeRoleStatusDto changeRoleStatusDto) {
        Role role = getById(changeRoleStatusDto.getRoleID());
        role.setStatus(changeRoleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        LambdaQueryWrapper<Role>wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleName,addRoleDto.getRoleName());
        if(count(wrapper)>0) {
            throw new SystemException(AppHttpCodeEnum.ROLE_EXIST);
        }
        if(!StringUtils.hasText(addRoleDto.getRoleName())){
            throw new SystemException((AppHttpCodeEnum.ROLE_NAME_NOT_NULL));
        }
        if(!StringUtils.hasText(addRoleDto.getRoleKey())) {
            throw new SystemException((AppHttpCodeEnum.ROLE_KEY_NOT_NULL));
        }
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);

        save(role);
        addRoleMenu(role.getId(), addRoleDto.getMenuIds());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleDetail(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role,RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult updateRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        updateById(role);
        //删除原来的menu
        LambdaQueryWrapper<RoleMenu>wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,addRoleDto.getId());
        roleMenuService.remove(wrapper);
        //修改menu
        List<Long>menuIds = addRoleDto.getMenuIds();
        List<RoleMenu>roleMenus = menuIds.stream()
                .map(roleMenuId->new RoleMenu(addRoleDto.getId(),roleMenuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<RoleVo>  roles = BeanCopyUtils.copyBeanList(list(wrapper),RoleVo.class);
        return ResponseResult.okResult(roles);
    }

    public void addRoleMenu(Long roleId,List<Long>menuId){
        List<RoleMenu>roleMenus = menuId.stream()
                .map(o->(new RoleMenu(roleId,o))).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
    }
}
