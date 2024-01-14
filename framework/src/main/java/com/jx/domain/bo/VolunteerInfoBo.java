package com.jx.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerInfoBo {
    private Long id;
    private String name;
    private String phoneNumber;
    private String qq;
}
