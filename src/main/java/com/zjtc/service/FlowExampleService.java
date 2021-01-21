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
  void add(User user, String businessId,String type);

  /**
   * 修改流转状态
   * @param nodeCode 节点编码
   * @param businessId 业务id
   */
  boolean edit(String nodeCode,String businessId);
}
