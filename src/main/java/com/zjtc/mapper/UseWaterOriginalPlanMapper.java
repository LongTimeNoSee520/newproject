package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterOriginalPlan;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TWUseWaterOriginalPlan的Dao接口
 *
 * @author
 */
@Mapper
public interface UseWaterOriginalPlanMapper extends BaseMapper<UseWaterOriginalPlan> {

  /**
   * 编制初始化
   *
   * @param year 年份
   * @param userType 用户类型
   * @param unitCodeStart 单位编号开头
   */
  List<Map<String, Object>> initPlan(@Param("year") int year,
      @Param("unitCodeType") String userType, @Param("unitCodeStart") String unitCodeStart,
      @Param("userId") String userId, @Param("nodeCode") String nodeCode
  );

  /**
   * 编制查询
   *
   * @param year 年份
   * @param userType 用户类型
   * @param unitCodeStart 单位编号开头
   */
  List<Map<String, Object>> nowYearPlan(@Param("year") int year, @Param("userType") String userType,
      @Param("unitCodeStart") String unitCodeStart);
}