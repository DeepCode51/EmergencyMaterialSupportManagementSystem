package com.bestbigkk.web.config.shiro.authc;
 
import com.bestbigkk.common.utils.SnowflakeIdWorker;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.ServletRequest;

/**
* @author: 开
* @date: 2020-03-25 17:24:15
* @describe: 令牌
*/
@Data
public final class JwtToken implements AuthenticationToken {

    /**在Shiro的Redis缓存管理器中，使用该字段唯一标识*/
    private Long id;

	private String token;

    private ServletRequest servletRequest;

    public JwtToken(String token) {
        this.token = token;
        this.id = SnowflakeIdWorker.nextId();
    }
 
    @Override
    public String getPrincipal() {
        return token;
    }
 
    @Override
    public String getCredentials() {
        return token;
    }

    public ServletRequest getServletRequest() {
        return servletRequest;
    }

}

