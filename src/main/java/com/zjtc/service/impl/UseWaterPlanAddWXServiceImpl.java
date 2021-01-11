package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterPlanAddWXMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.UseWaterPlanAddWXService;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
      String auditResult) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(auditPersonId) || StringUtils.isBlank(userName) || StringUtils
        .isBlank(id) || StringUtils.isBlank(auditStatus)) {
      response.recordError("系统异常");
      return response;
    }
//    审核调整申请
    int i = this.baseMapper
        .updateAudit(auditPersonId, userName, id, auditStatus, auditResult, new Date());
//  审核通过后向办结单中增加数据,2为审核通过,只能是通过审核后才进入办结单流程
    UseWaterPlanAddWX useWaterPlanAddWX = this.baseMapper.selectById(id);
    EndPaper endPaper = new EndPaper();
    if ("2".equals(useWaterPlanAddWX.getAuditStatus())) {
      endPaper.setId(UUID.randomUUID().toString().replace("-", ""));
//      节点编码
      endPaper.setNodeCode(useWaterPlanAddWX.getNodeCode());
//      单位id
      endPaper.setUseWaterUnitId(useWaterPlanAddWX.getUseWaterUnitId());
//      单位名称
      endPaper.setUnitName(useWaterPlanAddWX.getUnitName());
//      单位编号
      endPaper.setUnitCode(useWaterPlanAddWX.getUnitCode());
//      水表档案号
      endPaper.setWaterMeterCode(useWaterPlanAddWX.getWaterMeterCode());
//      办结单类型
      // TODO: 2021/1/9   不清楚类型
//      endPaper.setPaperType()
//      数据来源(1:网上申报,2:现场申报)
      endPaper.setDataSources("1");
//      创建时间
      endPaper.setCreateTime(useWaterPlanAddWX.getCreateTime());
//      经办人（创建人）
      endPaper.setCreaterId(useWaterPlanAddWX.getAuditPersonId());
//      调整年份
      endPaper.setPlanYear(useWaterPlanAddWX.getPlanYear());
//      调整季度
      endPaper.setChangeQuarter(useWaterPlanAddWX.getChangeQuarter());
//      本年计划（当前年计划）
      endPaper.setCurYearPlan(useWaterPlanAddWX.getCurYearPlan());
//      第一水量
      endPaper.setFirstWater(useWaterPlanAddWX.getFirstWater());
//      第二水量
      endPaper.setSecondWater(useWaterPlanAddWX.getSecondWater());
//      增加水量
      // TODO: 2021/1/9
//      endPaper.setAddNumber()
//      是否在年计划上增加
      endPaper.setYear("1");
//      创建类型
//      endPaper.setCreateType()
//      第一季度计划
      endPaper.setFirstQuarter(useWaterPlanAddWX.getFirstQuarter());
//      第二季度计划
      endPaper.setSecondQuarter(useWaterPlanAddWX.getSecondQuarter());
//      第三季度计划
      endPaper.setThirdQuarter(useWaterPlanAddWX.getThirdQuarter());
//      第四季度计划
      endPaper.setFourthQuarter(useWaterPlanAddWX.getFourthQuarter());
//      是否确认
      endPaper.setConfirmed(useWaterPlanAddWX.getConfirmed());
//      确认时间
      endPaper.setConfirmTime(useWaterPlanAddWX.getConfirmTime());
//      是否审核
      endPaper.setAuditStatus(useWaterPlanAddWX.getAuditStatus());
//      是否执行
      endPaper.setExecuted(useWaterPlanAddWX.getExecuted());
//      审批申请附件id
      endPaper.setAuditFileId(useWaterPlanAddWX.getAuditFileId());
//      近2月水量凭证附件id
      endPaper.setWaterProofFileId(useWaterPlanAddWX.getWaterProofFileId());
//      其他证明材料
      endPaper.setOtherFileId(useWaterPlanAddWX.getOtherFileId());
       endPaperService.insert(endPaper);
    }
    if (i > 0){
      response.setCode(200);
      return response;
    }else {
      response.recordError("审核失败");
      return response;
    }
  }
}