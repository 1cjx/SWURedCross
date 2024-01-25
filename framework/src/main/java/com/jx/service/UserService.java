package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.dto.ListUserDto;
import com.jx.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-09-02 17:25:35
 */
public interface UserService extends IService<User> {

    ResponseResult getUserInfo();

    ResponseResult getUserActivityNumsAndVolunteerTimes();

    ResponseResult listUsers(Long pageNum, Long pageSize, ListUserDto listUserDto);

    ResponseResult getUserDetail(Long userId);

    ResponseResult updateUser(AddUserDto addUserDto);

    ResponseResult templateDownLoad(HttpServletResponse response);

    ResponseResult excelAddUsers(MultipartFile multipartFile) throws IOException;

    ResponseResult failUploadDownload(Long recordId, HttpServletResponse response);

    ResponseResult userImportRecordList(Long pageNum, Long pageSize);

    ResponseResult deleteUsers(List<Long> userIds);
}

