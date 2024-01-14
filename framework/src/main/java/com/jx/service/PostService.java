package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddPostDto;
import com.jx.domain.entity.Post;


/**
 * (Post)表服务接口
 *
 * @author makejava
 * @since 2023-09-02 17:24:34
 */
public interface PostService extends IService<Post> {

    ResponseResult listPosts(Long pageSize, Long pageNum, String postName, Long categoryId, String status);

    ResponseResult getPostDetail(Long id);

    ResponseResult updatePost(AddPostDto addPostDto);

    ResponseResult addPost(AddPostDto addPostDto);

    ResponseResult deletePost(Long id);

    ResponseResult changeStatus(AddPostDto addPostDto);

    ResponseResult listAllPost(Long categoryId);
}

