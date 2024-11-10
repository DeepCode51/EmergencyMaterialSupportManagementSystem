package com.bestbigkk.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
* @author: 开
* @date: 2020-03-24 15:54:47
* @describe: 跟随SpringBoot进行初始化，在SpringBoot完全启动之后将触发。
*/
@Slf4j
@Configuration
public class GlobalInit implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("伴随启动行为...");
    }

}
