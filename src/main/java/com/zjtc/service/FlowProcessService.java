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
   * 审核流新增(3条数据)
   * */
  void create(User user, String businessId, String opinions, String auditorName,
      String auditorId);
  /**
   * 查询最后一条记录
   */
  FlowProcess getLastData(String nodeCode,String businessId);

  /**
   * 新增一条记录
   * @param nodeCode 节点编码
   * @param businessId 业务id
   * @param operator 操作人
   * @param operatorId  操作人id
   */
  void add(String nodeCode ,String businessId,String operator,String operatorId);
}
