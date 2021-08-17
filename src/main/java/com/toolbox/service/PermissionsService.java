package com.toolbox.service;

import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Permissions;


public interface PermissionsService {
    PageInfo<Permissions> qurryAll(Integer pageNumber, Integer pageSize);

    PageInfo<Permissions> selectByName(String PermissionsName, Integer pageNumber, Integer pageSize);

}
