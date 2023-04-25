package com.course.app.common.swagger.config;

import com.course.app.common.core.annotation.MyRequestBody;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 自动加载bean的配置对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@EnableSwagger2WebMvc
@EnableKnife4j
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "swagger", name = "enabled")
public class SwaggerAutoConfiguration {

    @Bean
    public Docket upmsDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1. 用户权限分组接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getServiceBasePackage() + ".upms.controller"))
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public Docket bizDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("2. 业务应用分组接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getServiceBasePackage() + ".app.controller"))
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public Docket workflowDocket(SwaggerProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3. 工作流通用操作接口")
                .ignoredParameterTypes(MyRequestBody.class)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage() + ".common.flow.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo(SwaggerProperties properties) {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion()).build();
    }
}
