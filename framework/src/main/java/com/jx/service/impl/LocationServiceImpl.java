package com.jx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jx.constants.SystemConstants;
import com.jx.domain.bean.ResponseResult;
import com.jx.domain.dto.AddLocationDto;
import com.jx.domain.dto.ListLocationDto;
import com.jx.domain.entity.ActivityAssignment;
import com.jx.domain.entity.Location;
import com.jx.domain.vo.ListLocationVo;
import com.jx.domain.vo.LocationVo;
import com.jx.domain.vo.PageVo;
import com.jx.enums.AppHttpCodeEnum;
import com.jx.exception.SystemException;
import com.jx.mapper.LocationMapper;
import com.jx.service.ActivityAssignmentService;
import com.jx.service.LocationService;
import com.jx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (Location)表服务实现类
 *
 * @author makejava
 * @since 2023-09-02 17:24:23
 */
@Service("locationService")
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {

    @Autowired
    private ActivityAssignmentService activityAssignmentService;
    @Override
    public ResponseResult listAllLocation() {
        LambdaQueryWrapper<Location> locationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        locationLambdaQueryWrapper.eq(Location::getStatus, SystemConstants.STATUS_NORMAL);
        locationLambdaQueryWrapper.orderByDesc(Location::getName);
        List<ListLocationVo> locationList = BeanCopyUtils.copyBeanList(list(locationLambdaQueryWrapper),ListLocationVo.class);
        return ResponseResult.okResult(locationList);
    }

    @Override
    public ResponseResult listLocations(Long pageNum, Long pageSize, ListLocationDto listLocationDto) {
        LambdaQueryWrapper<Location> locationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        locationLambdaQueryWrapper.like(StringUtils.hasText(listLocationDto.getName()),Location::getName,listLocationDto.getName());
        locationLambdaQueryWrapper.eq(StringUtils.hasText(listLocationDto.getStatus()),Location::getStatus,listLocationDto.getStatus());
        Page<Location> page = new Page<>(pageNum,pageSize);
        page(page,locationLambdaQueryWrapper);
        List<LocationVo> locationVos = BeanCopyUtils.copyBeanList(page.getRecords(),LocationVo.class);
        PageVo pageVo = new PageVo(locationVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Transactional
    @Override
    public ResponseResult addLocation(AddLocationDto addLocationDto) {
        LambdaQueryWrapper<Location> locationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询当前地点是否存在
        locationLambdaQueryWrapper.eq(Location::getName,addLocationDto.getName());
        Location location = getOne(locationLambdaQueryWrapper);
        if(!Objects.isNull(location)){
            throw new SystemException(AppHttpCodeEnum.LOCATION_EXIT);
        }
        Location newLocation = BeanCopyUtils.copyBean(addLocationDto,Location.class);
        save(newLocation);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult updateLocation(AddLocationDto addLocationDto) {
        LambdaQueryWrapper<Location> locationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询当前地点是否存在
        Location l = getById(addLocationDto.getId());
        if(!l.getName().equals(addLocationDto.getName())) {
            locationLambdaQueryWrapper.eq(Location::getName, addLocationDto.getName());
            Location location = getOne(locationLambdaQueryWrapper);
            if (!Objects.isNull(location)) {
                throw new SystemException(AppHttpCodeEnum.LOCATION_EXIT);
            }
        }
        Location newLocation = BeanCopyUtils.copyBean(addLocationDto,Location.class);
        updateById(newLocation);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLocationDetail(Long id) {
        Location location = getById(id);
        AddLocationDto locationVo = BeanCopyUtils.copyBean(location,AddLocationDto.class);
        return ResponseResult.okResult(locationVo);
    }

    @Override
    public ResponseResult deleteLocation(List<Long> locationIds) {
        List<String> ans = new ArrayList<>();
        locationIds.stream().forEach(id->{
            LambdaQueryWrapper<ActivityAssignment> activityAssignmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            activityAssignmentLambdaQueryWrapper.eq(ActivityAssignment::getLocationId,id);
            if (activityAssignmentService.count(activityAssignmentLambdaQueryWrapper) > 0) {
                Location location = getById(id);
                ans.add(location.getName());
            }
            else {
                removeById(id);
            }
        });
        if(!ans.isEmpty()){
            return ResponseResult.errorResult(550,"地点"+ans.toString()+"有活动使用,无法删除");
        }
        return  ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(AddLocationDto addLocationDto) {
        Location l = getById(addLocationDto.getId());
        l.setStatus(addLocationDto.getStatus());
        updateById(l);
        return  ResponseResult.okResult();
    }
}
