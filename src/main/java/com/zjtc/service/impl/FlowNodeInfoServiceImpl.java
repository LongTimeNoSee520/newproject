package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.FlowNodeInfoMapper;
import com.zjtc.model.FlowNode;
import com.zjtc.model.FlowNodeInfo;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowNodeService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
@Slf4j
public class FlowNodeInfoServiceImpl extends ServiceImpl<FlowNodeInfoMapper, FlowNodeInfo> implements
    FlowNodeInfoService {

  @Autowired
  private FlowNodeService flowNodeService;

  @Override
  public void selectAndInsert(String nodeCode,String businessId,String flowCode) {
    /**1.查询办结单流程节点数据*/
    List<FlowNodeInfo> flowNodeInfos = flowNodeService.selectNodes(nodeCode,flowCode);
    if (flowNodeInfos.isEmpty()){
      log.info("没有查询到办结单流程节点数据");
    }else {
     /**2.将办结单流程节点数据插入流程节点记录表*/
       for(FlowNodeInfo flowNodeInfo : flowNodeInfos){
         flowNodeInfo.setId(UUID.randomUUID().toString().replace("-", ""));
         flowNodeInfo.setBusinessId(businessId);
         flowNodeInfo.setCreateTime(new Date());
       }
       this.insertBatch(flowNodeInfos);
    }

  }
}
