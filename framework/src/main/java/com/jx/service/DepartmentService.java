package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddDepartmentDto;
import com.jx.domain.dto.AddUserDto;
import com.jx.domain.dto.ListDepartmentDto;
import com.jx.domain.entity.Department;


/**
 * (Department)表服务接口
 *
 * @author makejava
 * @since 2023-09-02 17:24:12
 */
public interface DepartmentService extends IService<Department> {

    ResponseResult listDepartment(Integer pageNum, Integer pageSize, ListDepartmentDto listDepartmentDto);

    ResponseResult getDepartmentDetail(Long id);

    ResponseResult updateDepartment(AddDepartmentDto addDepartmentDto);

    ResponseResult addDepartment(AddDepartmentDto addDepartmentDto);

    ResponseResult deleteDepartment(Long id);

    ResponseResult changeStatus(AddDepartmentDto addDepartmentDto);

    ResponseResult listAllDepartment();
}

