package com.zjtc.base.conf;

import com.zjtc.service.DictService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author yuyantian
 * @date 2020/12/15
 * @description 异步调用数据字典刷新缓存
 */
@Component
public class DictConf {
 @Autowired
 private DictService dictService;

  @Async
  @SneakyThrows
  public void asyncRefresh(String nodeCode){
    dictService.refreshDictData(nodeCode);
  }

}
