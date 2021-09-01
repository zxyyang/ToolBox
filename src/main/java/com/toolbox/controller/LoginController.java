package com.toolbox.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toolbox.valueobject.RequestBean;
import com.toolbox.vo.UserVO;

import io.swagger.annotations.Api;

@RestController
@Api(value = "/login", tags = { "登录" }, description = "登录接口")
public class LoginController {

	@GetMapping("/login")
	public RequestBean<String> login(UserVO user) {
		user.setRememberMe(false);
		// 用户认证信息
		Subject subject = SecurityUtils.getSubject();
		// 如果有点击 记住我
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword(), user.getRememberMe());

		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
			return RequestBean.Error("用户名或者密码错误！");
		}

		try {
			// 进行验证，这里可以捕获异常，然后返回对应信息
			subject.login(usernamePasswordToken);
			// subject.checkRole("admin");
			// subject.checkPermissions("query", "add");
		} catch (UnknownAccountException e) {
			return RequestBean.Error("用户名或者密码错误！");
		} catch (AuthenticationException e) {
			return RequestBean.Error("用户名或者密码错误！");
		} catch (AuthorizationException e) {
			return RequestBean.Error(400, "没有权限！");
		}
		return RequestBean.Success();
	}

}
