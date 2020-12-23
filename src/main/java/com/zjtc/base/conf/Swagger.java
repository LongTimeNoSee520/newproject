package com.zjtc.base.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author yuchen
 * @date 2020/11/30
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
public class Swagger {

  @Bean
  public Docket createSwaggerApi() {
    return new Docket(DocumentationType.SWAGGER_2).apiInfo(myApi()).select().apis(
        RequestHandlerSelectors.basePackage("com.zjtc.controller")).paths(
        PathSelectors.any())
        .build().pathMapping("/water/service");
  }

  public ApiInfo myApi() {
    return new ApiInfoBuilder().title("节约用水服务rest接口")
        .contact(new Contact("yuchen", "http", "@qq.com")).description("节约用水服务rest接口").version("1.0")
        .build();
  }
}
