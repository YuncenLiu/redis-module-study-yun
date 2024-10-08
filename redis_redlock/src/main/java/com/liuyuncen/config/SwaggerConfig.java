package com.liuyuncen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.config
 * @author: Xiang想
 * @createTime: 2024-08-02  17:36
 * @description: TODO
 * @version: 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${spring.swagger2.enabled}")
    private Boolean enabled;

    @Bean
    public Docket createRestApi() {
        return new Docket (DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.liuyuncen")) //你自己的package
                .paths (PathSelectors.any())
                .build();

    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Yun swagger2构建api接口文档 "+"\t"
                        + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()))
                .description( "RedisLock" )
                .version("1.0")
                .termsOfServiceUrl("https://github.com/yuncenLiu")
                .build();
    }
}
