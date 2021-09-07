package com.toolbox.vo;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserVO implements Serializable {

	private Integer id;

	private String userName;

	private String password;

	private String salt;

	private String label;

	private Boolean rememberMe;

	private List<RoleVO> roleVOList;
}
