package com.toolbox.vo;

import java.util.List;

import lombok.Data;

@Data
public class UserVO {

	private Integer id;

	private String userName;

	private String password;

	private String salt;

	private String label;

	private Boolean rememberMe;

	private List<RoleVO> roleVOList;
}
