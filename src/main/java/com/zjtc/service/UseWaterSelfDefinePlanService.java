package com.zjtc.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterSelfDefinePlan;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;

/**
 * TWUseWaterSelfDefinePlan的服务接口 用水自平计划表
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/4
 */
public interface UseWaterSelfDefinePlanService extends IService<UseWaterSelfDefinePlan> {

  /**
   * 分页
   *
   * @param jsonObject 参数
   * @return 结果集
   */
  ApiResponse queryPage(JSONObject jsonObject, String nodeCode,String userId);

  /*
   * 审核用水自平计划
   * @param id 自平id
   * @param auditPerson 审核人
   * @param auditPersonId 审核人id
   * @param result 审核结果(0:未通过,1:通过)
   * @param auditResult 审核结果(不通过理由)
   * @return 结果集
   */
  ApiResponse audit(User user,String id, String auditPerson, String auditPersonId, String auditStatus,
      String auditResult, String auditorName,
      String auditorId, String businessJson, String detailConfig, String nextNodeId);

  /**
   * 执行
   *
   * @param ids        自平id
   * @param executor   审核人
   * @param executorId 审核人id
   * @return 响应结果
   */
  ApiResponse execute(User user,List<String> ids, String executor, String executorId, String codeNode);

  /**
   * 通过单位编号查询单位信息
   * @param unitCodes
   * @return
   */
  ApiResponse selectByUnitCode(List<String> unitCodes);

  List<Map<String ,Object>> selectAllAudit(User user);
}
