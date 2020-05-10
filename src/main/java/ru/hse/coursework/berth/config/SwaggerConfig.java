package ru.hse.coursework.berth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.hse.coursework.berth"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()));
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(List.of(new SecurityReference("Token", new AuthorizationScope[0])))
                .forPaths(PathSelectors.ant("/**"))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Token", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Berth-Booking",
                null, null, null, null, null, null, Collections.emptyList());
    }
}
