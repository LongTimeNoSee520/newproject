package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnitMonitor;
import com.zjtc.model.vo.UseWaterMonitorExportVO;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/15
 */
@Mapper
public interface UseWaterUnitMonitorMapper extends BaseMapper<UseWaterUnitMonitor> {

  int queryNum(Map<String, Object> map);

  List<Map<String, Object>> queryPage(Map<String, Object> map);

  void updateDeleted(@Param("ids") List<String> ids);

  void initNextYear(@Param("userId") String userId, @Param("nodeCode") String nodeCode,
      @Param("monitorType") String monitorType,@Param("currentYear")Integer currentYear);

  /**
   * 查询导出的数据
   * @param map
   * @return
   */
  List <UseWaterMonitorExportVO> selectExportData(Map<String, Object> map);

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