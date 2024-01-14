package com.jx.controller;

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
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("分页查询分类")
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto){
        return categoryService.listCategory(pageNum,pageSize,listCategoryDto);
    }
    @ApiOperation("查询选中分类详情信息")
    @GetMapping("/{id}")
    public ResponseResult getCategoryDetail(@PathVariable("id") Long id){
        return categoryService.getCategoryDetail(id);
    }
    @ApiOperation("更新分类信息")
    @PutMapping
    public ResponseResult updateCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.updateCategory(addCategoryDto);
    }
    @ApiOperation("新增分类")
    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }
    @ApiOperation("删除选中分类")
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);
    }
}
