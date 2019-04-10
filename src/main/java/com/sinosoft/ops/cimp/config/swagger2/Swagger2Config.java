package com.sinosoft.ops.cimp.config.swagger2;

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("4.其他分组")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sinosoft.cimp.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createDefaultGroupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3.默认分组")
                .apiInfo(apiInfo())
                .select()
                .apis(input -> {
                    assert input != null;
                    Class<?> declaringClass = input.declaringClass();
                    // 排除
                    return declaringClass != BasicErrorController.class
                            && !declaringClass.isAnnotationPresent(BusinessApiGroup.class)
                            && !declaringClass.isAnnotationPresent(SystemApiGroup.class);
                })
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createSystemGroupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("2.系统接口分组")
                .apiInfo(apiInfo())
                .select()
                .apis(input -> {
                    assert input != null;
                    Class<?> declaringClass = input.declaringClass();
                    return declaringClass.isAnnotationPresent(SystemApiGroup.class);
                })
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createBusRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1.业务接口分组")
                .apiInfo(apiInfo())
                .select()
                .apis(input -> {
                    assert input != null;
                    Class<?> declaringClass = input.declaringClass();
                    return declaringClass.isAnnotationPresent(BusinessApiGroup.class);
                })
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("干部一体化综合管理平台 API 文档")
                .description("干部一体化综合管理平台接口文档")
                .version("1.0.0")
                .build();
    }
}
