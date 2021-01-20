package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.FlowNode;
import com.zjtc.model.FlowNodeInfo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface FlowNodeMapper extends BaseMapper<FlowNode> {
//  FlowNodeInfo getNextMess(@Param("id") String id, @Param("nextflowId") String nextCodeId,
//      @Param("nodeCode") String nodeCode);
//
//  List<Map<String,Object>> firStAuditRole(@Param("nodeCode") String nodeCode);
//
//  List<Map<String,Object>> nextAuditRole(@Param("nodeCode") String nodeCode, @Param("id") String id);
  /**
   * 流程节点记录表字段与节点表一致，返回流程节点记录实体方便记录表新增
   * */
  List<FlowNodeInfo> selectNodes(@Param("nodeCode")String nodeCode,@Param("flowCode")String flowCode);
}
