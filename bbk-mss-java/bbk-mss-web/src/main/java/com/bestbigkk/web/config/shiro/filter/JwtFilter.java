package com.bestbigkk.web.config.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.bestbigkk.common.web.ResultCode;
import com.bestbigkk.web.config.shiro.authc.JwtToken;
import com.bestbigkk.web.response.RWrapper;
import com.bestbigkk.web.response.Tips;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
* @author: 开
* @date: 2020-03-25 18:38:16
* @describe: 自定义一个Filter，用来拦截所有的请求判断是否携带Token
*   isAccessAllowed()判断是否携带了有效的JwtToken
*   onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
 *
 *  该拦截器必须要注册在Shiro默认拦截器的后面，因此【不要主动注入到容器中】否则该拦截器去拦截原本属于其他拦截器的地址。
 *   在anonymousFilter的后面注册jwtFilter，可以保证之前设置的放行的地址正常先进入anonymousFilter中。
 *
 *
*/
@Slf4j
public final class JwtFilter extends AccessControlFilter {


    /*
     * 1. 返回true，shiro就直接允许访问url
     * 2. 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     * */

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)  {
        log.warn("isAccessAllowed 方法被调用");
        //这里先让它始终返回false来使用onAccessDenied()方法
        return false;
    }

    /**
     * 返回结果为true表明登录通过
     */
    @Override
    @SneakyThrows
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)  {
        //所以以后发起请求的时候就需要在Header中放一个Authorization，值就是对应的Token
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        log.warn("onAccessDenied 方法被调用:::"+ request.getRequestURI());

        String jwt = request.getHeader("Authorization");
        log.info("从Request-Header中获取Authorization: {}", jwt);
        JwtToken jwtToken = new JwtToken(jwt);
        jwtToken.setServletRequest(servletRequest);

        try {
            // 委托 realm 进行登录认证, 所以这个地方最终还是调用JwtRealm进行的认证
            getSubject(servletRequest, servletResponse).login(jwtToken);
        } catch (Exception e) {
            //肯定是Jwt校验失败的异常
            final HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");

            final String s = Tips.getAndRemove();

            String jsonString = JSONObject.toJSONString(RWrapper.failure((HttpServletRequest) servletRequest, ResultCode.UN_AUTHORIZATION , s));

            final PrintWriter writer = response.getWriter();
            writer.write(jsonString);
            writer.flush();
            writer.close();

            return false;
        }

        return true;
    }
}

