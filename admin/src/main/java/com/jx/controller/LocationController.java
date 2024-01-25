package com.jx.controller;

import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddLocationDto;
import com.jx.domain.dto.ListLocationDto;
import com.jx.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/location")
@Api(tags = "活动地点相关接口")
public class LocationController {
    @Autowired
    LocationService locationService;
    @ApiOperation("分页查询活动地点")
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, ListLocationDto listLocationDto){
        return locationService.listLocations(pageNum,pageSize,listLocationDto);
    }
    @ApiOperation("新增活动地点")
    @PostMapping
    public ResponseResult addLocation(@RequestBody AddLocationDto addLocationDto) {
        return locationService.addLocation(addLocationDto);
    }
    @ApiOperation("修改活动地点信息")
    @PutMapping
    public ResponseResult updateLocation(@RequestBody AddLocationDto addLocationDto){
        return  locationService.updateLocation(addLocationDto);
    }
    @ApiOperation("修改活动地点状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody AddLocationDto addLocationDto){
        return  locationService.changeStatus(addLocationDto);
    }
    @ApiOperation("查询选中活动地点详情信息")
    @GetMapping("/{id}")
    public ResponseResult getLocationDetail(@PathVariable("id") Long id){
        return locationService.getLocationDetail(id);
    }
    @ApiOperation("删除选中活动地点")
    @DeleteMapping
    public ResponseResult deleteLocation(@RequestBody List<Long> locationIds){
        return locationService.deleteLocation(locationIds);
    }
}
