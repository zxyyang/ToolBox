package com.toolbox.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVO {
    private Integer ID;

    private String UserName;

    private String Password;

    private String Salt;

    private String Label;

    private List<RoleVO> roleVOList;
}
