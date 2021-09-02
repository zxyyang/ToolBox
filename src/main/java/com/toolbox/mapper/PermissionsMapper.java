package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.toolbox.domain.Permissions;

@Mapper
public interface PermissionsMapper extends tk.mybatis.mapper.common.Mapper<Permissions> {

	@Select("select * from permissions where permissions_name = #{PermissionsName} ")
	List<Permissions> selectByName(String PermissionsName);

	// get role's permissions list
	@Select("select permissions.id , permissions.permissions_name from permissions, role_permission where role_permission.role_id = #{roleId} and role_permission.permissions_id = permissions.id")
	List<Permissions> selectPermissions(Integer roleId);
}
