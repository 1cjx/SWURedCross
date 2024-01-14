package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddCategoryDto;
import com.jx.domain.dto.AddDepartmentDto;
import com.jx.domain.dto.ListCategoryDto;
import com.jx.domain.entity.Category;


/**
 * (Category)表服务接口
 *
 * @author makejava
 * @since 2023-09-02 17:24:00
 */
public interface CategoryService extends IService<Category> {

    ResponseResult listAllCategory();

    ResponseResult listCategory(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto);

    ResponseResult getCategoryDetail(Long id);

    ResponseResult updateCategory(AddCategoryDto addCategoryDto);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult deleteCategory(Long id);

}

