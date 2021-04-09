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
@MapperScan(basePackages = "com.zjtc.mapper.waterCounty", sqlSessionTemplateRef = "waterBizSqlSessionTemplate")
public class DataSourceWaterCounty {

  @Value("${spring.datasource.waterCounty.url}")
  private String url;

  @Value("${spring.datasource.waterCounty.driverClassName}")
  private String driverClassName;

  @Value("${spring.datasource.waterCounty.username}")
  private String username;

  @Value("${spring.datasource.waterCounty.password}")
  private String password;

  /**
   * 数据源扫描的mapper路径
   */
  private static final String MAPPER_LOCATION = "classpath*:mapper/waterCounty/*.xml";


  @Bean(name = "waterCountyDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.waterCounty")
  @Primary
  public DataSource getDataSourceWaterBiz() {
    return DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username)
        .password(password).build();
  }

  @Bean(name = "waterCountySqlSessionFactory")
  @Primary
  public SqlSessionFactory waterBizSqlSessionFactory(
      @Qualifier("waterCountyDataSource") DataSource dataSource) throws Exception {
    MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
//    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    mybatisSqlSessionFactoryBean.setDataSource(dataSource);
    mybatisSqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources(MAPPER_LOCATION));
    return mybatisSqlSessionFactoryBean.getObject();
  }

  @Bean(name = "waterCountyTransactionManager")
  @Primary
  public DataSourceTransactionManager waterBizTransactionManager(
      @Qualifier("waterCountyDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean(name="waterCountySqlSessionTemplate")
  @Primary
  public SqlSessionTemplate waterBizSqlSessionTemplate(
      @Qualifier("waterCountySqlSessionFactory") SqlSessionFactory sqlSessionFactory)
      throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

}
