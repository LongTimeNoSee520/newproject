package com.zjtc.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.mapper.waterBiz.FlowExampleMapper;
import com.zjtc.model.FlowExample;
import com.zjtc.model.User;
import com.zjtc.service.FlowExampleService;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
public class FlowExampleServiceImpl extends ServiceImpl<FlowExampleMapper, FlowExample> implements
    FlowExampleService {


  @Override
  public void add(User user, String businessId,String type) {
    FlowExample flowExample = new FlowExample();
    flowExample.setBusinessId(businessId);
    flowExample.setCreateTime(new Date());
    flowExample.setCreator(user.getUsername());
    flowExample.setCreatorId(user.getId());
    flowExample.setNodeCode(user.getNodeCode());
    flowExample.setFlowStatus("1");//1-正在流转;2-已结束
    if (type.equals(AuditConstants.PAY_TODO_TYPE)) {
      flowExample.setExampleTitile(user.getUsername()+"退减免单审核");
    }
    if (type.equals(AuditConstants.END_PAPER_TODO_TYPE)) {
      flowExample.setExampleTitile(user.getUsername()+"办结单审核");
    }
    this.baseMapper.insert(flowExample);
  }

  @Override
  public boolean edit(String nodeCode, String businessId) {
    QueryWrapper entityQueryWrapper=new QueryWrapper();
    entityQueryWrapper.eq("node_code",nodeCode);
    entityQueryWrapper.eq("business_id",businessId);
    FlowExample flowExample= this.getOne(entityQueryWrapper);
    flowExample.setFlowStatus("2");
    return this.updateById(flowExample);
  }
}