package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.ListMenuDto;
import com.jx.domain.dto.RoleMenuTreeDto;
import com.jx.domain.entity.Menu;
import com.jx.domain.entity.RoleMenu;
import com.jx.domain.vo.MenuTreeVo;
import com.jx.domain.vo.MenuVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.MenuMapper;
import com.jx.service.MenuService;
import com.jx.service.RoleMenuService;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.SecurityUtils;
import com.jx.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-10-14 15:09:49
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(SecurityUtils.isAdmin()){
            //返回所有的权限
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu>menus = list(wrapper);
            List<String> perms =  menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserID(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        //判断是否是管理员
        List<Menu>menus = null;
        //如果是 返回所有符合要求的menu
        if(SecurityUtils.isAdmin()){
            menus = menuMapper.selectAllRouterMenu();
        }
        else {
            //否则 当前用户所具有的menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus,MenuVo.class);
        List<MenuVo> menuTree = builderMenuTree(menuVos,0L);

        return menuTree;
    }

    @Override
    public ResponseResult listMenus(ListMenuDto listMenuDto) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(listMenuDto.getStatus()),Menu::getStatus,listMenuDto.getStatus());
        lambdaQueryWrapper.like(StringUtils.hasText(listMenuDto.getMenuName()),Menu::getMenuName,listMenuDto.getMenuName());
        lambdaQueryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(lambdaQueryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus,MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if(menu.getParentId().equals(menu.getId())){
            throw new SystemException(AppHttpCodeEnum.SET_MENU_ERROR);
        }
        updateById(menu);
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu>lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,id);
        if(count(lambdaQueryWrapper)>0){
            throw new SystemException(AppHttpCodeEnum.HAS_CHILD_MENU);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public List<MenuTreeVo> treeSelect() {
        LambdaQueryWrapper<Menu>wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
        wrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(wrapper);
        List<MenuTreeVo> list = SystemConverter.buildMenuSelectTree(menus);
        return list;
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> menuIds = roleMenuService.list(wrapper);
        //角色关联菜单权限id
        List<Long> menus = menuIds.stream().map(menu->menu.getMenuId()).collect(Collectors.toList());
        //菜单树
        List<MenuTreeVo> menuTreeVos = treeSelect();
        RoleMenuTreeDto roleMenuTreeDto = new RoleMenuTreeDto(menuTreeVos,menus);
        return ResponseResult.okResult(roleMenuTreeDto);
    }

    private List<MenuVo> builderMenuTree(List<MenuVo> menus, Long parentId) {
        List<MenuVo> list = menus.stream().filter(menu->menu.getParentId().equals(parentId))
                .map(menu->menu.setChildren(getChildren(menu,menus)))
                .collect(Collectors.toList());
        return list;
    }

    //获取存入参数的子menu集合
    private List<MenuVo> getChildren(MenuVo menu, List<MenuVo> menus) {
        List<MenuVo> list =  menus.stream()
                .filter(m->m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return list;
    }
}
