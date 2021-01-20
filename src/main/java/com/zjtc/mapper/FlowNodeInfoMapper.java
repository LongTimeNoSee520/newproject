package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
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

  List<Map<String, Object>> nextAuditRole(@Param("id") String id,
      @Param("tableName") String tableName, @Param("nodeCode") String nodeCode,
      @Param("auditBtn") String auditBtn
  );
}
