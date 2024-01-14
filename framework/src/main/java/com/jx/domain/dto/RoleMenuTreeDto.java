package com.jx.domain.dto;

import com.jx.domain.vo.MenuTreeVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeDto {
    private List<MenuTreeVo> menus;
    private List<Long>checkedKeys;
}
