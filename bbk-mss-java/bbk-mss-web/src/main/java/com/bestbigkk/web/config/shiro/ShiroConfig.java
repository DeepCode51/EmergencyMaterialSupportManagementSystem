package com.bestbigkk.web.config.shiro;

import com.bestbigkk.web.config.RedisConfig;
import com.bestbigkk.web.config.shiro.authc.BaseJwtRealm;
import com.bestbigkk.web.config.shiro.filter.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Scott
 * @date: 2018/2/7
 * @description: shiro 配置类
 */

@Slf4j
@Configuration
public class ShiroConfig {
	private static final String PASS = "anon";
	private static final String JWT = "jwt";

	private static final HashSet<String> PASS_URLS = new HashSet<String>(){{
		add("/dev/**");
		add("/swagger-ui.html" );
		add("/swagger**/**" );
		add("/webjars/**" );
		add("/v2/**" );
		add("/login/**");
		add("/register/**");
		add("/ws/**");
		add("/druid/**");
		add("/csrf");
		add("/");
	}};

	private static final HashSet<String> JWT_URLS = new HashSet<String>(){{
		add("/**");
	}};

	@Autowired
	private RedisConfig redisConfig;

	@Autowired(required = false)
	private BaseJwtRealm baseJwtRealm;

	@Value("${bbk.shiro.enable}")
	private Boolean enable;

	@Value("${bbk.shiro.cache:7200}")
	public Integer PERMISSION_CACHE_IN_REDIS_SECONDS;

	/**
	 * Filter Chain定义说明
	 *
	 * 1、一个URL可以配置多个Filter，使用逗号分隔
	 * 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 拦截器, 过滤链定义，从上向下顺序执行，
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

		//未启用shiro
		if (Objects.isNull(enable) || !enable) {
			log.warn("Shiro已禁用，默认放行所有Url...");
			filterChainDefinitionMap.put("/**", PASS);
			shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
			return shiroFilterFactoryBean;
		}

		PASS_URLS.forEach(url->filterChainDefinitionMap.put(url, PASS));
		JWT_URLS.forEach(url -> filterChainDefinitionMap.put(url, JWT));

		// 添加自己的过滤器并且取名为jwt
		Map<String, Filter> filterMap = new LinkedHashMap<>(1);
		filterMap.put(JWT, new JwtFilter());
		shiroFilterFactoryBean.setFilters(filterMap);

		// 未授权界面返回JSON
		///shiroFilterFactoryBean.setUnauthorizedUrl("/_/401");
		///shiroFilterFactoryBean.setLoginUrl("/_/401");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		log.info("配置Shiro拦截链完成...");
		return shiroFilterFactoryBean;
	}

	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

		//启用shiro
		if (Objects.nonNull(enable) && enable) {
			if (Objects.isNull(baseJwtRealm)) {
				log.error("\n----------\n\n\n当前已经启用了Shiro, 但是未发现BaseJwtRealm的实现类被注入，如果要启用Shiro，请实现! [参见: README.MD / Shiro权限控制 ]\n\n\n----------");
				System.exit(-1);
			}
			securityManager.setRealm(baseJwtRealm);
			log.info("成功注入自定义Realm: {}", baseJwtRealm.getClass().getName());
		}

		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);
		//自定义缓存实现,使用redis
		securityManager.setCacheManager(redisCacheManager());
		return securityManager;
	}


	/**
	 * 下面的代码是添加注解支持
	 * @return
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	/**
	 * cacheManager 缓存 redis实现
	 * 使用的是shiro-redis开源插件
	 *
	 * @return
	 */
	public RedisCacheManager redisCacheManager() {
		log.info("创建Shiro缓存管理器RedisCacheManager, 缓存时间:{}", PERMISSION_CACHE_IN_REDIS_SECONDS);
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisConfig.redisManager());
		//redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
		redisCacheManager.setPrincipalIdFieldName("id");
		//用户权限信息缓存时间
		redisCacheManager.setExpire(PERMISSION_CACHE_IN_REDIS_SECONDS);
		return redisCacheManager;
	}



}
