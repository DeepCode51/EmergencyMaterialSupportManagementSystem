package com.bestbigkk.common.config.oss.qiniu;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
* @author: 开
* @date: 2020-04-25 12:18:30
* @describe: 七牛云配置
*/
@Data
@Slf4j
@ConfigurationProperties(prefix = "bbk.oss.qiniu")
public class QiniuConfig {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String path;

    @PostConstruct
    public void init(){
        log.info("加载七牛云配置：" + accessKey + ", " + secretKey + ", " + bucket + ", " + path);
    }

}
