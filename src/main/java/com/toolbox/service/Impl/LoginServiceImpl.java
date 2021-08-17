package com.toolbox.service.Impl;

import com.toolbox.service.LoginService;
import com.toolbox.vo.PermissionsVO;
import com.toolbox.vo.RoleVO;
import com.toolbox.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public UserVO getUserByName(String getMapByName) {
        return getMapByName(getMapByName);
    }

    /**
     * 模拟数据库查询
     *
     * @param userName 用户名
     * @return User
     */
    private UserVO getMapByName(String userName) {
        PermissionsVO permissions1 = new PermissionsVO();
        permissions1.setId(1);
        permissions1.setPermissionsNam("qurry");
        PermissionsVO permissions2 = new PermissionsVO();
        permissions1.setId(2);
        permissions1.setPermissionsNam("add");
        List<PermissionsVO> permissionsSet = new ArrayList<>();
        permissionsSet.add(permissions1);
        permissionsSet.add(permissions2);
        RoleVO role = new RoleVO();
        role.setId(1);
        role.setRoleName("admin");
        role.setPermissionsVOLis(permissionsSet);
        List<RoleVO> roleSet = new ArrayList<>();
        roleSet.add(role);
        UserVO user = new UserVO();
        user.setID(1);
        user.setUserName("zxyang");
        user.setLabel("杨梓轩");
        user.setPassword("123456");
        user.setRoleVOList(roleSet);

        //操作
        Map<String, UserVO> map = new HashMap<>();
        map.put(user.getUserName(), user);

        List<PermissionsVO> permissionsSet1 = new ArrayList<>();
        permissionsSet1.add(permissions1);
        RoleVO role1 = new RoleVO();
        role1.setId(2);
        role1.setRoleName("user");
        role1.setPermissionsVOLis(permissionsSet1);
        List<RoleVO> roleSet1 = new ArrayList<>();
        roleSet1.add(role1);
        UserVO user1 = new UserVO();
        user1.setID(2);
        user1.setUserName("ceshi");
        user1.setPassword("123456");
        user1.setLabel("测试");
        user1.setRoleVOList(roleSet1);
        map.put(user1.getUserName(), user1);
        return map.get(userName);
    }
}
