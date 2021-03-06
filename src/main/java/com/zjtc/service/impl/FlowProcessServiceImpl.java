package com.zjtc.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.FlowProcessMapper;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.User;
import com.zjtc.service.FlowProcessService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
@Slf4j
public class FlowProcessServiceImpl extends ServiceImpl<FlowProcessMapper, FlowProcess> implements
    FlowProcessService {

  @Override
  public void create(User user, String businessId, String opinions, String auditorName,
      String auditorId) {
    long start = System.currentTimeMillis();
    /**办结单/退、减免单提交(审核创建)*/
    FlowProcess commit = new FlowProcess();
    commit.setAuditContent(opinions);
    commit.setAuditStatus("0");//审核状态(0:短信/办结单/退减免单创建1:待审核,2:审核通过,3:审核不通过)
    commit.setCreateTime(new Date(start));//
    commit.setOperatorId(user.getId());
    commit.setOperator(user.getUsername());
    commit.setNodeCode(user.getNodeCode());
    commit.setBusinessId(businessId);
    commit.setOperationTime(commit.getCreateTime());
    /**办结单退、减免单提交(审核通过)*/
    FlowProcess commitAudit = new FlowProcess();
    commitAudit.setAuditContent(opinions);
    commitAudit.setAuditStatus("2");//审核状态(0:短信/办结单/退减免单创建1:待审核,2:审核通过,3:审核不通过)
    commitAudit.setCreateTime(new Date(start+50));//时间增加50毫秒，用于查询流程信息时排序
    commitAudit.setOperatorId(user.getId());
    commitAudit.setOperator(user.getUsername());
    commitAudit.setNodeCode(user.getNodeCode());
    commitAudit.setBusinessId(businessId);
    commitAudit.setOperationTime(commit.getCreateTime());
    /**提交后的下一环节数据*/
    FlowProcess next = new FlowProcess();
    next.setAuditStatus("1");
    next.setCreateTime(new Date(start+100));
    next.setOperator(auditorName);
    next.setOperatorId(auditorId);
    next.setBusinessId(businessId);
    next.setNodeCode(user.getNodeCode());
    List<FlowProcess> flowProcesses = new ArrayList<>();
    flowProcesses.add(commit);
    flowProcesses.add(commitAudit);
    flowProcesses.add(next);
    this.insertBatch(flowProcesses);
  }

  @Override
  public FlowProcess getLastData(String nodeCode, String businessId) {
    Wrapper auditWrapper = new EntityWrapper();
    auditWrapper.eq("business_id", businessId);
    auditWrapper.orderBy("create_time", false);
    FlowProcess flowProcess = this.selectOne(auditWrapper);
    return flowProcess;
  }

  @Override
  public void add(String nodeCode, String businessId, String operator, String operatorId) {
    FlowProcess flowProcess = new FlowProcess();
    flowProcess.setNodeCode(nodeCode);
    flowProcess.setAuditStatus("1");
    flowProcess.setOperator(operator);
    flowProcess.setOperatorId(operatorId);
    flowProcess.setCreateTime(new Date());
    flowProcess.setBusinessId(businessId);
    this.insert(flowProcess);
  }

  @Override
  public FlowProcess selectFirstRecords(String businessId) {
    Wrapper auditWrapper = new EntityWrapper();
    auditWrapper.eq("business_id", businessId);
    auditWrapper.eq("audit_status", "0");
    FlowProcess flowProcess = this.selectOne(auditWrapper);
    return flowProcess;
  }

}