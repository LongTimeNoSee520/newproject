package com.zjtc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.FlowNodeLineMapper;
import com.zjtc.model.FlowNodeLine;
import com.zjtc.model.FlowNodeLineInfo;
import com.zjtc.service.FlowNodeLineService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
public class FlowNodeLineServiceImpl extends
    ServiceImpl<FlowNodeLineMapper, FlowNodeLine> implements
    FlowNodeLineService {


  @Override
  public List<FlowNodeLineInfo> selectLineInfo(String flowCode, String nodeCode) {
    return this.baseMapper.selectLineInfo(flowCode,nodeCode);
  }
}