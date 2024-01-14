package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddLocationDto;
import com.jx.domain.dto.AddPostDto;
import com.jx.service.PostService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/post")
@Api(tags = "岗位相关接口")
public class PostController {
    @Autowired
    PostService postService;
    @GetMapping("/list")
    public ResponseResult list(Long pageSize,Long pageNum,String postName,Long categoryId,String status){
        return postService.listPosts(pageSize,pageNum,postName,categoryId,status);
    }
    @GetMapping("/{id}")
    public ResponseResult getPostDetail(@PathVariable("id") Long id){
        return postService.getPostDetail(id);
    }
    @PutMapping
    public ResponseResult updatePost(@RequestBody AddPostDto addPostDto){
        return postService.updatePost(addPostDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody AddPostDto addPostDto){
        return  postService.changeStatus(addPostDto);
    }
    @PostMapping
    public ResponseResult addPost(@RequestBody AddPostDto addPostDto){
        return postService.addPost(addPostDto);
    }
    @DeleteMapping("{id}")
    public ResponseResult deletePost(@PathVariable("id") Long id){
        return postService.deletePost(id);
    }
}
