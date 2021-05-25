package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.vo.UseWaterPlanAddWXVO;
import com.zjtc.model.vo.WaterVo;
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
   * @param status 是否审核
   * @return 匹配的条数
   */
  Integer selectCount(
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("executed") String executed,
      @Param("nodeCode") String nodeCode,
      @Param("status") List<String> status,
      @Param("userId") String userId,
      @Param("unitCode") String unitCode);

  /**
   * 分页查询
   *
   * @param currPage    页数
   * @param pageSize    条数
   * @param unitName    单位名称
   * @param userType    用户类型
   * @param executed    是否执行
   * @param nodeCode    节点编码
   * @param status 是否审核
   * @return 数据集
   */
  List<UseWaterPlanAddWXVO> queryList(
      @Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize,
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("executed") String executed,
      @Param("nodeCode") String nodeCode,
      @Param("status") List<String> status,
      @Param("userId") String userId,
      @Param("path") String path,
      @Param("unitCode") String unitCode);


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
   * @return 影响的行数
   */
  int update(@Param("useWaterPlanAddWX") UseWaterPlanAddWX useWaterPlanAddWX);

  WaterVo selectFirstAndSecondWater(@Param("id") String id);

  /**
   * 异常后恢复之前的值
   */

  int recover(
      @Param("id") String id,
      @Param("firstWater") Double firstWater,
      @Param("secondWater") Double secondWater
  );
}