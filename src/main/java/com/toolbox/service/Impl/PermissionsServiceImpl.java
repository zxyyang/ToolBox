package com.toolbox.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Permissions;
import com.toolbox.mapper.PermissionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class PermissionsServiceImpl {
    @Autowired
    private PermissionsMapper permissionsMapper;

    public PageInfo<Permissions> qurryAll(@RequestParam(defaultValue = "1") Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Permissions> PermissionsList = permissionsMapper.selectAll();
        PageInfo<Permissions> PermissionsPageInfo = new PageInfo<>(PermissionsList);
        return PermissionsPageInfo;
    }

    public PageInfo<Permissions> selectByName(String PermissionsName, @RequestParam(defaultValue = "1") Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Permissions> PermissionsList = permissionsMapper.selectByName(PermissionsName);
        PageInfo<Permissions> PermissionsPageInfo = new PageInfo<>(PermissionsList);
        return PermissionsPageInfo;
    }
}
