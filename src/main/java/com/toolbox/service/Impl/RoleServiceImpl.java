package com.toolbox.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.toolbox.domain.Role;
import com.toolbox.mapper.RoleMapper;
import com.toolbox.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public PageInfo<Role> qurryAll(@RequestParam(defaultValue = "1") Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Role> roleList = roleMapper.selectAll();
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);
        return rolePageInfo;
    }

    public PageInfo<Role> selectByName(String roleName, @RequestParam(defaultValue = "1") Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        List<Role> roleList = roleMapper.selectByName(roleName);
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);
        return rolePageInfo;
    }
}
