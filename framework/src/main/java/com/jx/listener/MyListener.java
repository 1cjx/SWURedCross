package com.jx.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jx.constants.SystemConstants;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.entity.*;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.CollegeMapper;
import com.jx.mapper.UserImportRecordMapper;
import com.jx.service.*;
import com.jx.service.impl.UserServiceImpl;
import com.jx.utils.BeanCopyUtils;
import com.jx.utils.CheckUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

// 继承AnalysisEventListener，括号里放表格对应实体类
@Component
public class MyListener extends AnalysisEventListener<AddUserDto> {
    private static UserService userService;
    private static PasswordEncoder passwordEncoder;
    private static DepartmentService departmentService;
    private static UserImportDetailService userImportDetailService;
    private static CollegeMapper collegeMapper;
    private static RoleService roleService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @Autowired
    public void setUserImportDetailService(UserImportDetailService userImportDetailService) {
        this.userImportDetailService = userImportDetailService;
    }

    @Autowired
    public void setCollegeMapper(CollegeMapper collegeMapper) {
        this.collegeMapper = collegeMapper;
    }
    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
    private Long failNum =0L;
    private Long successNum = 0L;
    private Long allNum = 0L;
    private Long recordId;
    private Boolean hasEffectiveData = false;
    public MyListener(Long recordId) {
        this.recordId =recordId;
    }
    public MyListener(){
    }
    public Long getFailNum(){
        return failNum;
    }
    public Long getSuccessNum(){
        return successNum;
    }
    public Long getAllNum(){
        return allNum;
    }
   /**
     * 重写invokeHeadMap方法，校验表头
     *headMap的key是表头下标，value是内容
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    	// headMap.containsKey(下标)，先判空
    	// headMap.get(0).equals() 判内容是否相符
        try{
            if(!headMap.containsKey(0) || !headMap.containsKey(1) || !headMap.containsKey(2)
                || !headMap.containsKey(3) ||!headMap.containsKey(4)||!headMap.containsKey(5)
                ||!headMap.containsKey(6) ||!headMap.containsKey(7)
                || !headMap.get(0).equals("部门")  || !headMap.get(1).equals("职称")
                || !headMap.get(2).equals("学号") || !headMap.get(3).equals("姓名")
                || !headMap.get(4).equals("性别") ||!headMap.get(5).equals("学院")|| !headMap.get(6).equals("电话号码")
                || !headMap.get(7).equals("qq")  || !headMap.get(8).equals("电子邮箱")
               );
        }
        catch(Exception e) {
            throw new SystemException(AppHttpCodeEnum.NOT_USE_TEMPLATE);
        }
    }

    @Override
    public void invoke(AddUserDto addUserDto, AnalysisContext analysisContext) {
        if(CheckUtils.checkObjAllFieldsIsNull(addUserDto)){
            return;
        }
    	// 这里放具体数据校验方法，校验通过往data里面放数据，否则直接return
        UserImportDetail detail = BeanCopyUtils.copyBean(addUserDto,UserImportDetail.class);
        detail.setUserId(addUserDto.getId());
        detail.setRecordId(recordId);
        UserImportFail fail = new UserImportFail();
        allNum++;
        if (checkImportData(addUserDto,fail)) {
            successNum++;
            hasEffectiveData = true;
            detail.setStatus(SystemConstants.IMPORT_USER_SUCCESS);
            userImportDetailService.save(detail);
            //插入用户
            User user = BeanCopyUtils.copyBean(addUserDto,User.class);
            user.setId(addUserDto.getId());
            user.setPassword(passwordEncoder.encode("redcross666"));
            //判断用户是否存在 存在则修改信息
            User tempUser = userService.getById(user.getId());
            if(Objects.isNull(tempUser)) {
                userService.save(user);
            }
            else{
                userService.updateById(user);
            }
        }
        else{
            detail.setReason(fail.getReason());
            detail.setStatus(SystemConstants.IMPORT_USER_ERROR);
            failNum++;
            userImportDetailService.save(detail);
        }
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
//        if (!hasEffectiveData) {
//            throw new SystemException(AppHttpCodeEnum.NO_IMPORT_DATA);
//        }
    }
    //效验数据，根据需求自己更改
    private boolean checkImportData(AddUserDto addUserDto, UserImportFail fail) {
        if(!StringUtils.hasText(addUserDto.getDepartmentName())){
            fail.setReason("部门不能为空");
            return false;
        }
        else{
            LambdaQueryWrapper<Department>departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            departmentLambdaQueryWrapper.eq(Department::getName,addUserDto.getDepartmentName());
            Department department = departmentService.getOne(departmentLambdaQueryWrapper);
            if(Objects.isNull(department)){
                fail.setReason("该部门不存在");
                return false;
            }
            else{
                addUserDto.setDepartmentId(department.getId());
            }
        }
        if(!StringUtils.hasText(addUserDto.getRoleName())){
            fail.setReason("职称不能为空");
            return false;
        }
        else{
            LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleLambdaQueryWrapper.eq(Role::getRoleName,addUserDto.getRoleName());
            Role role = roleService.getOne(roleLambdaQueryWrapper);
            if(Objects.isNull(role)){
                fail.setReason("该职称不存在");
                return false;
            }
            else{
                addUserDto.setRoleId(role.getId());
            }
        }
        if(Objects.isNull(addUserDto.getId())){
            fail.setReason("学号不能为空");
            return false;
        }
        else if(addUserDto.getId().toString().length()!=15){
            fail.setReason("学号长度有误");
            return false;
        }
        else{
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getId,addUserDto.getId());
            int cnt = userService.count(lambdaQueryWrapper);
            if (cnt > 0){
                fail.setReason("该学号已注册");
                return false;
            }
        }
        if(!StringUtils.hasText(addUserDto.getName())){
            fail.setReason("姓名不能为空");
            return false;
        }
        if(!StringUtils.hasText(addUserDto.getSex())){
            fail.setReason("性别不能为空");
            return false;
        }
        else{
            if(!addUserDto.getSex().equals("男")&&!addUserDto.getSex().equals("女")) {
                fail.setReason("性别有问题");
                return  false;
            }
            String sex = addUserDto.getSex().equals("男")?"1":"2";
            addUserDto.setSex(sex);
        }
        if(!StringUtils.hasText(addUserDto.getPhoneNumber())){
            fail.setReason("手机号不能为空");
            return false;
        }
        if(!CheckUtils.isValidPhoneNumber(addUserDto.getPhoneNumber())){
            fail.setReason("手机号格式不正确");
            return false;
        }
        if(!StringUtils.hasText(addUserDto.getQq())){
            fail.setReason("qq不能为空");
            return false;
        }
        if(!StringUtils.hasText(addUserDto.getEmail())){
            fail.setReason("邮箱不能为空");
            return false;
        }
        if(!CheckUtils.checkEmail(addUserDto.getEmail())){
            fail.setReason("邮件格式不正确");
            return false;
        }

        if(!StringUtils.hasText(addUserDto.getCollegeName())){
            fail.setReason("学院不能为空");
            return false;
        }
        else{
            Long collegeId = collegeMapper.selectByName(addUserDto.getCollegeName());
            if(Objects.isNull(collegeId)){
                fail.setReason("学院信息有误");
                return false;
            }
            addUserDto.setCollegeId(collegeId);
        }
        return true;
    }
}
