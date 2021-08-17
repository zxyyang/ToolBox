package com.toolbox.domain;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "users")
public class User {

    private Integer ID;

    private String UserName;

    private String Password;

    private String Salt;

    private String Label;

    private Integer roleId;

}
