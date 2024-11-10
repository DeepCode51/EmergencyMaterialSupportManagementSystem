package com.bestbigkk.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
*  @author: xugongkai
*  @data: 2019-12-11 17:09:23
*  @describe: 文档配置
**/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${server.servlet.application-display-name}")
    private String applicationName;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bestbigkk.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        final ApiInfo build = new ApiInfoBuilder()
                .title(applicationName)
                .description("应急物资保障系统")
                .termsOfServiceUrl("http://localhost:21584/")
                .contact(new Contact("xugongkai", "bestbigkk.com", "xugongkai@banggood.com"))
                .version("1.0-SNAPSHOT")
                .build();
        return build;
    }
}
