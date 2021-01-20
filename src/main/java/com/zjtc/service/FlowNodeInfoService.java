package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowNodeInfo;
import java.util.List;
import java.util.Map;

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

  /**
   * @param id 业务id
   * @param tableName 业务表名
   * @param nodeCode 节点编码
   * @param auditBtn 审核是否通过 1通过 2不通过
   * @return
   */
  List<Map<String,Object>> nextAuditRole(String id, String tableName,String nodeCode,String auditBtn);

  /**
   * 查询第一个流程
   * @param flowCode 流程编码
   * @param nodeCode 节点编码
   * @return
   */
  List<Map<String,Object>> firStAuditRole(String flowCode,String nodeCode);
}
