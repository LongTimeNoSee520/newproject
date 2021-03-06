package com.zjtc.mapper.waterSys;

import com.baomidou.mybatisplus.mapper.BaseMapper;
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
}
