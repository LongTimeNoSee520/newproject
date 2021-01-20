package com.zjtc.service;


import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.User;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowProcessService extends IService<FlowProcess> {
  /**
   * 审核流新增(创建和下一环节两条数据)
   * */
  void create(User user, String businessId, String opinions);
}
