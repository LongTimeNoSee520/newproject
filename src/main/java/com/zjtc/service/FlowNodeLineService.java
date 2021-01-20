package com.zjtc.service;


import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.FlowNodeLine;
import com.zjtc.model.FlowNodeLineInfo;
import java.util.List;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface FlowNodeLineService extends IService<FlowNodeLine> {
  /**
   * 查询办结单审核流程的流程线数据
   * */
  List<FlowNodeLineInfo> selectLineInfo(String flowCode, String nodeCode);
}
