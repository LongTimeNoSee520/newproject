package com.zjtc.mapper.waterBiz;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/4/10
 */
//用水情况分析
@Mapper
public interface WaterConditionAnalyseMapper {


  /**
   * 实际用水
   * @param nodeCode
   * @param year
   * @return
   */
  List<Double> realityWater(@Param("nodeCode")String nodeCode,@Param("year") Integer year);

  /**
   * 计划用水
   * @param nodeCode
   * @param year
   * @return
   */
  List<Double>   planWater(@Param("nodeCode")String nodeCode,@Param("year") Integer year);
}
