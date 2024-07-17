package com.jx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="查询活动用于接收传入参数的DTO")
public class ListActivityDto {
    @ApiModelProperty(value="活动名",name="activityName",example="无偿献血")
    private String name;
    @ApiModelProperty(value="活动分类Id",name="categoryId",example="1")
    private Long categoryId;
    @ApiModelProperty(value="活动状态",name="status",example="1")
    private String status;
    @ApiModelProperty(value="活动地点Id",name="locationId",example="1")
    private Long locationId;

}
