package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAddWX;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * UseWaterPlanAdd的Dao接口
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
@Mapper
public interface UseWaterPlanAddWXMapper extends BaseMapper<UseWaterPlanAddWX> {


  /**
   * 统计条数
   *
   * @param unitName    单位名称
   * @param userType    用户类型
   * @param executed    是否执行
   * @param nodeCode    节点编码
   * @param auditStatus 是否审核
   * @return 匹配的条数
   */
  Integer selectCount(
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("executed") String executed,
      @Param("nodeCode") String nodeCode,
      @Param("auditStatus") String auditStatus,
      @Param("userId") String userId);

  /**
   * 分页查询
   *
   * @param currPage    页数
   * @param pageSize    条数
   * @param unitName    单位名称
   * @param userType    用户类型
   * @param executed    是否执行
   * @param nodeCode    节点编码
   * @param auditStatus 是否审核
   * @return 数据集
   */
  List<UseWaterPlanAddWX> queryList(
      @Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize,
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("executed") String executed,
      @Param("nodeCode") String nodeCode,
      @Param("auditStatus") String auditStatus,
      @Param("userId") String userId);


  /**
   * 修改用水计划调整数据是否打印状态
   *
   * @param id 打印的主键id
   * @return 响应行数
   */
  int updatePrinted(@Param("id") String id);

  /**
   * 审核调整申请
   *
   * @param auditPersonId    审核人id
   * @param userName         审核人
   * @param id               审核信息id
   * @param auditStatus      审核状态
   * @param auditResult      审核结果
   * @param firstWater       第一水量
   * @param secondWater      第二水量
   * @return 响应行数
   */
  int updateAudit(
      @Param("auditPersonId") String auditPersonId,
      @Param("userName") String userName,
      @Param("id") String id,
      @Param("auditStatus") String auditStatus,
      @Param("auditResult") String auditResult,
      @Param("auditTime") Date auditTime,
      @Param("firstWater") Double firstWater,
      @Param("secondWater") Double secondWater
  );

  /**
   * 修改审核 \ 执行状态,执行后增加核定水量
//   * @param id 修改的主键id
//   * @param auditStatus 审核状态
//   * @param executed 执行状态
//   * @param checkAdjustWater  核定调整计划(定额)
//   * @param firstQuarterQuota 第一季度计划（定额）
//   * @param secondQuarterQuota 第二季度计划（定额）
//   * @param thirdQuarterQuota 第三季度计划（定额）
//   * @param fourthQuarterQuota 第四季度计划（定额）
   *
   */
  int update(@Param("useWaterPlanAddWX") UseWaterPlanAddWX useWaterPlanAddWX);


  /**
   * 查询对应数据的用水计划表信息
   * @param nodeCode 节点编码
   * @param unitCode 单位编码
   * @param planYear 年份
   * @return 用水计划表实体
   */
  UseWaterPlan selectEndPaper(@Param("nodeCode") String nodeCode,@Param("unitCode") String unitCode,@Param("planYear") Integer planYear);
}