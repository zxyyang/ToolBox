package com.toolbox.service;

import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Role;

public interface RoleService {

    PageInfo<Role> qurryAll(Integer pageNumber, Integer pageSize);

    PageInfo<Role> selectByName(String roleName, Integer pageNumber, Integer pageSize);

}
