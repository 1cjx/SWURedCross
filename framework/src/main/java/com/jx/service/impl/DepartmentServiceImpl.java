package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddDepartmentDto;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.dto.ListDepartmentDto;
import com.jx.domain.entity.Department;
import com.jx.domain.entity.Location;
import com.jx.domain.entity.Post;
import com.jx.domain.entity.User;
import com.jx.domain.vo.DepartmentVo;
import com.jx.domain.vo.PageVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.DepartmentMapper;
import com.jx.service.DepartmentService;
import com.jx.service.UserService;
import com.jx.utils.BeanCopyUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (Department)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:24:12
 */
@Service("departmentService")
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private UserService userService;
    @Override
    public ResponseResult listDepartment(Integer pageNum, Integer pageSize, ListDepartmentDto listDepartmentDto) {
        LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        departmentLambdaQueryWrapper.like(StringUtils.hasText(listDepartmentDto.getName()),Department::getName,listDepartmentDto.getName());
        departmentLambdaQueryWrapper.eq(StringUtils.hasText(listDepartmentDto.getStatus()),Department::getStatus,listDepartmentDto.getStatus());
        Page<Department> page = new Page<>(pageNum,pageSize);
        page(page,departmentLambdaQueryWrapper);
        List<Department> departmentList = page.getRecords();
        List<DepartmentVo> departmentVos = BeanCopyUtils.copyBeanList(departmentList,DepartmentVo.class);
        PageVo pageVo = new PageVo(departmentVos,page.getTotal());
        return  ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getDepartmentDetail(Long id) {
        Department department = getById(id);
        DepartmentVo departmentVo = BeanCopyUtils.copyBean(department,DepartmentVo.class);
        return  ResponseResult.okResult(departmentVo);
    }

    @Transactional
    @Override
    public ResponseResult updateDepartment(AddDepartmentDto addDepartmentDto) {
        Department d = getById(addDepartmentDto.getId());
        if(!d.getName().equals(addDepartmentDto.getName())) {
            LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            departmentLambdaQueryWrapper.eq(Department::getName, addDepartmentDto.getName());
            Long cnt = (long) count(departmentLambdaQueryWrapper);
            if (cnt > 0) {
                throw new SystemException(AppHttpCodeEnum.DEPARTMENT_EXIT);
            }
        }
        Department department = BeanCopyUtils.copyBean(addDepartmentDto,Department.class);
        updateById(department);
        return  ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult addDepartment(AddDepartmentDto addDepartmentDto) {
        LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        departmentLambdaQueryWrapper.eq(Department::getName, addDepartmentDto.getName());
        Long cnt = (long) count(departmentLambdaQueryWrapper);
        if (cnt > 0) {
            throw new SystemException(AppHttpCodeEnum.DEPARTMENT_EXIT);
        }
        Department department = BeanCopyUtils.copyBean(addDepartmentDto,Department.class);
        save(department);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult deleteDepartment(List<Long> departmentIds) {
        List<String> ans = new ArrayList<>();
        departmentIds.stream().forEach(id->{
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getDepartmentId,id);
            if( userService.count(userLambdaQueryWrapper)>0){
                Department department = getById(id);
                ans.add(department.getName());
            }
            else {
                removeById(id);
            }
        });
        if(ans.size()>0){
            return ResponseResult.errorResult(550,"部门"+ans.toString()+"有用户使用,无法删除");
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(AddDepartmentDto addDepartmentDto) {
        Department d = getById(addDepartmentDto.getId());
        d.setStatus(addDepartmentDto.getStatus());
        updateById(d);
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllDepartment() {
        LambdaQueryWrapper<Department> departmentLambdaQueryWrapper =new LambdaQueryWrapper<>();
        departmentLambdaQueryWrapper.eq(Department::getStatus, SystemConstants.STATUS_NORMAL);
        List<Department> departmentList = list(departmentLambdaQueryWrapper);
        return ResponseResult.okResult(departmentList);
    }
}
