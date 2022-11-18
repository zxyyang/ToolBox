package com.toolbox.service.Impl;

import java.util.ArrayList;
import java.util.List;

import com.toolbox.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toolbox.domain.Permissions;
import com.toolbox.domain.Role;
import com.toolbox.domain.User;
import com.toolbox.mapper.PermissionsMapper;
import com.toolbox.mapper.RoleMapper;
import com.toolbox.mapper.UserMapper;
import com.toolbox.service.LoginService;
import com.toolbox.vo.PermissionsVO;
import com.toolbox.vo.RoleVO;
import com.toolbox.vo.UserVO;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private PermissionsMapper permissionsMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserVO getUserByName(String userName) {

		UserVO userVO = new UserVO();
		List<PermissionsVO> permissionsVOList = new ArrayList<>();
		List<RoleVO> roleVOList = new ArrayList<>();

		User user = userMapper.queryByName(userName);

		userVO.setId(user.getID());
		userVO.setUserName(user.getUserName());
		userVO.setPassword(EncryptUtil.decryptPassWord(user.getPassword(),user.getSalt()));
		userVO.setSalt(user.getSalt());
		userVO.setLabel(user.getLabel());

		List<Role> roles = roleMapper.selectRole(user.getID());
		for (Role role : roles) {
			RoleVO roleVO = new RoleVO();
			roleVO.setId(role.getId());
			roleVO.setRoleName(role.getRoleName());
			List<Permissions> permissions = permissionsMapper.selectPermissions(roleVO.getId());
			for (Permissions per : permissions) {
				PermissionsVO permissionsVO = new PermissionsVO();
				permissionsVO.setId(per.getId());
				permissionsVO.setPermissionsNam(per.getPermissionsName());
				permissionsVOList.add(permissionsVO);
			}
			roleVO.setPermissionsVOLis(permissionsVOList);
			roleVOList.add(roleVO);

		}
		userVO.setRoleVOList(roleVOList);

		return userVO;
	}
}
