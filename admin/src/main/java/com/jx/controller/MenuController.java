package com.jx.controller;

import com.jx.anatation.SystemLog;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
@Api(tags = "菜单相关接口")
public class MenuController {
    @Autowired
    MenuService menuService;
    @SystemLog(businessName = "分页查询菜单信息",type="2")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    public ResponseResult list(ListMenuDto listMenuDto){
        return menuService.listMenus(listMenuDto);
    }
    @SystemLog(businessName = "新增菜单",type="2")
    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:menu:add')")
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }
    @SystemLog(businessName = "查询选中菜单信息",type="2")
    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:menu:edit')")
    public ResponseResult getMenu(@PathVariable("id") Long id){
        Menu menu = menuService.getById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu,MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }
    @SystemLog(businessName = "更新菜单",type="2")
    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:menu:edit')")
    public ResponseResult updateMenu(@RequestBody Menu menu){

        return  menuService.updateMenu(menu);
    }
    @SystemLog(businessName = "删除选中菜单",type="2")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:menu:remove')")
    public ResponseResult delete(@PathVariable("id") Long id){
        return menuService.deleteMenu(id);
    }
    @SystemLog(businessName = "查询菜单树结构",type="2")
    @GetMapping("/treeselect")
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    public ResponseResult treeSelect(){
        List<MenuTreeVo> list = menuService.treeSelect();
        return ResponseResult.okResult(list);
    }
    @SystemLog(businessName = "查询选中角色关联树",type="2")
    @GetMapping("/roleMenuTreeselect/{id}")
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id")Long id){
        return menuService.roleMenuTreeSelect(id);
    }
}
