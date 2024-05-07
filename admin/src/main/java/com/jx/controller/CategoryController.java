package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddCategoryDto;
import com.jx.domain.dto.AddDepartmentDto;
import com.jx.domain.dto.ListCategoryDto;
import com.jx.domain.dto.ListDepartmentDto;
import com.jx.service.CategoryService;
import com.jx.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "活动分类相关接口")
@RestController
@RequestMapping("/system/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询分类信息
     * @param pageNum
     * @param pageSize
     * @param listCategoryDto
     * @return
     */
    @GetMapping("/list")
    @SystemLog(businessName = "分页查询分类",type="2")
    @PreAuthorize("@ps.hasPermission('content:category:query')")
    public ResponseResult list(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto){
        return categoryService.listCategory(pageNum,pageSize,listCategoryDto);
    }
    @GetMapping("/{id}")
    @SystemLog(businessName = "查询选中分类详情信息",type="2")
    @PreAuthorize("@ps.hasPermission('content:category:edit')")
    public ResponseResult getCategoryDetail(@PathVariable("id") Long id){
        return categoryService.getCategoryDetail(id);
    }
    @PutMapping
    @SystemLog(businessName = "更新分类信息",type="2")
    @PreAuthorize("@ps.hasPermission('content:category:edit')")
    public ResponseResult updateCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.updateCategory(addCategoryDto);
    }
    @PostMapping
    @SystemLog(businessName = "新增分类",type="2")
    @PreAuthorize("@ps.hasPermission('content:category:add')")
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }
    @DeleteMapping
    @SystemLog(businessName = "删除选中分类",type="2")
    @PreAuthorize("@ps.hasPermission('content:category:remove')")
    public ResponseResult deleteCategory(@RequestBody List<Long> categoryId){
        return categoryService.deleteCategory(categoryId);
    }
}
