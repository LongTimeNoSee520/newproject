package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowNode;
import com.zjtc.model.FlowNodeInfo;
import java.util.List;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowNodeService extends IService<FlowNode> {
//  /**
//   * 查看当前短信信息审核过后，是否还有下一环节数据
//   * 根据下一环节id,获取审核后，下一环节信息
//   * @param nextCodeId 下一环节id
//   * * @param nodeCode 节点编码
//   * @return
//   */
//  public FlowNodeInfo getNextMess(String id, String nextCodeId, String nodeCode);
  /**
   * 查询办结单流程节点数据
   * */
  List<FlowNodeInfo> selectNodes(String nodeCode,String flowCode);
  /**
   * 查询当前用户是否是第一个审核流程
   */
  long isFirstFlowNode(String userId,String nodeCode,String flowCode);
}
