package com.toolbox.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.toolbox.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toolbox.valueobject.RequestBean;
import com.toolbox.vo.UserVO;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(value = "/login", tags = { "登录" }, description = "登录接口")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@GetMapping("/login")
	public RequestBean<UserVO> login(UserVO user)   {
		user.setRememberMe(true);
		// 用户认证信息
		Subject subject = SecurityUtils.getSubject();
		// 如果有点击 记住我
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword(), user.getRememberMe());

		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
			return RequestBean.Error("用户名或者密码为空！");
		}

		try {
			// 进行验证，这里可以捕获异常，然后返回对应信息
			subject.login(usernamePasswordToken);
			// subject.checkRole("admin");
			// subject.checkPermissions("query", "add");
		} catch (UnknownAccountException e) {
			log.error(ExceptionUtil.stacktraceToString(e));
			return RequestBean.Error(e.getMessage());
		} catch (AuthenticationException e) {
			log.error(ExceptionUtil.stacktraceToString(e));
			return RequestBean.Error("用户名或者密码错误！");
		} catch (AuthorizationException e) {
			log.error(ExceptionUtil.stacktraceToString(e));
			return RequestBean.Error("没有权限!");
		}
		UserVO userByName = loginService.getUserByName(user.getUserName());
		userByName.setSalt(null);
		userByName.setRoleVOList(null);
		userByName.setPassword(null);
		return RequestBean.Success(userByName);
	}

}
