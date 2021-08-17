package com.toolbox.mapper;

import com.toolbox.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends tk.mybatis.mapper.common.Mapper<Role> {

    @Select("select * from role where role_name like  '%#{roleName}%' ")
    List<Role> selectByName(String roleName);
}
