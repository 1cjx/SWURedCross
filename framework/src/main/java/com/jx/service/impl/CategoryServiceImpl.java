package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddCategoryDto;
import com.jx.domain.dto.ListCategoryDto;
import com.jx.domain.entity.Activity;
import com.jx.domain.entity.Category;
import com.jx.domain.vo.CategoryVo;
import com.jx.domain.vo.PageVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.CategoryMapper;
import com.jx.service.ActivityService;
import com.jx.service.CategoryService;
import com.jx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * (Category)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:24:00
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    ActivityService activityService;
    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        categoryLambdaQueryWrapper.orderByDesc(Category::getName);
        List<Category> categoryList = list(categoryLambdaQueryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList,CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, ListCategoryDto listCategoryDto) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(StringUtils.hasText(listCategoryDto.getName()),Category::getName,listCategoryDto.getName());
        categoryLambdaQueryWrapper.eq(StringUtils.hasText(listCategoryDto.getStatus()),Category::getStatus,listCategoryDto.getStatus());
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,categoryLambdaQueryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(),CategoryVo.class);
        PageVo pageVo = new PageVo(categoryVos,page.getTotal());
        return  ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getCategoryDetail(Long id) {
        Category category = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category,CategoryVo.class);
        return  ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(AddCategoryDto addCategoryDto) {
        Category c = getById(addCategoryDto.getId());
        if(!c.getName().equals(addCategoryDto.getName())) {
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.eq(Category::getName, addCategoryDto.getName());
            if(count(categoryLambdaQueryWrapper)>0){
                throw new SystemException(AppHttpCodeEnum.CATEGORY_EXIT);
            }
        }
        Category category = BeanCopyUtils.copyBean(addCategoryDto,Category.class);
        updateById(category);
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getName, addCategoryDto.getName());
        if(count(categoryLambdaQueryWrapper)>0){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_EXIT);
        }
        Category category = BeanCopyUtils.copyBean(addCategoryDto,Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(List<Long> categoryId) {
        List<String> ans = new ArrayList<>();
        categoryId.stream().forEach(o-> {
            LambdaQueryWrapper<Activity> activityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            activityLambdaQueryWrapper.eq(Activity::getCategoryId, o);
            if (activityService.count(activityLambdaQueryWrapper) > 0) {
                Category category = getById(o);
                ans.add(category.getName());
            }
            else {
                removeById(o);
            }
        });
        if(!ans.isEmpty()){
            return ResponseResult.errorResult(550,"分类"+ans.toString()+"有活动使用,无法删除");
        }
        return  ResponseResult.okResult();
    }
}
