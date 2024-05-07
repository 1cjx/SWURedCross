package com.jx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLocationDto {
    private Long id;
    private String name;
    //经度
    private Double longitude;
    //纬度
    private Double latitude;
    private String status;
}
