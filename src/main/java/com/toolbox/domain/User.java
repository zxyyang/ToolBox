package com.toolbox.domain;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/***
 * Serializable
 * 继承此类为了实现rememberMe
 */
@Data
@Table(name = "users")
public class User implements Serializable {

    private Integer ID;

    private String UserName;

    private String Password;

    private String Salt;

    private String Label;

    private Integer roleId;

}
