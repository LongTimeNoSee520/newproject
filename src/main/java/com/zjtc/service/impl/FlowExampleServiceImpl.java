package com.zjtc.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.FlowExampleMapper;
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
  public void add(User user, String businessId) {
    FlowExample flowExample = new FlowExample();
    flowExample.setBusinessId(businessId);
    flowExample.setCreateTime(new Date());
    flowExample.setCreator(user.getUsername());
    flowExample.setCreatorId(user.getId());
    flowExample.setNodeCode(user.getNodeCode());
    flowExample.setFlowStatus("1");//1-正在流转;2-已结束
    flowExample.setExampleTitile(user.getUsername()+"办结单审核");
    this.baseMapper.insert(flowExample);
  }
}