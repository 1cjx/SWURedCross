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
import com.jx.domain.entity.User;
import com.jx.domain.vo.ListRoleVo;
import com.jx.domain.vo.PageVo;
import com.jx.domain.vo.RoleVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.RoleMapper;
import com.jx.service.*;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.PageUtils;
import com.jx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private TitleService titleService;
    @Autowired
    private UserService userService;
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
    public ResponseResult listRoles(Long pageNum, Long pageSize, ListRoleDto listRoleDto) {
        //按条件查询
        List<ListRoleVo>roleVos = roleMapper.getRoleList(listRoleDto);
        //分页查询
        //封装数据返回
        Page roleVoPage = PageUtils.listToPage(roleVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(roleVoPage.getRecords(),roleVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(ChangeRoleStatusDto changeRoleStatusDto) {
        Role role = getById(changeRoleStatusDto.getRoleId());
        role.setStatus(changeRoleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        LambdaQueryWrapper<Role>wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(addRoleDto.getTitleId()),Role::getTitleId,addRoleDto.getTitleId());
        wrapper.eq(!Objects.isNull(addRoleDto.getDepartmentId()),Role::getDepartmentId,addRoleDto.getDepartmentId());
        if(count(wrapper)>0) {
            throw new SystemException(AppHttpCodeEnum.ROLE_EXIST);
        }
        if(Objects.isNull(addRoleDto.getDepartmentId())){
            throw new SystemException((AppHttpCodeEnum.ROLE_DEPARTMENT_NOT_NULL));
        }
        if(Objects.isNull(addRoleDto.getDepartmentId())){
            throw new SystemException((AppHttpCodeEnum.ROLE_TITLE_NOT_NULL));
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
    public ResponseResult deleteRoles(List<Long> roleIds) {
        List<String> ans = new ArrayList<>();
        roleIds.stream().forEach(o->{
            Role role = getById(o);
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
            lambdaQueryWrapper.eq(User::getDepartmentId,role.getDepartmentId());
            lambdaQueryWrapper.eq(User::getTitleId,role.getTitleId());
            if(userService.count(lambdaQueryWrapper)>0){
                //说明该角色有绑定用户
                String departmentName = departmentService.getById(role.getDepartmentId()).getName();
                String titleName = titleService.getById(role.getTitleId()).getName();
                ans.add(departmentName+titleName);
            }
            else{
                //删除角色
                removeById(o);
            }
        });
        if(!ans.isEmpty()){
            return ResponseResult.errorResult(550,"角色"+ans.toString()+"有绑定用户,无法删除");
        }
        return ResponseResult.okResult();
    }

    public void addRoleMenu(Long roleId,List<Long>menuId){
        List<RoleMenu>roleMenus = menuId.stream()
                .map(o->(new RoleMenu(roleId,o))).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
    }
}
