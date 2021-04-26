package com.zjtc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.FlowNodeInfo;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;

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
   * @param sort 排序
   */
  void add(String nodeCode ,String businessId,String operator,String operatorId,Integer sort);

  /**
   * 查询该审核流程发起人
   * @param businessId 业务id
   */
  FlowProcess selectFirstRecords(String businessId);

  /**
   * 查询相关业务流程信息
   * @param businessId,nodeCode
   */
  List<Map<String,Object>> queryAuditList(String businessId, String nodeCode);
  /**
   * 查询是否需要当前登录人员审核
   * */
  int ifNeedAudit(String id, String userId);

  List<Map<String, Object>> firstAuditRole(String flowCode, String nodeCode);


  List<FlowProcess> queryAll(String nodeCode);

  List<FlowNodeInfo> selectNodes(String nodeCode,String flowCode);

  /**
   * 查询当前用户是否属于第一环节
   * @param userId
   * @param nodeCode
   * @param flowCode
   * @return
   */
  boolean isFirstFlowNode(String userId, String nodeCode, String flowCode);
}
