package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterSelfDefinePlan;
import com.zjtc.model.vo.UseWaterSelfDefinePlanVO;
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
   * @param planYear    自平年份-
   * @param unitCode    单位编号-
   * @param nodeCode    节点编码-
   * @param audit 是否审核
   * @param executed    是否执行
   * @return 匹配的条数
   */
  Integer selectCount(
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("planYear") Integer planYear,
      @Param("executed") String executed,
      @Param("unitCode") String unitCode,
      @Param("nodeCode") String nodeCode,
      @Param("audit") List<String> audit,
      @Param("userId") String userId,
      @Param("unitCodeType") String unitCodeType,
      @Param("areaCode") String areaCode);

  /**
   * 分页查询数据
   *
   * @param currPage    页数
   * @param pageSize    条数
   * @param unitName    单位名称
   * @param userType    用户类型
   * @param planYear    自平年份
   * @param executed    是否执行
   * @param unitCode    单位编号
   * @param nodeCode    节点编码
   * @param audit       是否审核
   * @return 结果集
   */
  List<UseWaterSelfDefinePlanVO> queryList(
      @Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize,
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("planYear") Integer planYear,
      @Param("executed") String executed,
      @Param("unitCode") String unitCode,
      @Param("nodeCode") String nodeCode,
      @Param("audit") List<String> audit,
      @Param("userId") String userId,
      @Param("preViewRealPath") String preViewRealPath,
      @Param("unitCodeType") String unitCodeType,
      @Param("areaCode") String areaCode);

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
      @Param("thirdQuarter") Double thirdQuarter,
      @Param("fourthQuarter") Double fourthQuarter,
      @Param("curYearPlan") Double curYearPlan,
      @Param("executorId") String executorId,
      @Param("updateTime") Date updateTime);

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
   *
   * @param id 执行的自平数据id
   * @return 匹配的数据
   */
  UseWaterSelfDefinePlan selectAuditStatus(@Param("id") String id);

  /**
   * 查询数据是否已经被执行
   *
   * @param id 执行的自平数据id
   * @return 匹配的数据
   */
  UseWaterSelfDefinePlan selectExecuted(@Param("id") String id);


  /**
   * 通过单位编号查询单位信息
   * @param unitCode 单位编号
   * @return
   */
  UseWaterSelfDefinePlan selectByUnitCode(@Param("unitCode") String unitCode);
}