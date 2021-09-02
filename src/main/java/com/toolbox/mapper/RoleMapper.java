package com.toolbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.toolbox.domain.Role;

@Mapper
public interface RoleMapper extends tk.mybatis.mapper.common.Mapper<Role> {

	@Select("select * from role where role_name =  #{roleName} ")
	List<Role> selectByName(String roleName);

	@Select("select permissions_id from role where id = #{ids}  ")
	List<Integer> selectById(@Param("ids") Integer ids);

	// get user's role list
	@Select("SELECT  role.id , role.role_name  from user_role,users,role where  role.id = user_role.role_id and  user_role.user_id  = #{id} \n")
	List<Role> selectRole(Integer id);

}
