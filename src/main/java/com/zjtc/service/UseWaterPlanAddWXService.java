package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterPlanAddWX;
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
   * @param checkAdjustWater 核定调整水量
   * @return 响应结果
   */
  ApiResponse audit(String auditPersonId, String userName, String id, String auditStatus,
      String auditResult, Double firstWater, Double secondWater, Double checkAdjustWater);
}
