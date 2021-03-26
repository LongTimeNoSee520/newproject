package com.zjtc.base.conf;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author yuchen
 * @date 2021/3/4
 */
@Configuration
@MapperScan(basePackages = "com.zjtc.mapper.waterBiz", sqlSessionTemplateRef = "waterBizSqlSessionTemplate")
public class DataSourceWaterBiz {

  @Value("${spring.datasource.waterBiz.url}")
  private String url;

  @Value("${spring.datasource.waterBiz.driverClassName}")
  private String driverClassName;

  @Value("${spring.datasource.waterBiz.username}")
  private String username;

  @Value("${spring.datasource.waterBiz.password}")
  private String password;

  /**
   * 数据源扫描的mapper路径
   */
  private static final String MAPPER_LOCATION = "classpath*:mapper/waterBiz/*.xml";


  @Bean(name = "waterBizDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.waterBiz")
  @Primary
  public DataSource getDataSourceWaterBiz() {
    return DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username)
        .password(password).build();
  }

  @Bean(name = "waterBizSqlSessionFactory")
  @Primary
  public SqlSessionFactory waterBizSqlSessionFactory(
      @Qualifier("waterBizDataSource") DataSource dataSource) throws Exception {
    MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
//    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    mybatisSqlSessionFactoryBean.setDataSource(dataSource);
    mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources(MAPPER_LOCATION));
    return mybatisSqlSessionFactoryBean.getObject();
  }

  @Bean(name = "waterBizTransactionManager")
  @Primary
  public DataSourceTransactionManager waterBizTransactionManager(
      @Qualifier("waterBizDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean(name="waterBizSqlSessionTemplate")
  @Primary
  public SqlSessionTemplate waterBizSqlSessionTemplate(
      @Qualifier("waterBizSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
      throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

}
