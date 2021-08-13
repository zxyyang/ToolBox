package com.toolbox.domain;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "users")
public class User {

	private Integer ID;

	private String UserName;

	private String Password;

	private String Salt;

	private String Label;

}
