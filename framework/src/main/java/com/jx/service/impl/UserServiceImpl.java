package com.jx.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.dto.ListUserDto;
import com.jx.domain.entity.*;
import com.jx.domain.vo.*;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.listener.MyListener;
import com.jx.mapper.UserImportDetailMapper;
import com.jx.mapper.UserImportRecordMapper;
import com.jx.mapper.UserMapper;
import com.jx.mapper.VolunteerRecordMapper;
import com.jx.service.*;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.PageUtils;
import com.jx.utils.SecurityUtils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:25:35
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserImportRecordMapper userImportRecordMapper;
    @Autowired
    UserImportRecordService userImportRecordService;
    @Autowired
    UserImportDetailService userImportDetailService;
    @Autowired
    VolunteerRecordService volunteerRecordService;
    @Autowired
    VolunteerRecordMapper volunteerRecordMapper;
    @Autowired
    DepartmentService departmentService;
    @Override
    public ResponseResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();
        UserInfoVo userInfoVo = userMapper.getUserInfo(userId);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult getUserActivityNumsAndVolunteerTimes() {
        Long userId = SecurityUtils.getUserId();
        //获取参加活动数目
        LambdaQueryWrapper<VolunteerRecord> volunteerRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        volunteerRecordLambdaQueryWrapper.eq(VolunteerRecord::getUserId,userId);
        Long count = (long)volunteerRecordService.count(volunteerRecordLambdaQueryWrapper);
        VolunteerVo volunteerVo = new VolunteerVo();
        volunteerVo.setTotalActivityNums(count);
        //获取志愿时长
        Double userVolunteerTimes =volunteerRecordMapper.getUserTotalVolunteerTimes(userId);
        if(Objects.isNull(userVolunteerTimes)) {
            volunteerVo.setTotalVolunteerTime(0.0);
        }
        else{
            volunteerVo.setTotalVolunteerTime(userVolunteerTimes);

        }
        return ResponseResult.okResult(volunteerVo);
    }

    @Override
    public ResponseResult listUsers(Long pageNum, Long pageSize, ListUserDto listUserDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(listUserDto.getName()),User::getName, listUserDto.getName());
        wrapper.like(StringUtils.hasText(listUserDto.getQq()),User::getQq, listUserDto.getQq());
        wrapper.like(StringUtils.hasText(listUserDto.getPhoneNumber()),User::getPhoneNumber, listUserDto.getPhoneNumber());
        wrapper.eq(StringUtils.hasText(listUserDto.getStatus()),User::getStatus, listUserDto.getStatus());
        wrapper.like(!Objects.isNull(listUserDto.getDepartmentId()),User::getDepartmentId, listUserDto.getDepartmentId());
        wrapper.like(!Objects.isNull(listUserDto.getCollegeId()),User::getCollegeId, listUserDto.getCollegeId());
        wrapper.like(!Objects.isNull(listUserDto.getTitleId()),User::getTitleId, listUserDto.getTitleId());
        wrapper.eq(StringUtils.hasText(listUserDto.getIsBind()),User::getIsBind, listUserDto.getIsBind());
        wrapper.like(!Objects.isNull(listUserDto.getId()),User::getId, listUserDto.getId());
        wrapper.orderByAsc(User::getTitleId);
        wrapper.orderByAsc(User::getDepartmentId);
        List<User> userList = list(wrapper);
        List<UserInfoVo> userInfoVos = userList.stream().map(o->userMapper.getUserInfo(o.getId()).setIsBind(o.getIsBind())).collect(Collectors.toList());
        Page<UserInfoVo> userInfoVoPage = PageUtils.listToPage(userInfoVos,pageNum,pageSize);
        PageVo pageVo = new PageVo(userInfoVoPage.getRecords(),userInfoVoPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getUserDetail(Long userId) {
        ListUserVo listUserVo = BeanCopyUtils.copyBean(getById(userId),ListUserVo.class);
        return ResponseResult.okResult(listUserVo);
    }

    @Override
    @Transactional
    public ResponseResult updateUser(AddUserDto addUserDto) {
        //更新user表数据
        User user = BeanCopyUtils.copyBean(addUserDto,User.class);
        //小程序修改信息
        if(Objects.isNull(addUserDto.getId())) {
            Long userId = SecurityUtils.getUserId();
            user.setId(userId);
        }
        String password = user.getPassword();
        if(StringUtils.hasText(password)){
            //重置密码
            user.setPassword(passwordEncoder.encode("redcross666"));
        }
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult templateDownLoad(HttpServletResponse response) {
        ClassPathResource pathResource = new ClassPathResource("/template/用户数据批量导入模板.xlsx");
        try {
            InputStream inputStream = pathResource.getInputStream();
            //输出的文件名称，格式
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("用户数据批量导入模板","UTF-8");
            response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");
            //输出文件
            ExcelWriter writer = EasyExcel.write(response.getOutputStream()).withTemplate(inputStream).build();
            writer.finish();
            return ResponseResult.okResult();
        } catch (IOException e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TEMPLATE_DOWNLOAD_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseResult excelAddUsers(MultipartFile multipartFile) throws IOException {
        UserImportRecord userImportRecord = new UserImportRecord();
        userImportRecordMapper.insert(userImportRecord);
        Long recordId = userImportRecord.getId();
        MyListener lister = new MyListener(recordId);
        EasyExcel.read(multipartFile.getInputStream(),AddUserDto.class,lister).sheet().doRead();
        userImportRecord.setAllNum(lister.getAllNum());
        userImportRecord.setFailNum(lister.getFailNum());
        userImportRecord.setSucceedNum(lister.getSuccessNum());
        userImportRecordMapper.updateById(userImportRecord);
        String ans = "";
        if(lister.getFailNum()>0){
            ans = "共有"+lister.getFailNum()+"条数据导入失败,详情查看导入记录";
            return ResponseResult.errorResult(550,ans);
        }
        else{
            ans = "成功导入";
            return ResponseResult.okResult(ans);
        }
    }

    @Override
    public ResponseResult failUploadDownload(Long recordId, HttpServletResponse response) {
        LambdaQueryWrapper<UserImportDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserImportDetail::getRecordId,recordId);
        lambdaQueryWrapper.eq(UserImportDetail::getStatus,SystemConstants.IMPORT_USER_ERROR);
        List<UserImportDetail> list = userImportDetailService.list(lambdaQueryWrapper);
        try {
            ClassPathResource resource = new ClassPathResource("/template/导入失败模板.xlsx");
            String filename  = URLEncoder.encode("导入失败数据" + System.currentTimeMillis(),"UTF-8");
            response.setHeader("Content-disposition","attachment;filename="+filename+".xlsx");
            ExcelWriter writer = EasyExcel.write(response.getOutputStream()).withTemplate(resource.getInputStream()).build();
            writer.fill(list,EasyExcel.writerSheet(0).build());
            writer.finish();
            return ResponseResult.okResult();
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.TEMPLATE_DOWNLOAD_ERROR);
        }
    }

    @Override
    public ResponseResult userImportRecordList(Long pageNum, Long pageSize) {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<UserImportRecord> userImportRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userImportRecordLambdaQueryWrapper.eq(UserImportRecord::getCreateBy,userId).orderByDesc(UserImportRecord::getCreateTime);
        List<UserImportRecord> userImportRecords = userImportRecordService.list(userImportRecordLambdaQueryWrapper);
        Page<UserImportRecord> importRecordPage = PageUtils.listToPage(userImportRecords,pageNum,pageSize);
        PageVo pageVo = new PageVo(importRecordPage.getRecords(),importRecordPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteUsers(List<Long> userIds) {
        removeByIds(userIds);
        return ResponseResult.okResult();
    }
}
