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
@Table(name = "role")
public class Role implements Serializable {

	private Integer id;

	@Column(name = "role_name")
	private String roleName;

}
