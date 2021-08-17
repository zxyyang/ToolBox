package com.toolbox.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "permissions")
public class Permissions {
    private Integer id;
    @Column(name = "permissions_name")
    private String permissionsName;

}
