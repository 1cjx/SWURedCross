package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddLocationDto;
import com.jx.domain.dto.AddPostDto;
import com.jx.service.PostService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/post")
@Api(tags = "岗位相关接口")
public class PostController {
    @Autowired
    PostService postService;
    @GetMapping("/list")
    @SystemLog(businessName = "分页查询岗位信息",type="2")
    @PreAuthorize("@ps.hasPermission('content:post:query')")
    public ResponseResult list(Long pageSize,Long pageNum,String postName,Long categoryId,String status){
        return postService.listPosts(pageSize,pageNum,postName,categoryId,status);
    }
    @GetMapping("/{id}")
    @SystemLog(businessName = "获取选择岗位详情信息",type="2")
    @PreAuthorize("@ps.hasPermission('content:post:edit')")
    public ResponseResult getPostDetail(@PathVariable("id") Long id){
        return postService.getPostDetail(id);
    }
    @PutMapping
    @SystemLog(businessName = "修改岗位信息",type="2")
    @PreAuthorize("@ps.hasPermission('content:post:edit')")
    public ResponseResult updatePost(@RequestBody AddPostDto addPostDto){
        return postService.updatePost(addPostDto);
    }
    @PutMapping("/changeStatus")
    @SystemLog(businessName = "修改岗位状态",type="2")
    @PreAuthorize("@ps.hasPermission('content:post:changeStatus')")
    public ResponseResult changeStatus(@RequestBody AddPostDto addPostDto){
        return  postService.changeStatus(addPostDto);
    }
    @PostMapping
    @SystemLog(businessName = "新增岗位",type="2")
    @PreAuthorize("@ps.hasPermission('content:post:add')")
    public ResponseResult addPost(@RequestBody AddPostDto addPostDto){
        return postService.addPost(addPostDto);
    }
    @DeleteMapping
    @SystemLog(businessName = "删除选中岗位",type="2")
    @PreAuthorize("@ps.hasPermission('content:post:remove')")
    public ResponseResult deletePost(@RequestBody List<Long> postIds){
        return postService.deletePost(postIds);
    }
}
