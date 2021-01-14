package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterPlanAddWXMapper;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.PlanDailyAdjustmentService;
import com.zjtc.service.UseWaterPlanAddWXService;
import com.zjtc.service.UseWaterPlanService;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/8
 */
@Service
@Slf4j
public class UseWaterPlanAddWXServiceImpl extends
    ServiceImpl<UseWaterPlanAddWXMapper, UseWaterPlanAddWX> implements
    UseWaterPlanAddWXService {

  @Autowired
  private EndPaperService endPaperService;

  @Autowired
  private UseWaterPlanService useWaterPlanService;

  @Autowired
  private PlanDailyAdjustmentService planDailyAdjustmentService;
  @Override
  public ApiResponse queryPage(JSONObject jsonObject, String nodeCode, String userId) {
    ApiResponse response = new ApiResponse();
    Map<String, Object> map = new LinkedHashMap<>(10);
//    页数
    Integer currPage = jsonObject.getInteger("current");
//    条数
    Integer pageSize = jsonObject.getInteger("size");
//    单位名称
    String unitName = "";
    if (null != jsonObject.getString("unitName")) {
      unitName = jsonObject.getString("unitName");
    }
//    用户类型(截取的是3-4位)
    String userType = "";
    if (null != jsonObject.getString("userType")) {
      userType = jsonObject.getString("userType");
    }
    //    是否审核(0:未审核,1:已审核)
    String auditStatus = "";
    if (null != jsonObject.getString("auditStatus")) {
      auditStatus = jsonObject.getString("auditStatus");
    }
    //    是否执行(0:未执行,1:已执行)
    String executed = "";
    if (null != jsonObject.getString("executed")) {
      executed = jsonObject.getString("executed");
    }

//    总条数
    Integer total = this.baseMapper
        .selectCount(unitName, userType, executed, nodeCode, auditStatus, userId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    // TODO: 2021/1/8 还没有查询加价费信息
    // TODO: 2021/1/14 当前计划对比查询待调整 
    List<UseWaterPlanAddWX> useWaterPlanAdds = this.baseMapper
        .queryList(currPage, pageSize, unitName, userType,
            executed, nodeCode, auditStatus, userId);
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", useWaterPlanAdds);
    response.setCode(200);
    response.setData(map);
    return response;
  }

  @Override
  public ApiResponse printed(List<String> ids) {
    ApiResponse response = new ApiResponse();
    if (ids.isEmpty()) {
      response.recordError("系统异常");
      return response;
    }
    int i = 0;
    for (String id : ids) {
//      修改是否打印状态
      i = this.baseMapper.updatePrinted(id);
    }
    if (i > 0) {
      response.setCode(200);
      return response;
    } else {
      response.recordError("打印失败");
      return response;
    }
  }

  @Override
  public ApiResponse audit(String auditPersonId, String userName, String id, String auditStatus,
      String auditResult,Double firstWater,Double secondWater,Double checkAdjustWater) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(auditPersonId) || StringUtils.isBlank(userName) || StringUtils
        .isBlank(id) || StringUtils.isBlank(auditStatus)) {
      response.recordError("系统异常");
      return response;
    }
//    审核增加或调整水量申请
    int i = this.baseMapper
        .updateAudit(auditPersonId, userName, id, auditStatus, auditResult, new Date(),firstWater,secondWater,checkAdjustWater);
//审核通过后进去用户确认流程,确认在微信端确认

    if (i > 0) {
      response.setCode(200);
      return response;
    } else {
      response.recordError("审核失败");
      return response;
    }
  }
}
