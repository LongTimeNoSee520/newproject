package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.FlowNodeMapper;
import com.zjtc.model.FlowNode;
import com.zjtc.model.FlowNodeInfo;
import com.zjtc.service.FlowNodeService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
@Slf4j
public class FlowNodeServiceImpl extends ServiceImpl<FlowNodeMapper, FlowNode> implements
    FlowNodeService {


  @Override
  public List<FlowNodeInfo> selectNodes(String nodeCode,String flowCode) {
    return this.baseMapper.selectNodes(nodeCode,flowCode);
  }

  @Override
  public long isFirstFlowNode(String userId, String nodeCode, String flowCode) {
    return baseMapper.isFirstFlowNode( userId,  nodeCode,  flowCode);
  }
}
