package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.ListMenuDto;
import com.jx.domain.entity.Menu;
import com.jx.domain.vo.MenuTreeVo;
import com.jx.domain.vo.MenuVo;
import com.jx.service.MenuService;
import com.jx.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
@Api(tags = "菜单相关接口")
public class MenuController {
    @Autowired
    MenuService menuService;
    @ApiOperation("分页查询菜单信息")
    @GetMapping("/list")
    public ResponseResult list(ListMenuDto listMenuDto){
        return menuService.listMenus(listMenuDto);
    }
    @ApiOperation("新增菜单")
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }
    @ApiOperation("查询选中菜单信息")
    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable("id") Long id){
        Menu menu = menuService.getById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu,MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }
    @ApiOperation("更新菜单")
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){

        return  menuService.updateMenu(menu);
    }
    @ApiOperation("删除选中菜单")
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable("id") Long id){
        return menuService.deleteMenu(id);
    }
    @ApiOperation("查询菜单树结构")
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        List<MenuTreeVo> list = menuService.treeSelect();
        return ResponseResult.okResult(list);
    }
    @ApiOperation("查询选中角色关联树")
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id")Long id){
        return menuService.roleMenuTreeSelect(id);
    }
}
