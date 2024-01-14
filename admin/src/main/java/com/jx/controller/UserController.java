package com.jx.controller;


import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.dto.ListUserDto;
import com.jx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RequestMapping("/system/user")
@RestController
@Api(tags = "用户管理相关接口")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 分页根据传入信息获取用户列表，默认查询所有用户
     * @param pageNum
     * @param pageSize
     * @param listUserDto
     * @return
     */
    @ApiOperation("分页查询用户")
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, ListUserDto listUserDto){
        return userService.listUsers(pageNum,pageSize, listUserDto);
    }

    /**
     * 修改时根据选择用户id查询信息
     * @param id
     * @return
     */
    @ApiOperation("查询选中用户详情信息")
    @GetMapping("/{id}")
    public ResponseResult getUserDetail(@PathVariable("id") Long id){
        return userService.getUserDetail(id);
    }

    /**
     * 修改用户信息
     * @param addUserDto
     * @return
     */
    @ApiOperation("更新用户信息")
    @PutMapping
    public ResponseResult updateUser(@RequestBody AddUserDto addUserDto){
        return userService.updateUser(addUserDto);
    }

    /**
     * 用户批量导入模板下载
     * @param response
     * @return
     */
    @ApiOperation("用户批量导入模板下载")
    @GetMapping("/templateDownLoad")
    public ResponseResult templateDownLoad(HttpServletResponse response){
        return userService.templateDownLoad(response);
    }

    /**
     * 通过excel数据批量插入用户
     * @return
     */
    @ApiOperation("通过模板excel数据批量新增用户")
    @PostMapping("/excelAddUsers")
    public ResponseResult excelAddUsers(MultipartFile multipartFile) throws IOException {
        return userService.excelAddUsers(multipartFile);
    }

    /**
     * 插入失败数据导出为excel
     * @param recordId
     * @param response
     * @return
     */
    @ApiOperation("当前选中用户新增记录的失败数据导出为excel")
    @GetMapping("/failUploadDownload/{recordId}")
    public ResponseResult failUploadDownload(@PathVariable("recordId") Long recordId,HttpServletResponse response){
        return userService.failUploadDownload(recordId,response);
    }
    @ApiOperation("查询当前用户的新增用户记录")
    @GetMapping("/userImportRecordList")
    public ResponseResult userImportRecordList(){
        return userService.userImportRecordList();
    }
}
