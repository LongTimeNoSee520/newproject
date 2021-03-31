package com.zjtc.mapper.waterSys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.FlowNodeInfo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yuyantian
 * @date 2020/12/31
 * @description
 */
@Mapper
public interface FlowNodeInfoMapper extends BaseMapper<FlowNodeInfo> {

  List<Map<String, Object>> firStAuditRole(@Param("flowCode") String flowCode,
      @Param("nodeCode") String nodeCode);

  List<Map<String, Object>> secondAuditRole(@Param("flowCode") String flowCode,
      @Param("nodeCode") String nodeCode);

  List<Map<String, Object>> nextAuditRole(@Param("nextNodeId") String nextNodeId,
      @Param("nodeCode") String nodeCode,
      @Param("auditBtn") String auditBtn
  );
}
