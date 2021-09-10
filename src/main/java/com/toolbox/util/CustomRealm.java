package com.toolbox.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.toolbox.domain.User;
import com.toolbox.mapper.UserMapper;
import com.toolbox.service.LoginService;
import com.toolbox.vo.PermissionsVO;
import com.toolbox.vo.RoleVO;
import com.toolbox.vo.UserVO;

public class CustomRealm extends AuthorizingRealm {

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserMapper userMapper;

	/**
	 * @MethodName doGetAuthorizationInfo
	 * @Description 权限配置类
	 * @Param [principalCollection]
	 * @Return AuthorizationInfo
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		// 获取登录用户名
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		// String name = (String) principalCollection.getPrimaryPrincipal().getClass().getName();
		// 查询用户名称
		UserVO userVO = loginService.getUserByName(user.getUserName());
		// 添加角色和权限
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		for (RoleVO role : userVO.getRoleVOList()) {
			// 添加角色
			simpleAuthorizationInfo.addRole(role.getRoleName());
			// 添加权限
			for (PermissionsVO permissions : role.getPermissionsVOLis()) {
				simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsNam());
			}
		}
		return simpleAuthorizationInfo;
	}

	/**
	 * @MethodName doGetAuthenticationInfo
	 * @Description 认证配置类
	 * @Param [authenticationToken]
	 * @Return AuthenticationInfo
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		if (StringUtils.isEmpty(authenticationToken.getPrincipal())) {
			return null;
		}

		// 获取登录用户名
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
		String username = usernamePasswordToken.getUsername();
		String password = new String(usernamePasswordToken.getPassword());

		// 查询用户名称
		User user = userMapper.queryByName(username);
		if (user == null) {
			throw new UnknownAccountException("用户名或密码错误！");
		} else if (!password.equals(user.getPassword())) {
			throw new UnknownAccountException("用户名或密码错误！");

		}

		// 调用 CredentialsMatcher 校验 还需要创建一个类 继承CredentialsMatcher 如果在上面校验了,这个就不需要了
		// 配置自定义权限登录器 参考博客：

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		return info;
	}

	/**
	 * 重写方法,清除当前用户的的 授权缓存
	 * 
	 * @param principals
	 */
	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 重写方法，清除当前用户的 认证缓存
	 * 
	 * @param principals
	 */
	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	/**
	 * 自定义方法：清除所有 授权缓存
	 */
	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	/**
	 * 自定义方法：清除所有 认证缓存
	 */
	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	/**
	 * 自定义方法：清除所有的 认证缓存 和 授权缓存
	 */
	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}
}
