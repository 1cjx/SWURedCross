package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.ListMenuDto;
import com.jx.domain.entity.Menu;
import com.jx.domain.vo.MenuTreeVo;
import com.jx.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-10-14 15:09:49
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserID(Long userId);

    ResponseResult listMenus(ListMenuDto listMenuDto);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long id);

    List<MenuTreeVo> treeSelect();

    ResponseResult roleMenuTreeSelect(Long id);
}

