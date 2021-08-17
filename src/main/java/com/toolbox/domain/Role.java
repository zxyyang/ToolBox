package com.toolbox.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "role")
public class Role {
    private Integer id;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "permissions_id")
    private Integer permissionsId;
}
