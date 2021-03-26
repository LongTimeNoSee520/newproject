package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.vo.PlanDailyAdjustmentVO;
import com.zjtc.model.vo.SendListVO;
import com.zjtc.model.vo.UseWaterPlanExportVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/04
 */

@Mapper
public interface PlanDailyAdjustmentMapper extends BaseMapper<UseWaterPlan> {

  int queryNum(Map<String, Object> map);

  List<PlanDailyAdjustmentVO> queryPage(Map<String, Object> map);

  void updateRemarks(@Param("id") String id, @Param("remarks") String remarks);

  /**
   * 修改计划表数据的打印状态
   * */
  void updatePrintStatus(@Param("id") String id);

  List<Map<String,Object>> queryMessage(Map<String, Object> map);

  void updateExistSettlement(@Param("existSettlement") String existSettlement,
      @Param("unitCode") String unitCode, @Param("nodeCode") String nodeCode,
      @Param("planYear") Integer planYear);

  List<PlanDailyAdjustmentVO> queryList(Map<String, Object> map);

  /**
   * 查询导出数据
   * @param map
   * @return
   */
  List<UseWaterPlanExportVO> selectExportData(Map<String, Object> map);

  List<SendListVO> queryUnit(Map<String, Object> map);
}
