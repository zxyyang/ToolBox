package com.toolbox.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "permissions")
public class Permissions implements Serializable {

	private Integer id;

	@Column(name = "permissions_name")
	private String permissionsName;

}
