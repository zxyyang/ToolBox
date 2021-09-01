package com.toolbox.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.toolbox.util.CustomRealm;

@Configuration
public class shiroConfig {

	@Bean
	@ConditionalOnMissingBean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
		defaultAAP.setProxyTargetClass(true);
		return defaultAAP;
	}

	// 将自己的验证方式加入容器
	@Bean
	public CustomRealm myShiroRealm() {
		CustomRealm customRealm = new CustomRealm();
		return customRealm;
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager securityManager(CustomRealm myShiroRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置自定义realm.
		securityManager.setRealm(myShiroRealm);
		// 配置记住我
		securityManager.setRememberMeManager(rememberMeManager());

		// 配置 redis缓存管理器 参考博客：
		// securityManager.setCacheManager(getEhCacheManager());

		// 配置自定义session管理，使用redis 参考博客：
		// securityManager.setSessionManager(sessionManager());

		return securityManager;
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 一定要注意顺序,否则就不好使了
		Map<String, String> map = new LinkedHashMap<>();
		// 配置不登录可以访问的资源，anon 表示资源都可以匿名访问
		// map.put("/**", "anon");
		map.put("/login", "anon");
		map.put("/", "anon");
		map.put("/css/**", "anon");
		map.put("/js/**", "anon");
		map.put("/img/**", "anon");
		map.put("/druid/**", "anon");
		// 登出
		map.put("/logout", "logout");
		// 对所有用户认证
		// 其他资源都需要认证 authc 表示需要认证才能进行访问 user表示配置记住我或认证通过可以访问的地址
		map.put("/**", "authc");
		// 登录
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 首页
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 错误页面，认证不通过跳转
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		return shiroFilterFactoryBean;
	}

	/**
	 * 开启shiro 注解模式
	 * 可以在controller中的方法前加上注解
	 * 如 @RequiresPermissions("userInfo:add")
	 *
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
	 *
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

		// setcookie()的第七个参数
		// 设为true后，只能通过http访问，javascript无法访问
		// 防止xss读取cookie
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		// <!-- 记住我cookie生效时间1小时 ,单位秒;-->
		simpleCookie.setMaxAge(3600);
		return simpleCookie;
	}

	/**
	 * cookie管理对象;记住我功能,rememberMe管理器
	 *
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode("GNKHt4WN/Z9nWik67+hjug=="));
		return cookieRememberMeManager;
	}

	/**
	 * FormAuthenticationFilter 过滤器 过滤记住我
	 *
	 * @return
	 */
	@Bean
	public FormAuthenticationFilter formAuthenticationFilter() {
		FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
		// 对应前端的checkbox的name = rememberMe
		formAuthenticationFilter.setRememberMeParam("rememberMe");
		return formAuthenticationFilter;
	}

}