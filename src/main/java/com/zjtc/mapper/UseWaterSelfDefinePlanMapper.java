package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterSelfDefinePlan;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TWUseWaterSelfDefinePlan的Dao接口 用水自平计划表
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/4
 */
@Mapper
public interface UseWaterSelfDefinePlanMapper extends BaseMapper<UseWaterSelfDefinePlan> {

  /**
   * @param unitName    单位名称-
   * @param userType    用户类型-
   * @param areaCode    编号开头-
   * @param beginYear   开始年份-
   * @param endYear     结束年份-
   * @param unitCode    单位编号-
   * @param nodeCode    节点编码-
   * @param auditStatus 是否审核
   * @param executed    是否执行
   * @return 匹配的条数
   */
  Integer selectCount(
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("areaCode") String areaCode,
      @Param("beginYear") Integer beginYear,
      @Param("endYear") Integer endYear,
      @Param("executed") String executed,
      @Param("unitCode") String unitCode,
      @Param("nodeCode") String nodeCode,
      @Param("auditStatus") String auditStatus,
      @Param("userId") String userId);

  /**
   * 分页查询数据
   *
   * @param currPage    页数
   * @param pageSize    条数
   * @param unitName    单位名称
   * @param userType    用户类型
   * @param areaCode    编号开头
   * @param beginYear   开始年份
   * @param endYear     结束年份
   * @param executed    是否执行
   * @param unitCode    单位编号
   * @param nodeCode    节点编码
   * @param auditStatus 是否审核
//     * @param rank        根据单位编号排序
   * @return 结果集
   */
  List<UseWaterSelfDefinePlan> queryList(
      @Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize,
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("areaCode") String areaCode,
      @Param("beginYear") Integer beginYear,
      @Param("endYear") Integer endYear,
      @Param("executed") String executed,
      @Param("unitCode") String unitCode,
      @Param("nodeCode") String nodeCode,
      @Param("auditStatus") String auditStatus,
      @Param("userId") String userId);

  /**
   * 执行时修改执行信息
   *
   * @param id         id
   * @param executor   执行人
   * @param executorId 执行人Id
   */
  int updateExecuteData(
      @Param("id") String id,
      @Param("executor") String executor,
      @Param("executorId") String executorId,
      @Param("executeTime") Date executeTime);


  /**
   * 根据自平id查询出关联的附件id
   *
   * @param id 自平id
   * @return 附件id
   */
  String selectFileId(@Param("id") String id);

  /**
   * 修改用水计划表季度水量
   *
   * @param id            用水计划表id
   * @param firstQuarter  第一季度
   * @param secondQuarter 第二季度
   * @param thirdQuarter  第三季度
   * @param fourthQuarter 第四季度
   * @param executorId    调整用户Id
   * @param updateTime    调整时间
   * @return 影响的行数
   */
  int updateUseWaterPlanWater(
      @Param("id") String id,
      @Param("firstQuarter") Double firstQuarter,
      @Param("secondQuarter") Double secondQuarter,
      @Param("thirdQuarter")Double thirdQuarter,
      @Param("fourthQuarter")Double fourthQuarter,
      @Param("curYearPlan")Double curYearPlan,
      @Param("executorId")String executorId,
      @Param("updateTime")Date updateTime);

  /**
   * 匹配用水计划表里的那条数据
   *
   * @param nodeCode       节点编码
   * @param useWaterUnitId 单位id
   * @param unitName       单位名称
   * @param unitCode       单位编号
   * @param planYear       年度
   * @return 计划用水实体
   */
  List<UseWaterPlan> selectWaterPlan(
      @Param("nodeCode") String nodeCode,
      @Param("useWaterUnitId") String useWaterUnitId,
      @Param("unitName") String unitName,
      @Param("unitCode") String unitCode,
      @Param("planYear") Integer planYear);

  /**
   * 查询执行的数据是否为未审核和审核不通过
   * @param id 执行的自平数据id
   * @return 匹配的数据
   */
  UseWaterSelfDefinePlan selectAuditStatus(@Param("id") String id);

  /**
   * 查询数据是否已经被执行
   * @param id 执行的自平数据id
   * @return 匹配的数据
   */
  UseWaterSelfDefinePlan selectExecuted(@Param("id") String id);
}