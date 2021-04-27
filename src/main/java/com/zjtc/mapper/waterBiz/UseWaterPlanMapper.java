package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TWUseWaterPlan的Dao接口
 *
 * @author
 */
@Mapper
public interface UseWaterPlanMapper extends BaseMapper<UseWaterPlan> {

  UseWaterPlan selectUseWaterPlanAll(
      @Param("nodeCode") String nodeCode,
      @Param("planYear") Integer planYear,
      @Param("waterUnitId") String waterUnitId,
      @Param("unitCode") String unitCode);

  UseWaterPlan selectUseWaterPlan(@Param("nodeCode") String nodeCode,
      @Param("unitCode") String unitCode, @Param("planYear") Integer planYear);
}