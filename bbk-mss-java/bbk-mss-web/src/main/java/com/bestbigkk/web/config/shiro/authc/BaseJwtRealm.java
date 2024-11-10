package com.bestbigkk.web.config.shiro.authc;

import com.bestbigkk.common.web.ResultCode;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import java.util.Objects;

/**
* @author: 开
* @date: 2020-03-26 10:25:03
* @describe: 配合Shiro进行身份，权限校验的Realm，主要从Jwt里获取信息。
 *  这里为了便于再次通用，对Realm再次进行了针对Jwt的封装。后续项目使用的时候，
 *  【需要建立一个具体的子类，实现贴合于当前系统的校验方法，来保证Shiro可用】
 *  如果Shiro未启用，则可以不配置。
*/

@Slf4j
public abstract class BaseJwtRealm extends AuthorizingRealm {

    @Autowired
    protected JwtUtils jwtUtils;

    private static final ResultCode unAuthorization = ResultCode.UN_AUTHORIZATION;



    /**
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token（UsernamePasswordToken）
     * */
    @Override
    public boolean supports(AuthenticationToken token) {
        //这个token就是从过滤器中传入的jwtToken
        return token instanceof JwtToken;
    }



    /**
     * 授权校验，只有通过了doGetAuthenticationInfo的身份认证，且方法要求了权限认证，才会执行。因此进入的JWT都是有效的
     * 可以验证当前用户的【角色】和【权限】
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String jwt = (String) principals.getPrimaryPrincipal();
        AuthorizationInfo authorizationInfo;
        try {
            authorizationInfo = verifyPermissionAndRolesIsValid(jwt);
            if (Objects.isNull(authorizationInfo)) {
                throw new BusinessException(unAuthorization, unAuthorization.msg);
            }
        } catch (Exception e) {
            throw new BusinessException(unAuthorization, unAuthorization.msg);
        }
        return authorizationInfo;
    }

    /**认证账户密码，这个token就是从过滤器中传入的jwtToken*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token){
        JwtToken jwtToken = (JwtToken) token;
        String jwt = jwtToken.getPrincipal();

        try {
            if (!verifyIdentityIsValid(jwt, jwtToken.getServletRequest())) {
                throw new BusinessException(unAuthorization, unAuthorization.msg);
            }
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SERVER_ERROR, "身份验证服务出现异常");
        }

        log.info("{}校验通过登录", jwt);

        // 这里需要再次返回一个对象给后续的拦截器进行验证，由于Jwt只是字符串，我们在这里已经验证了，所以直接返回 jwt, jwt 让后续的匹配为true即可。
        return new SimpleAuthenticationInfo(jwt, jwt,"JwtRealm");
    }

    /**
     *  校验当前Jwt表示的用户信息，是否是被当前系统认可的。
     * @param jwt 当前请求登录用户携带的JWT。
     * @param servletRequest 请求对象
     * @return 是否通过验证
     */
    protected abstract boolean verifyIdentityIsValid(String jwt, ServletRequest servletRequest);

    /**
     *  给定当前请求用户携带的jwt，要求返回其具有的权限以及角色信息
     * @param jwt 当前请求登录用户携带的JWT。
     * @return 角色信息。
     */
    protected abstract AuthorizationInfo verifyPermissionAndRolesIsValid(String jwt);


}
