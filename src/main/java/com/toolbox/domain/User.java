package com.toolbox.domain;

import java.io.Serializable;

import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * Serializable
 * 继承此类为了实现rememberMe
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User implements Serializable {

	private Integer ID;

	private String UserName;

	private String Password;

	private String Salt;

	private String Label;

}
