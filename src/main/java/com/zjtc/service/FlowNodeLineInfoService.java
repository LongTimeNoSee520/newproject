package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowNodeLineInfo;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowNodeLineInfoService extends IService<FlowNodeLineInfo> {
  /**
   * 查询办结单审核流程的流程线数据
   * 并复制到流程节点线记录表
   * */
  void selectAndInsert(String nodeCode,String flowCode);
}
