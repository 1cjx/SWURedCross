package com.jx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jx.domain.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-14 15:09:48
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(@Param("userId") Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(@Param("userId") Long userId);
}

