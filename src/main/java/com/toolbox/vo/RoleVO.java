package com.toolbox.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleVO {
    private Integer id;
    private String roleName;
    private List<PermissionsVO> permissionsVOLis;
}
