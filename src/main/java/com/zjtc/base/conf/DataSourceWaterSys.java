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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author yuchen
 * @date 2021/3/4
 */
@Configuration
@MapperScan(basePackages = "com.zjtc.mapper.waterSys", sqlSessionTemplateRef = "waterSysSqlSessionTemplate")
public class DataSourceWaterSys {

  @Value("${spring.datasource.waterSys.url}")
  private String url;

  @Value("${spring.datasource.waterSys.driverClassName}")
  private String driverClassName;

  @Value("${spring.datasource.waterSys.username}")
  private String username;

  @Value("${spring.datasource.waterSys.password}")
  private String password;

  /**
   * 数据源扫描的mapper路径
   */
  private static final String MAPPER_LOCATION = "classpath*:mapper/waterSys/*.xml";


  @Bean(name = "waterSysDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.waterSys")
  public DataSource getDataSourceWaterSys() {
    return DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username)
        .password(password).build();
  }

  @Bean(name = "waterSysSqlSessionFactory")
  public SqlSessionFactory waterSysSqlSessionFactory(
      @Qualifier("waterSysDataSource") DataSource dataSource) throws Exception {
//    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
    mybatisSqlSessionFactoryBean.setDataSource(dataSource);
    mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources(MAPPER_LOCATION));
    return mybatisSqlSessionFactoryBean.getObject();
  }

  @Bean(name = "waterSysTransactionManager")
  public DataSourceTransactionManager waterSysTransactionManager(
      @Qualifier("waterSysDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean(name="waterSysSqlSessionTemplate")
  public SqlSessionTemplate waterSysSqlSessionTemplate(
      @Qualifier("waterSysSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
      throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

}
