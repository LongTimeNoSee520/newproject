package com.zjtc.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.FlowProcessMapper;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.User;
import com.zjtc.service.FlowProcessService;

import java.util.Date;
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
  public void create(User user, String businessId, String opinions) {
    /**办结单提交(审核创建)*/
    FlowProcess commit = new FlowProcess();
    commit.setAuditContent(opinions);
    commit.setAuditStatus("0");//审核状态(0:短信/办结单创建1:待审核,2:审核通过,3:审核不通过)
    commit.setCreateTime(new Date());
    commit.setOperatorId(user.getId());
    commit.setOperator(user.getUsername());
    commit.setNodeCode(user.getNodeCode());
    commit.setBusinessId(businessId);
    commit.setOperationTime(commit.getCreateTime());
    /**提交后的下一环节数据*/
    FlowProcess next = new FlowProcess();
    /**查询*/
  }
}