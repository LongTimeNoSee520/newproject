package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.FlowNodeInfo;
import java.util.List;
import java.util.Map;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowNodeInfoService extends IService<FlowNodeInfo> {

  /**
   * 查询办结单/退减免单审核流程的流程节点数据、流程线数据
   * 并复制到流程节点记录表、流程节点线记录表
   ** @param businessId 业务id
   * @param flowCode 业务表名
   * @param nodeCode 节点编码
   * @param nextNodeId 下一环节id(流程节点表配置的id)
   * @return 复制到流程节点信息表后 下一环节id对应的新生成的id
   * */
  String selectAndInsert(String nodeCode,String businessId,String flowCode,String nextNodeId);

  /**
   * @param id 业务id
   * @param tableName 业务表名
   * @param nodeCode 节点编码
   * @param auditBtn 审核是否通过 1通过 2不通过
   * @return
   */
  List<Map<String,Object>> nextAuditRole(String id, String tableName,String nodeCode,String auditBtn);

  /**
   * 查询第二个审核流程
   * @param flowCode 流程编码
   * @param nodeCode 节点编码
   * @return
   */
  List<Map<String,Object>> secondAuditRole(String flowCode,String nodeCode);

  /**
   * 查询第一个审核流程
   * @param flowCode 流程编码
   * @param nodeCode 节点编码
   * @return
   */
  List<Map<String,Object>> firstAuditRole(String flowCode,String nodeCode);

  /**
   * 根据节点id查询当前是否是第一环节
   */
  boolean isFirst(String id);
}
