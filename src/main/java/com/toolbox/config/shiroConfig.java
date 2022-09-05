package com.toolbox.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.toolbox.util.CustomRealm;
import com.toolbox.util.ShiroSessionListener;

@Configuration
public class shiroConfig {

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private Integer redisPort;

	@Value("${spring.redis.database}")
	private Integer redisDb;

	@Value("${spring.redis.password}")
	private String redisPassword;

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 一定要注意顺序,否则就不好使了
		Map<String, String> map = new LinkedHashMap<>();
		// 配置不登录可以访问的资源，anon 表示资源都可以匿名访问
		map.put("/**", "anon");
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
		// map.put("/**", "authc");
		// 登录
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 首页
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 错误页面，认证不通过跳转
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		return shiroFilterFactoryBean;
	}

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
		securityManager.setCacheManager(cacheManager());

		// 配置自定义session管理，使用redis 参考博客：
		securityManager.setSessionManager(sessionManager());

		return securityManager;
	}

	/**
	 * 配置Shiro生命周期处理器
	 *
	 * @return
	 */
	@Bean(name = "lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
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

	/**
	 * shiro缓存管理器;
	 * 需要添加到securityManager中
	 *
	 * @return
	 */
	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		// redis中针对不同用户缓存
		redisCacheManager.setPrincipalIdFieldName("UserName");
		// 用户权限信息缓存时间
		redisCacheManager.setExpire(200000);
		return redisCacheManager;
	}

	@Bean
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(redisHost);
		redisManager.setPort(redisPort);
		redisManager.setPassword(redisPassword);
		redisManager.setDatabase(redisDb);
		return redisManager;
	}

	/**
	 * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
	 * MemorySessionDAO 直接在内存中进行会话维护
	 * EnterpriseCacheSessionDAO 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 *
	 * @return
	 */
	@Bean
	public SessionDAO sessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		// session在redis中的保存时间,最好大于session会话超时时间
		redisSessionDAO.setExpire(12000);
		return redisSessionDAO;
	}

	/**
	 * 配置保存sessionId的cookie
	 * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
	 * 默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
	 *
	 * @return
	 */
	@Bean("sessionIdCookie")
	public SimpleCookie sessionIdCookie() {
		// 这个参数是cookie的名称
		SimpleCookie simpleCookie = new SimpleCookie("sid");
		// setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

		// setcookie()的第七个参数
		// 设为true后，只能通过http访问，javascript无法访问
		// 防止xss读取cookie
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		// maxAge=-1表示浏览器关闭时失效此Cookie
		simpleCookie.setMaxAge(-1);
		return simpleCookie;
	}

	/**
	 * 配置会话管理器，设定会话超时及保存
	 *
	 * @return
	 */
	@Bean("sessionManager")
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		Collection<SessionListener> listeners = new ArrayList<SessionListener>();
		// 配置监听
		listeners.add(sessionListener());
		sessionManager.setSessionListeners(listeners);
		sessionManager.setSessionIdCookie(sessionIdCookie());
		sessionManager.setSessionDAO(sessionDAO());
		sessionManager.setCacheManager(cacheManager());

		// 全局会话超时时间（单位毫秒），默认30分钟 暂时设置为10秒钟 用来测试
		sessionManager.setGlobalSessionTimeout(1800000);
		// 是否开启删除无效的session对象 默认为true
		sessionManager.setDeleteInvalidSessions(true);
		// 是否开启定时调度器进行检测过期session 默认为true
		sessionManager.setSessionValidationSchedulerEnabled(true);
		// 设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
		// 设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
		// 暂时设置为 5秒 用来测试
		sessionManager.setSessionValidationInterval(3600000);
		// 取消url 后面的 JSESSIONID
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;

	}

	/**
	 * 配置session监听
	 *
	 * @return
	 */
	@Bean("sessionListener")
	public ShiroSessionListener sessionListener() {
		ShiroSessionListener sessionListener = new ShiroSessionListener();
		return sessionListener;
	}

}