package com.toolbox.domain;

import javax.persistence.Table;

import lombok.Data;

/**
 * @Version 1.0
 * @Author zixuan.yang
 * @Created 2021/8/13 17:35
 * @Description
 *              <p>
 * @Modification
 *               <p>
 *               Date Author Version Description
 *               <p>
 *               2021/8/13 zixuan.yang@hirain.com 1.0 create file
 */
@Data
@Table(name = "users")
public class User {

	private Integer ID;

	private String UserName;

	private String Password;

	private String Salt;

	private String Label;

}
