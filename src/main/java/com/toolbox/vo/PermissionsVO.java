package com.toolbox.vo;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionsVO implements Serializable {

	private Integer id;

	private String permissionsNam;
}
