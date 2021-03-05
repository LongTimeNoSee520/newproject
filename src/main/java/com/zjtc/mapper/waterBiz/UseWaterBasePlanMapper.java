package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterBasePlan;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 用水基建计划
 * @author lianghao
 * @date 2020/12/24
 */
@Mapper
public interface UseWaterBasePlanMapper extends BaseMapper<UseWaterBasePlan> {

  int queryExistNum(@Param("unitCode") String unitCode, @Param("nodeCode") String nodeCode,
      @Param("planYear") Integer planYear);

  List<Integer> queryYear(@Param("nodeCode") String nodeCode, @Param("userId") String userId);

  boolean delete(@Param("ids") List<String> ids);

  int queryNum(Map<String, Object> map);

  List<UseWaterBasePlan> queryPage(Map<String, Object> map);

  int queryOthers(@Param("useWaterBasePlan") UseWaterBasePlan useWaterBasePlan);

  List<UseWaterBasePlan> selectExportData(Map<String, Object> map);
}