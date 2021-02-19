package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterPlanAddWXMapper;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.User;
import com.zjtc.model.vo.UseWaterPlanAddWXVO;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.MessageService;
import com.zjtc.service.PlanDailyAdjustmentService;
import com.zjtc.service.SmsService;
import com.zjtc.service.UseWaterPlanAddWXService;
import com.zjtc.service.UseWaterPlanService;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Autowired
  private MessageService messageService;

  @Autowired
  private SmsService smsService;

  @Value("${file.preViewRealPath}")
  private String preViewRealPath;

  @Value("${server.servlet-path}")
  private String contextPath;

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
//    附件展示路径
  String path = preViewRealPath + contextPath + "/";
//    总条数
    Integer total = this.baseMapper
        .selectCount(unitName, userType, executed, nodeCode, auditStatus, userId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    // TODO: 2021/1/8 还没有查询加价费信息
    List<UseWaterPlanAddWXVO> useWaterPlanAdds = this.baseMapper
        .queryList(currPage, pageSize, unitName, userType,
            executed, nodeCode, auditStatus, userId,path);
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
      String auditResult, Double firstWater, Double secondWater, User user, String auditorName,
      String auditorId, String businessJson, String detailConfig, String nextNodeId) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(auditPersonId) || StringUtils.isBlank(userName) || StringUtils
        .isBlank(id) || StringUtils.isBlank(auditStatus)) {
      response.recordError("系统异常");
      return response;
    }
//    审核增加或调整水量申请
    int i = this.baseMapper
        .updateAudit(auditPersonId, userName, id, auditStatus, auditResult, new Date(), firstWater,
            secondWater);
//审核通过后进入办结单审核流程,向办结单中增加数据
    UseWaterPlanAddWX useWaterPlanAddWX = this.baseMapper.selectById(id);
    String messageContent;
    if ("1".equals(auditStatus)) {
      messageContent =
          "用水单位" + useWaterPlanAddWX.getUnitCode() +
              "(" + useWaterPlanAddWX.getUnitName() + ")" +
              "用水计划增加或调整申请,第一季度计划:" + useWaterPlanAddWX.getFirstQuarter() +
              " 方,第二季度计划:" + useWaterPlanAddWX.getSecondQuarter() +
              "方,第三季度计划:" + useWaterPlanAddWX.getThirdQuarter() +
              "方,第四季度计划:" + useWaterPlanAddWX.getFourthQuarter() + "方,年计划水量:"+
              useWaterPlanAddWX.getCurYearPlan()+",审核未通过,已被驳回。";
      messageService
          .add(useWaterPlanAddWX.getNodeCode(), auditPersonId, userName, AuditConstants.NOT_APPROVED, messageContent);
      try {
        smsService.sendMsgToPromoter(user,auditPersonId,userName,messageContent,"计划通知");
      } catch (Exception e) {
       log.error("用水计划增加或调整审核短信发送失败");
      }
    } else if ("2".equals(auditStatus)) {
      messageContent =
          "用水单位" + useWaterPlanAddWX.getUnitCode() +
              "(" + useWaterPlanAddWX.getUnitName() + ")" +
              "用水计划增加或调整申请,第一季度计划:" + useWaterPlanAddWX.getFirstQuarter() +
              " 方,第二季度计划:" + useWaterPlanAddWX.getSecondQuarter() +
              "方,第三季度计划:" + useWaterPlanAddWX.getThirdQuarter() +
              "方,第四季度计划:" + useWaterPlanAddWX.getFourthQuarter() + "方,年计划水量:"+
              useWaterPlanAddWX.getCurYearPlan()+",审核已通过。";
      messageService
          .add(useWaterPlanAddWX.getNodeCode(), auditPersonId, userName, AuditConstants.GET_APPROVED, messageContent);
    }
    if ("2".equals(useWaterPlanAddWX.getAuditStatus())) {
      JSONObject jsonObject = new JSONObject();
//      单位编号
      jsonObject.put("unitCode", useWaterPlanAddWX.getUnitCode());
//      单位名称
      jsonObject.put("unitName", useWaterPlanAddWX.getUnitName());
//      数据来源
      jsonObject.put("dataSources", "1");
//      水表档案号
      jsonObject.put("waterMeterCode", useWaterPlanAddWX.getWaterMeterCode());
//      编制年度
      jsonObject.put("planYear", useWaterPlanAddWX.getPlanYear());
//      调整类型
      jsonObject.put("paperType", useWaterPlanAddWX.getChangeType());
//      一季度水量
      jsonObject.put("firstQuarter", useWaterPlanAddWX.getFirstQuarter());
//      二季度水量
      jsonObject.put("secondQuarter", useWaterPlanAddWX.getSecondQuarter());
//      三季度水量
      jsonObject.put("thirdQuarter", useWaterPlanAddWX.getThirdQuarter());
//      四季度水量
      jsonObject.put("fourthQuarter", useWaterPlanAddWX.getFourthQuarter());
//      第一水量
      jsonObject.put("firstWater", useWaterPlanAddWX.getFirstWater());
//      第二水量
      jsonObject.put("secondWater", useWaterPlanAddWX.getSecondWater());
//      季度
      jsonObject.put("quarter", useWaterPlanAddWX.getChangeQuarter());
//      具体意见
      jsonObject.put("opinions", useWaterPlanAddWX.getAuditResult());
//       审批申请附件id列表\"]没有时传[]
      String[] split = useWaterPlanAddWX.getAuditFileId().split(",");
      List<String> list = new ArrayList<>(16);
      for (String dd : split){
        list.add(dd);
      }
      jsonObject.put("auditFileIds", list);
//       近2月水量凭证附件id列表\"]没有时传[]
      String[] split1 = useWaterPlanAddWX.getWaterProofFileId().split(",");
      List<String> list1 = new ArrayList<>(16);
      for (String ss : split1){
        list1.add(ss);
      }
      jsonObject.put("waterProofFileIds", list1);
//        其他证明材料id列表\"]没有时传[]
      String[] split2 = useWaterPlanAddWX.getOtherFileId().split(",");
      List<String> list2 = new ArrayList<>(16);
      for (String aa : split2){
        list2.add(aa);
      }
      jsonObject.put("otherFileIds", list2);
//      审核人员名称
      jsonObject.put("auditorName", auditorName);
//      审核人员id
      jsonObject.put("auditorId", auditorId);
      // TODO: 2021/1/21 待办相关数据来源,增加办结单表的对应数据
//      关联业务json数据(待办相关)
      jsonObject.put("businessJson",businessJson);
//      详情配置文件(待办相关)
      jsonObject.put("detailConfig",detailConfig);
//     下一审核环节id
      jsonObject.put("nextNodeId", nextNodeId);
      try {
        planDailyAdjustmentService.initiateSettlement(user, jsonObject);
      } catch (Exception e) {
        log.error("转换json数据异常" + e.getMessage());
      }
    }
    if (i > 0) {
      response.setCode(200);
      return response;
    } else {
      response.recordError("审核失败");
      return response;
    }
  }


  @Override
  public boolean update(UseWaterPlanAddWX useWaterPlanAddWX) {
    if (StringUtils.isBlank(useWaterPlanAddWX.getId())) {
      return false;
    }
    int i = this.baseMapper.update(useWaterPlanAddWX);
    return i > 0;
  }
}
