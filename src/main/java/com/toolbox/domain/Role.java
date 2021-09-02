package com.toolbox.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "role")
public class Role {

	private Integer id;

	@Column(name = "role_name")
	private String roleName;

}
