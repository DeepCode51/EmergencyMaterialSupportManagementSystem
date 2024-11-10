package com.bestbigkk.web.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.JwtUtils;
import com.bestbigkk.persistence.entity.UserPO;
import com.bestbigkk.service.IUserService;
import com.bestbigkk.web.response.annotation.RW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: 开
 * @date: 2020-04-19 18:57:11
 * @describe: 用户控制器
 */
@RW
@Api(tags = "验证接口")
@RequestMapping(value = "/dev/verify", produces = {"application/json;charset=UTF-8"})
public class VerifyController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "登录操作")
    public R login(UserPO user) throws InterruptedException {
        final String account = user.getAccount();
        final String password = user.getPassword();
        if (Objects.isNull(account) || Objects.isNull(password)) {
            throw new BusinessException("用户名或密码错误");
        }
        final UserPO target = userService.getOne(new QueryWrapper<UserPO>().lambda().eq(UserPO::getAccount, account).eq(UserPO::getPassword, password));
        if (Objects.isNull(target)) {
            throw new BusinessException("用户名或密码错误");
        }
        Map<String, Object> userMap = new HashMap<>();
        try {
            Map<String, String> map = BeanUtils.describe(target);
            map.forEach(userMap::put);
        } catch (Exception e) {
            throw new BusinessException("登陆失败，Bean转化失败");
        }
        return new R(jwtUtils.encode("xugongkai", userMap), JSON.toJSONString(target).replaceAll("\"", "'"));
    }
}

@Data
@AllArgsConstructor
class R{
    private String token;
    private String userJson;
}
