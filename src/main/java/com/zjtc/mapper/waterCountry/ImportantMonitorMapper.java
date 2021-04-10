package com.zjtc.mapper.waterCountry;

import java.util.LinkedList;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author lianghao
 * @date 2020/12/25
 */

@Mapper
@Component
public interface ImportantMonitorMapper {

  /***
   * 查询重点单位四个季度的计划用水量
   * @param nodeCode
   * @param year
   * @return
   */
  Map<String,Object> queryPlan(@Param("nodeCode") String nodeCode,
      @Param("year") Integer year);
  /***
   * 查询重点单位四个季度的实际用水量
   * @param nodeCode
   * @param year
   * @return
   */
  LinkedList<Double> queryReal(@Param("nodeCode") String nodeCode,
      @Param("year") Integer year);
}
