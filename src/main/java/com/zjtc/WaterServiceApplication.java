package com.zjtc;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan(basePackages = "com.zjtc")
@MapperScan(value = "{com.zjtc}",annotationClass = Mapper.class)
@EnableWebMvc
@EnableAsync
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class WaterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaterServiceApplication.class, args);
	}

}
