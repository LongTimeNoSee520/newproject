package com.zjtc.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.FlowNodeLineInfoMapper;
import com.zjtc.model.FlowNodeLine;
import com.zjtc.model.FlowNodeLineInfo;
import com.zjtc.service.FlowNodeLineInfoService;
import com.zjtc.service.FlowNodeLineService;
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
public class FlowNodeLineInfoServiceImpl extends
    ServiceImpl<FlowNodeLineInfoMapper, FlowNodeLineInfo> implements
    FlowNodeLineInfoService {

  @Autowired
  private FlowNodeLineService flowNodeLineService;

  @Override
  public void selectAndInsert(String nodeCode,String flowCode) {
    /**1.查询办结单审核流程的流程线数据*/
//    final  String flowCode="endPaperFlow";
    List<FlowNodeLineInfo> flowNodeLineInfos = flowNodeLineService.selectLineInfo(flowCode,nodeCode);
    if (flowNodeLineInfos.isEmpty()){
      log.info("没有查询到流程节点线数据");
    }else {
      /**2.复制到流程节点线记录表*/
      for (FlowNodeLineInfo flowNodeLineInfo: flowNodeLineInfos){
        flowNodeLineInfo.setId(UUID.randomUUID().toString().replace("-", ""));
        flowNodeLineInfo.setCreateTime(new Date());
      }
    }

  }
}