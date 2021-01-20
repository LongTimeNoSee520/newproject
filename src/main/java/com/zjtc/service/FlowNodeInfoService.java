package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowNodeInfo;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowNodeInfoService extends IService<FlowNodeInfo> {

  /**
   * 查询办结单审核流程的流程节点数据、
   * 并复制到流程节点记录表
   * */
  void selectAndInsert(String nodeCode,String businessId,String flowCode);
}
