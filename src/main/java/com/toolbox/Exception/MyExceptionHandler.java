package com.toolbox.Exception;

import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.toolbox.valueobject.RequestBean;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class MyExceptionHandler extends RuntimeException {

	/**
	 * 请求方式不支持
	 */
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public RequestBean handleException(HttpRequestMethodNotSupportedException e) {
		log.error(e.getMessage(), e);
		return RequestBean.Error("不支持' " + e.getMethod() + "'请求");
	}

	/**
	 * 拦截未知的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	public RequestBean notFount(RuntimeException e) {
		log.error("运行时异常:", e);
		return RequestBean.Error("运行时异常:" + e.getMessage());
	}

	/**
	 * 系统异常
	 */
	@ExceptionHandler(Exception.class)
	public RequestBean handleException(Exception e) {
		log.error(e.getMessage(), e);
		return RequestBean.Error("服务器错误，请联系管理员");
	}

	/**
	 * 权限异常
	 */
	@ExceptionHandler(AuthorizationException.class)
	public RequestBean AuthorizationException(AuthorizationException e) {
		log.error("没有权限:", e);
		return RequestBean.Error("没有权限！:" + e.getMessage());
	}

	/**
	 * 登录异常
	 */
	@ExceptionHandler(UnknownAccountException.class)
	public RequestBean UnknownAccountException(UnknownAccountException e) {
		log.error("账号密码错误:", e);
		return RequestBean.Error("账号密码错误！:" + e.getMessage());
	}
}