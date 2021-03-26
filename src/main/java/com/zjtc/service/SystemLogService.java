package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.SystemLog;
import com.zjtc.model.User;

/**
 * @author lianghao
 * @date 2020/12/09
 */

public interface SystemLogService extends IService<SystemLog> {

  /**
   * 日志新增
   */
  boolean logInsert(User user,String operateModule,String operateContent,String ip);

}
