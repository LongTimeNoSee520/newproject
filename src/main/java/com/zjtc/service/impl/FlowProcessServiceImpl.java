package com.zjtc.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.FlowProcessMapper;
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
    /**办结单/退、减免单提交(审核创建)*/
    FlowProcess commit = new FlowProcess();
    commit.setAuditContent(opinions);
    commit.setAuditStatus("0");//审核状态(0:短信/办结单/退减免单创建1:待审核,2:审核通过,3:审核不通过)
    commit.setCreateTime(new Date());
    commit.setOperatorId(user.getId());
    commit.setOperator(user.getUsername());
    commit.setNodeCode(user.getNodeCode());
    commit.setBusinessId(businessId);
    commit.setOperationTime(commit.getCreateTime());
    /**办结单退、减免单提交(审核通过)*/
    FlowProcess commitAudit = new FlowProcess();
    commitAudit.setAuditContent(opinions);
    commitAudit.setAuditStatus("2");//审核状态(0:短信/办结单/退减免单创建1:待审核,2:审核通过,3:审核不通过)
    commitAudit.setCreateTime(new Date());
    commitAudit.setOperatorId(user.getId());
    commitAudit.setOperator(user.getUsername());
    commitAudit.setNodeCode(user.getNodeCode());
    commitAudit.setBusinessId(businessId);
    commitAudit.setOperationTime(commit.getCreateTime());
    /**提交后的下一环节数据*/
    FlowProcess next = new FlowProcess();
    next.setAuditStatus("1");
    next.setCreateTime(new Date());
    next.setOperator(auditorName);
    next.setOperatorId(auditorId);
    next.setBusinessId(businessId);
    next.setNodeCode(user.getNodeCode());
    List<FlowProcess> flowProcesses =new ArrayList<>();
    flowProcesses.add(commit);
    flowProcesses.add(commitAudit);
    flowProcesses.add(next);
    this.insertBatch(flowProcesses);
  }
}