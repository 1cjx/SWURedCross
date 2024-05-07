package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddPostDto;
import com.jx.domain.entity.*;
import com.jx.domain.vo.PageVo;
import com.jx.domain.vo.PostVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.PostAssignmentMapper;
import com.jx.mapper.PostMapper;
import com.jx.service.ActivityAssignmentService;
import com.jx.service.CategoryService;
import com.jx.service.PostAssignmentService;
import com.jx.service.PostService;
import com.jx.utils.BeanCopyUtils;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (Post)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:24:34
 */
@Service("postService")
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostAssignmentService postAssignmentService;
    @Override
    public ResponseResult listPosts(Long pageSize, Long pageNum, String postName, Long categoryId, String status) {
        LambdaQueryWrapper<Post> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(postName),Post::getName,postName);
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Post::getStatus,status);
        lambdaQueryWrapper.eq(!Objects.isNull(categoryId),Post::getCategoryId,categoryId).or().eq(!Objects.isNull(categoryId),Post::getCategoryId,0L);
        Page<Post> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<PostVo> postVos = BeanCopyUtils.copyBeanList(page.getRecords(),PostVo.class);
        postVos.stream().forEach(o->{
            String categoryName = null;
            if(o.getCategoryId()==0L){
                categoryName = "活动通用岗位";
            }
            else{
                categoryName = categoryService.getById(o.getCategoryId()).getName();
            }
            o.setCategoryName(categoryName);
        });
        //查询岗位所属名称;
        PageVo pageVo = new PageVo(postVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getPostDetail(Long id) {
        LambdaQueryWrapper<Post> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Post::getId,id);
        Post post = getOne(lambdaQueryWrapper);
        PostVo  postVo = BeanCopyUtils.copyBean(post,PostVo.class);
        return ResponseResult.okResult(postVo);
    }

    @Transactional
    @Override
    public ResponseResult updatePost(AddPostDto addPostDto) {
        //判断分类是否存在
        if (Objects.isNull(addPostDto.getCategoryId())||addPostDto.getCategoryId()==0L){
            addPostDto.setCategoryId(0L);
        }
        else {
            LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Category::getId, addPostDto.getCategoryId());
            Category category = categoryService.getOne(lambdaQueryWrapper);
            if (Objects.isNull(category)) {
                throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EXIT);
            }
        }
        //判断重名
        Post p = getById(addPostDto.getId());
        if(!p.getName().equals(addPostDto.getName())) {
            LambdaQueryWrapper<Post> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Post::getName, addPostDto.getName());
            int cnt = count(lambdaQueryWrapper);
            if (cnt > 0) {
                throw new SystemException(AppHttpCodeEnum.POST_EXIT);
            }
        }
        Post post = BeanCopyUtils.copyBean(addPostDto,Post.class);
        updateById(post);
        return ResponseResult.okResult();
    }
    @Transactional
    @Override
    public ResponseResult addPost(AddPostDto addPostDto) {
        if(!StringUtils.hasText(addPostDto.getName())){
            throw new SystemException(AppHttpCodeEnum.POST_NAME_EMPTY);
        }
        if(Objects.isNull(addPostDto.getCategoryId())){
            throw new SystemException(AppHttpCodeEnum.CATEGORY_EMPTY);
        }
        LambdaQueryWrapper<Post>lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Post::getName,addPostDto.getName());
        int cnt = count(lambdaQueryWrapper);
        if(cnt>0){
            throw new SystemException(AppHttpCodeEnum.POST_EXIT);
        }
        if (Objects.isNull(addPostDto.getCategoryId())){
            addPostDto.setCategoryId(0L);
        }
        else{
           Category category  = categoryService.getById(addPostDto.getCategoryId());
           if(Objects.isNull(category)){
               throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_EXIT);
           }
        }
        Post post = BeanCopyUtils.copyBean(addPostDto,Post.class);
        save(post);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult deletePost(List<Long> postIds) {
        List<String> ans = new ArrayList<>();
        postIds.stream().forEach(id->{
            LambdaQueryWrapper<PostAssignment> postAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postAssignmentLambdaQueryWrapper.eq(PostAssignment::getPostId,id);
            if(postAssignmentService.count(postAssignmentLambdaQueryWrapper)>0){
                Post post = getById(id);
                ans.add(post.getName());
            }
            else {
                removeById(id);
            }
        });
        if(!ans.isEmpty()){
            return ResponseResult.errorResult(550,"岗位"+ans.toString()+"有活动使用,无法删除");
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(AddPostDto addPostDto) {
        Post p = getById(addPostDto.getId());
        p.setStatus(addPostDto.getStatus());
        updateById(p);
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllPost(Long categoryId) {
        LambdaQueryWrapper<Post> postLambdaQueryWrapper =new LambdaQueryWrapper<>();
        postLambdaQueryWrapper.eq(Post::getStatus, SystemConstants.STATUS_NORMAL);
        postLambdaQueryWrapper.eq(Post::getCategoryId,categoryId).or().eq(Post::getCategoryId,0L);
        List<PostVo> postList = BeanCopyUtils.copyBeanList(list(postLambdaQueryWrapper),PostVo.class);
        return ResponseResult.okResult(postList);
    }
}
