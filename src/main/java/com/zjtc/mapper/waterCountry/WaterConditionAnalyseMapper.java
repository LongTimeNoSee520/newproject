package com.zjtc.mapper.waterCountry;

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
   * @param planYear
   * @return
   */
  List<Double> realityWater(@Param("nodeCode")String nodeCode,@Param("planYear") Integer planYear);

  /**
   * 计划用水
   * @param nodeCode
   * @param planYear
   * @return
   */
  List<Double>   planWater(@Param("nodeCode")String nodeCode,@Param("planYear") Integer planYear);
}
