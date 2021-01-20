package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowNodeLineInfo;
import java.util.List;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowNodeLineInfoService extends IService<FlowNodeLineInfo> {
//  /**
//   * 查询办结单审核流程的流程线数据
//   * 并复制到流程节点线记录表
//   * */
//  void selectAndInsert(String nodeCode,String flowCode);
  /**
   * 将重新设置了flow_node_id，next_node_id的流程线表数据
   * 插入 流程线信息表
   * */
  void add(List<FlowNodeLineInfo> flowNodeLineInfos);
}
