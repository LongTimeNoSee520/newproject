package com.zjtc.base.conf;

import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 * @author yuchen
 * @date 2021/3/6
 */
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {

  @Bean(name="asyncExecutor")
  public Executor asyncServiceExecutor(){
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(10);
    taskExecutor.setMaxPoolSize(50);
    taskExecutor.setQueueCapacity(200);
    taskExecutor.setKeepAliveSeconds(60);
    taskExecutor.setThreadNamePrefix("asyncExecutor-");
    taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    taskExecutor.setAwaitTerminationSeconds(60);
    return taskExecutor;
  }



}
