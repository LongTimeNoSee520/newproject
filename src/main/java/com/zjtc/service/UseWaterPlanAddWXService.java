package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.User;
import java.util.List;

/**
 * UseWaterPlanAdd的服务接口
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
public interface UseWaterPlanAddWXService extends IService<UseWaterPlanAddWX> {

  /**
   * 分页
   *
   * @param jsonObject 参数
   * @return 响应状态
   */
  ApiResponse queryPage(JSONObject jsonObject, String nodeCode, String userId);

  /**
   * 打印办结单
   *
   * @param ids 数据集
   * @return 响应状态
   */
  ApiResponse printed(List<String> ids);

  /**
   * 审核计划调整申请
   *
   * @param auditPersonId    审核人id
   * @param userName         用户名
   * @param id               审核信息id
   * @param auditStatus      审核状态
   * @param auditResult      审核结果
   * @param firstWater       第一水量
   * @param secondWater      第二水量
   * @return 响应结果
   */
  ApiResponse audit(String auditPersonId, String userName, String id, String auditStatus,
      String auditResult, Double firstWater, Double secondWater, User user,String auditorName,String auditorId,String businessJson,String detailConfig,String nextNodeId);

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
   * @return 修改是否成功
   */
  boolean update(UseWaterPlanAddWX useWaterPlanAddWX);
}
