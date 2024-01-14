package com.jx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddLocationDto;
import com.jx.domain.dto.ListLocationDto;
import com.jx.domain.entity.Location;


/**
 * (Location)表服务接口
 *
 * @author makejava
 * @since 2023-09-02 17:24:23
 */
public interface LocationService extends IService<Location> {

    ResponseResult listAllLocation();

    ResponseResult listLocations(Long pageNum, Long pageSize, ListLocationDto listLocationDto);

    ResponseResult addLocation(AddLocationDto addLocationDto);

    ResponseResult updateLocation(AddLocationDto addLocationDto);

    ResponseResult getLocationDetail(Long id);

    ResponseResult deleteLocation(Long id);

    ResponseResult changeStatus(AddLocationDto addLocationDto);

}

