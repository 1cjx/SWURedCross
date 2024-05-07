package com.jx.controller;

import com.jx.anatation.SystemLog;
import com.jx.domain.ResponseResult;
import com.jx.domain.dto.AddLocationDto;
import com.jx.domain.dto.ListLocationDto;
import com.jx.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/location")
@Api(tags = "活动地点相关接口")
public class LocationController {
    @Autowired
    LocationService locationService;
    @SystemLog(businessName = "分页查询活动地点",type="2")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('content:location:query')")
    public ResponseResult list(Long pageNum, Long pageSize, ListLocationDto listLocationDto){
        return locationService.listLocations(pageNum,pageSize,listLocationDto);
    }
    @SystemLog(businessName = "新增活动地点",type="2")
    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:location:add')")
    public ResponseResult addLocation(@RequestBody AddLocationDto addLocationDto) {
        return locationService.addLocation(addLocationDto);
    }
    @SystemLog(businessName = "修改活动地点信息",type="2")
    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:location:edit')")
    public ResponseResult updateLocation(@RequestBody AddLocationDto addLocationDto){
        return  locationService.updateLocation(addLocationDto);
    }
    @SystemLog(businessName = "修改活动地点状态",type="2")
    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('content:location:changeStatus')")
    public ResponseResult changeStatus(@RequestBody AddLocationDto addLocationDto){
        return  locationService.changeStatus(addLocationDto);
    }
    @SystemLog(businessName = "查询选中活动地点详情信息",type="2")
    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('content:location:edit')")
    public ResponseResult getLocationDetail(@PathVariable("id") Long id){
        return locationService.getLocationDetail(id);
    }
    @SystemLog(businessName = "删除选中活动地点",type="2")
    @DeleteMapping
    @PreAuthorize("@ps.hasPermission('content:location:remove')")
    public ResponseResult deleteLocation(@RequestBody List<Long> locationIds){
        return locationService.deleteLocation(locationIds);
    }
}
