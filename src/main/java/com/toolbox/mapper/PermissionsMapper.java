package com.toolbox.mapper;

import com.toolbox.domain.Permissions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionsMapper extends tk.mybatis.mapper.common.Mapper<Permissions> {
    @Select("select * from permissions where permissions_name like  '%#{PermissionsName}%' ")
    List<Permissions> selectByName(String PermissionsName);
}
