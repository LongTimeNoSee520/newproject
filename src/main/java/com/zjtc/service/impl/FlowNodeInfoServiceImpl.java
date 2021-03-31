package com.zjtc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterSys.FlowNodeInfoMapper;
import com.zjtc.model.FlowNodeInfo;
import com.zjtc.model.FlowNodeLineInfo;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowNodeLineInfoService;
import com.zjtc.service.FlowNodeLineService;
import com.zjtc.service.FlowNodeService;
import com.zjtc.service.FlowProcessService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
@Slf4j
public class FlowNodeInfoServiceImpl extends
    ServiceImpl<FlowNodeInfoMapper, FlowNodeInfo> implements
    FlowNodeInfoService {

  @Autowired
  private FlowNodeService flowNodeService;
  @Autowired
  private FlowNodeLineService flowNodeLineService;
  @Autowired
  private FlowNodeLineInfoService flowNodeLineInfoService;

  @Autowired
  private FlowProcessService  flowProcessService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String selectAndInsert(String nodeCode, String businessId, String flowCode,
      String nextNodeId) {
    String newNextNodeId = "";
    /**从流程节点表查询办结单/退减免单审核流程节点数据*/
    List<FlowNodeInfo> flowNodeInfos = flowProcessService.selectNodes(nodeCode, flowCode);
    /**从流程线表查询办结单/退减免单审核流程线数据*/
    List<FlowNodeLineInfo> flowNodeLineInfos = flowNodeLineService
        .selectLineInfo(flowCode, nodeCode);
    if (flowNodeInfos.isEmpty()) {
      log.info("没有查询到办结单/退减免单审核流程节点数据");
    } else {
      /**2.将办结单/退减免单审核流程节点数据插入流程节点记录表*/
      for (FlowNodeInfo flowNodeInfo : flowNodeInfos) {
        String flowNodeId = flowNodeInfo.getId();
        flowNodeInfo.setId(UUID.randomUUID().toString().replace("-", ""));
        flowNodeInfo.setBusinessId(businessId);
        flowNodeInfo.setCreateTime(new Date());
        /**将流程线表配置的数据的流程节点表id和下一环节流程节点id
         * 为“流程节点表”配置的节点流程id的 都设置为“流程节点信息表”的id
         * (即通过uuid生成的id flowNodeInfo.setId())
         * */
        if (!flowNodeLineInfos.isEmpty()) {
          for (FlowNodeLineInfo flowNodeLineInfo : flowNodeLineInfos) {
            if (flowNodeLineInfo.getFlowNodeId().equals(flowNodeId)) {
              flowNodeLineInfo.setFlowNodeId(flowNodeInfo.getId());//此时的id已经是重新生成的uuid
            }
            if (flowNodeLineInfo.getNextNodeId().equals(flowNodeId)) {
              flowNodeLineInfo.setNextNodeId(flowNodeInfo.getId());
            }
          }
        }
        /**在审核创建时，提交来的下一环节id是节点表(配置的id)，需要更新为流程信息表的id，即uuid生成的id*/
        if (flowNodeId.equals(nextNodeId)) {
          newNextNodeId = flowNodeInfo.getId();//此时的id已经是重新生成的uuid
        }
      }
      /**插入重新设置了flow_node_id，next_node_id的"流程线表"数据到"流程线信息表"*/
      flowNodeLineInfoService.add(flowNodeLineInfos);
      this.saveBatch(flowNodeInfos);
    }
    return newNextNodeId;
  }

  @Override
  public List<Map<String, Object>> nextAuditRole(String nextNodeId,String bussiesId,String nodeCode,String auditBtn) {
    return baseMapper.nextAuditRole(nextNodeId, bussiesId,nodeCode, auditBtn);
  }

  @Override
  public List<Map<String, Object>> secondAuditRole(String flowCode, String nodeCode) {
    return baseMapper.secondAuditRole(flowCode, nodeCode);
  }

  @Override
  public List<Map<String, Object>> firstAuditRole(String flowCode, String nodeCode) {
    return baseMapper.firStAuditRole(flowCode, nodeCode);

  }

  @Override
  public boolean isFirst(String id) {
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("id", id);
    wrapper.eq("flow_sort", "1");
    return this.count(wrapper) > 0;
  }
}
