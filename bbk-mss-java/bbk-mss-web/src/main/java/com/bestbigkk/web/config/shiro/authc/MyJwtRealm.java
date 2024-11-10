package com.bestbigkk.web.config.shiro.authc;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
* @author: 开
* @date: 2020-03-26 18:05:36
* @describe: 示例Realm实现
*/
@Component
@Slf4j
public class MyJwtRealm extends BaseJwtRealm {

    private static final Set<String> TOM_ROLE_NAME_SET = new HashSet<>();
    private static final Set<String> TOM_PERMISSION_NAME_SET = new HashSet<>();
    private static final Set<String> JERRY_ROLE_NAME_SET = new HashSet<>();
    private static final Set<String> JERRY_PERMISSION_NAME_SET = new HashSet<>();

    static {
        TOM_ROLE_NAME_SET.add("admin");
        TOM_PERMISSION_NAME_SET.add("insert");
        TOM_PERMISSION_NAME_SET.add("update");
        TOM_PERMISSION_NAME_SET.add("delete");
        TOM_PERMISSION_NAME_SET.add("query");

        JERRY_ROLE_NAME_SET.add("user");
        JERRY_PERMISSION_NAME_SET.add("query");
    }

    @Override
    protected AuthorizationInfo verifyPermissionAndRolesIsValid(String jwt) {
        log.info("自定义校验权限：{}", jwt);
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();

        //解析Token中的信息。
        final Claims decode = jwtUtils.decode(jwt);
        final String username = (String)decode.get("username");

        if ("tom".equals(username)) {
            info.addRoles(TOM_ROLE_NAME_SET);
            info.addStringPermissions(TOM_PERMISSION_NAME_SET);
        } else if ("jerry".equals(username)) {
            info.addRoles(JERRY_ROLE_NAME_SET);
            info.addStringPermissions(JERRY_PERMISSION_NAME_SET);
        }

        return info;
    }

    @Override
    protected boolean verifyIdentityIsValid(String jwt, ServletRequest request) {
        log.info("自定义验证身份：{}", jwt);
        try {
            return !jwtUtils.isExpired(jwt);
        } catch (Exception e) {
            return false;
        }
    }

}