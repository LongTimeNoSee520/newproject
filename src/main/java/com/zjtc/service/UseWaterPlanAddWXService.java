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
   * @param auditPersonId 审核人id
   * @param userName      用户名
   * @param id            审核信息id
   * @param auditStatus   审核状态
   * @param auditResult   审核结果
   * @param firstWater    第一水量
   * @param secondWater   第二水量
   * @param auditorName   审核人
   * @param auditorId     审核人id
   * @param businessJson  关联业务json数据(待办相关)
   * @param detailConfig  详情配置文件(待办相关)
   * @param nextNodeId    下一审核环节id
   * @return 响应结果
   */
  ApiResponse audit(String auditPersonId, String userName, String id, String auditStatus,
      String auditResult, Double firstWater, Double secondWater, User user, String auditorName,
      String auditorId, String businessJson, String detailConfig, String nextNodeId);

  /**
   * 修改审核 \ 执行状态,执行后增加核定水量
   *
   * @return 修改是否成功
   */
  boolean update(UseWaterPlanAddWX useWaterPlanAddWX);
}
