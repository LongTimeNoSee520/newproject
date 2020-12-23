package com.zjtc.base.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author yuyantian
 * @date 2020/8/23
 */

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;

  @Override
  protected void addInterceptors(InterceptorRegistry registry) {
//    /*除login外都拦截*/
//    registry.addInterceptor(webInterceptor()).addPathPatterns("/**")
//        .excludePathPatterns("/login/login").excludePathPatterns("/file/downloadToFlow");
//    super.addInterceptors(registry);
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    //附件虚拟路径的配置
    registry.addResourceHandler("/"+fileUploadPath+"/**")
        .addResourceLocations("file:"+fileUploadRootPath+fileUploadPath+"/");
    super.addResourceHandlers(registry);
    super.addResourceHandlers(registry);
  }

  //  @Value("${file.uploadRealPath}")
//  private String uploadRealPath;
//
//  @Value("${file.uploadFolder}")
//  private String uploadFolder;
//
//  @Value("${file.uploadZip}")
//  private String uploadZip;
//
//  /**
//   * 文件映射uri
//   */
//  @Value("${file.fileMappingPath}")
//  private String fileMappingUri;
//  /**
//   * zip文件映射uri
//   */
//  @Value("${file.zipMappingPath}")
//  private String zipMappingUri;
//
//  @Override
//  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//    //附件虚拟路径的配置
//    registry.addResourceHandler(fileMappingUri+"/**")
//        .addResourceLocations("file:" + uploadRealPath + uploadFolder+"/");
//    //zip文件
//    registry.addResourceHandler(zipMappingUri+"/**")
//        .addResourceLocations("file:" + uploadRealPath + uploadZip+"/");
//    super.addResourceHandlers(registry);
//  }


  @Bean
  public WebInterceptor webInterceptor() {
    return new WebInterceptor();
  }
}
