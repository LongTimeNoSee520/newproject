package com.zjtc.service;


import com.baomidou.mybatisplus.service.IService;
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
   */
  void add(String nodeCode ,String businessId,String operator,String operatorId);

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


  List<FlowProcess> queryAll(String nodeCode);
}
