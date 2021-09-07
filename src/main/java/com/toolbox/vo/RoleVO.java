package com.toolbox.vo;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleVO implements Serializable {

	private Integer id;

	private String roleName;

	private List<PermissionsVO> permissionsVOLis;
}
