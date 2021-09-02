package com.toolbox.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "permissions")
public class Permissions {

	private Integer id;

	@Column(name = "permissions_name")
	private String permissionsName;

}
