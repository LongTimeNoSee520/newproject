package com.zjtc.base.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * MybatisPlus配置类
 *
 * @Author: Zhoudabo
 * @Date: 2020/5/10
 */
@Configuration
public class MybatisPlusConfig {

  /**
   * MyBatisPlus分页插件配置
   * 说明：指定 MySQL 方言，如果不指定，则可能分页查询出现数据不准确
   */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor page = new PaginationInterceptor();
    //指定 MySQL 方言，否则它可能不知道怎么写分页函数
    page.setDialectType(DbType.SQL_SERVER.getDb());
    return page;

  }
}