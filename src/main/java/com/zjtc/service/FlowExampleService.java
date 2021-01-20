package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowExample;
import com.zjtc.model.User;


/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowExampleService extends IService<FlowExample> {
  /**新增*/
  void add(User user, String businessId);
}
