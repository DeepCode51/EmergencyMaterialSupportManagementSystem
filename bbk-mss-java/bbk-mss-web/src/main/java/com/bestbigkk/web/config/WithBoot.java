package com.bestbigkk.web.config;

import com.bestbigkk.common.utils.RedisUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Objects;

/**
* @author: 开
* @date: 2020-04-19 19:30:14
* @describe: 伴随启动
*/
@Slf4j
@Component
public class WithBoot implements CommandLineRunner {

    @Getter
    @Value("${server.servlet.application-display-name}")
    private String applicationName;

    @Autowired
    private RedisUtils redisUtils;

    private final String machineCountFlag = applicationName + ":machineCountFlag";

    @Override
    public void run(String... args) {
        Object count = redisUtils.get(machineCountFlag);
        int no = 1;
        if (Objects.isNull(count)) {
            redisUtils.set(machineCountFlag, 1);
        } else {
            redisUtils.incr(machineCountFlag, 1);
            no = Integer.parseInt(count+"") + 1;
        }
        applicationName += "_node_" + no;
        log.info("成功分配机器编号：{}", applicationName);
    }

}
