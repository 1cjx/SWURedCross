package com.jx.controller;


import com.jx.anatation.SystemLog;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.dto.ListUserDto;
import com.jx.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @SystemLog(businessName = "分页查询用户",type="2")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:user:query')")
    public ResponseResult list(Long pageNum, Long pageSize, ListUserDto listUserDto){
        return userService.listUsers(pageNum,pageSize, listUserDto);
    }

    /**
     * 修改时根据选择用户id查询信息
     * @param id
     * @return
     */
    @SystemLog(businessName = "查询选中用户详情信息",type="2")
    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:user:edit')")
    public ResponseResult getUserDetail(@PathVariable("id") Long id){
        return userService.getUserDetail(id);
    }

    /**
     * 修改用户信息
     * @param addUserDto
     * @return
     */
    @SystemLog(businessName = "更新用户信息",type="2")
    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:user:edit')")
    public ResponseResult updateUser(@RequestBody AddUserDto addUserDto){
        return userService.updateUser(addUserDto);
    }

    /**
     * 用户批量导入模板下载
     * @param response
     * @return
     */
    @SystemLog(businessName = "用户批量导入模板下载",type="2")
    @GetMapping("/templateDownLoad")
    @PreAuthorize("@ps.hasPermission('system:user:add')")
    public ResponseResult templateDownLoad(HttpServletResponse response){
        return userService.templateDownLoad(response);
    }

    /**
     * 通过excel数据批量插入用户
     * @return
     */
    @SystemLog(businessName = "通过模板excel数据批量新增用户",type="2")
    @PostMapping("/excelAddUsers")
    @PreAuthorize("@ps.hasPermission('system:user:import')")
    public ResponseResult excelAddUsers(MultipartFile multipartFile) throws IOException {
        return userService.excelAddUsers(multipartFile);
    }

    /**
     * 插入失败数据导出为excel
     * @param recordId
     * @param response
     * @return
     */
    @SystemLog(businessName = "当前选中用户新增记录的失败数据导出为excel",type="2")
    @GetMapping("/failUploadDownload/{recordId}")
    @PreAuthorize("@ps.hasPermission('system:user:export')")
    public ResponseResult failUploadDownload(@PathVariable("recordId") Long recordId,HttpServletResponse response){
        return userService.failUploadDownload(recordId,response);
    }
    @SystemLog(businessName = "查询当前用户的新增用户记录",type="2")
    @GetMapping("/userImportRecordList")
    @PreAuthorize("@ps.hasPermission('system:user:queryImport')")
    public ResponseResult userImportRecordList(Long pageNum, Long pageSize){
        return userService.userImportRecordList(pageNum,pageSize);
    }


    @DeleteMapping
    @SystemLog(businessName = "删除选中用户",type="2")
    @PreAuthorize("@ps.hasPermission('system:user:remove')")
    public ResponseResult deleteUsers(@RequestBody List<Long> userIds){
        return userService.deleteUsers(userIds);
    }
}
