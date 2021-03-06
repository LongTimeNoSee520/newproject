package com.zjtc.mapper.waterBiz;

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
   * 编制初始化：老户
   *
   * @param year 年份
   * @param userType 用户类型
   * @param unitCodeStart 单位编号开头[单位批次]
   * @param userId 用户id
   * @param nodeCode 节点编码
   */
  List<Map<String, Object>> initPlanOld(@Param("year") int year,
      @Param("unitCodeType") String userType, @Param("unitCodeStart") String unitCodeStart,
      @Param("userId") String userId, @Param("nodeCode") String nodeCode
  );

  /**
   * 编制初始化：新户
   *
   * @param year 年份
   * @param userId 用户id
   * @param nodeCode 节点编码
   * @param ParamOne  计算第三季度的年份参数
   * @param ParamTwo  计算第四季度的年份参数
   * @param threeWaterMonth 计算第三季度的水量参数
   * @param fourWaterMonth 计算第四季度的水量参数
   */
  List<Map<String, Object>> initPlanNew(@Param("year") int year,
      @Param("userId") String userId, @Param("nodeCode") String nodeCode,
      @Param("ParamOne") int ParamOne, @Param("ParamTwo") int ParamTwo,
      @Param("threeWaterMonth") String threeWaterMonth,
      @Param("fourWaterMonth") String fourWaterMonth
  );


  /**
   * 编制查询：老户
   *
   * @param year 年份
   * @param userType 用户类型
   * @param unitCodeStart 单位编号开头
   * @param userId 用户id
   * @param nodeCode 节点编码
   */
  List<Map<String, Object>> nowYearPlanOld(@Param("year") int year,
      @Param("userType") String userType,
      @Param("unitCodeStart") String unitCodeStart, @Param("userId") String userId,
      @Param("nodeCode") String nodeCode);

  /**
   * 编制查询：新户 满足条件：原始计划表中查询不到数据,2:包含基础算法中新户号
   *
   * @param year 年份
   * @param userId 用户id
   * @param nodeCode 节点编码
   */
  List<Map<String, Object>> nowYearPlanNew(@Param("year") int year,
      @Param("userId") String userId,
      @Param("nodeCode") String nodeCode);
}