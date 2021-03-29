package com.zjtc.mapper.waterSys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.FlowNodeInfo;
import com.zjtc.model.FlowProcess;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface FlowProcessMapper extends BaseMapper<FlowProcess> {

  List<Map<String, Object>> queryAuditList(@Param("id") String businessId,
      @Param("nodeCode") String nodeCode);

  int ifNeedAudit(@Param("businessId")String businessId, @Param("userId")String userId);

  List<Map<String, Object>> firStAuditRole(@Param("flowCode") String flowCode,
      @Param("nodeCode") String nodeCode);

  List<Map<String, Object>> secondAuditRole(@Param("flowCode") String flowCode,
      @Param("nodeCode") String nodeCode);

  List<FlowNodeInfo> selectNodes(@Param("nodeCode") String nodeCode,
      @Param("flowCode") String flowCode);

  long isFirstFlowNode(@Param("userId") String userId, @Param("nodeCode") String nodeCode,
      @Param("flowCode") String flowCode);
}
