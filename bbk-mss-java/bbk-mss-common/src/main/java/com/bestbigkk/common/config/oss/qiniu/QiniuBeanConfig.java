package com.bestbigkk.common.config.oss.qiniu;

import com.qiniu.common.Zone;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QiniuBeanConfig {

    @Autowired
    private QiniuConfig qiniuProperties;

    /** 获取七牛云的Configuration */
    @Bean
    public com.qiniu.storage.Configuration getQiniuConfig() {
        return new com.qiniu.storage.Configuration(Zone.zone0());
    }

    /** 获取Auth */
    @Bean
    public Auth getAuth() {
        return Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
    }
}
