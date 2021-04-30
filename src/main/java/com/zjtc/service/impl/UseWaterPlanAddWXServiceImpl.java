package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.waterBiz.UseWaterPlanAddWXMapper;
import com.zjtc.model.Person;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.User;
import com.zjtc.model.vo.UseWaterPlanAddWXVO;
import com.zjtc.service.FileService;
import com.zjtc.service.MessageService;
import com.zjtc.service.PersonService;
import com.zjtc.service.PlanDailyAdjustmentService;
import com.zjtc.service.SmsService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterPlanAddWXService;
import com.zjtc.service.UseWaterPlanService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
  private FileService fileService;

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

  @Autowired
  private SystemLogService systemLogService;

  @Autowired
  private TodoService todoService;

  @Autowired
  private PersonService personService;


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
    List<String> status = new ArrayList<>();
    String auditStatus = jsonObject.getString("auditStatus");
    if (null != auditStatus) {
      if ("0".equals(auditStatus)) {
        status.add("0");
      } else if ("2".equals(auditStatus)) {
        status.add("1");
        status.add("2");
        status.add("3");
        status.add("4");
      }
    }
    //    是否执行(0:未执行,1:已执行)
    String executed = "";
    if (null != jsonObject.getString("executed")) {
      executed = jsonObject.getString("executed");
    }
//    附件展示路径
    String path = preViewRealPath;
//    总条数
    Integer total = this.baseMapper
        .selectCount(unitName, userType, executed, nodeCode, status, userId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<UseWaterPlanAddWXVO> useWaterPlanAdds = this.baseMapper
        .queryList(currPage, pageSize, unitName, userType,
            executed, nodeCode, status, userId, path);
    UseWaterPlan useWaterPlan = null;
    for (UseWaterPlanAddWXVO useWaterPlanAddWXVO : useWaterPlanAdds) {
      try {
        useWaterPlanAddWXVO = this.getFiles(useWaterPlanAddWXVO, path);
        useWaterPlan = useWaterPlanService
            .selectUseWaterPlanAll(nodeCode, getYear(), useWaterPlanAddWXVO.getUseWaterUnitId(),
                useWaterPlanAddWXVO.getUnitCode());
      } catch (Exception e) {
        log.error("查询用水计划原始数据为空,数据id为:" + useWaterPlanAddWXVO.getUseWaterUnitId());
      }
      try {
        useWaterPlanAddWXVO.setFrontCurYearPlan(useWaterPlan.getCurYearPlan());
        useWaterPlanAddWXVO.setFrontPlanYear(useWaterPlan.getPlanYear());
        useWaterPlanAddWXVO.setFrontFirstQuarter(useWaterPlan.getFirstQuarter());
        useWaterPlanAddWXVO.setFrontSecondQuarter(useWaterPlan.getSecondQuarter());
        useWaterPlanAddWXVO.setFrontThirdQuarter(useWaterPlan.getThirdQuarter());
        useWaterPlanAddWXVO.setFrontFourthQuarter(useWaterPlan.getFourthQuarter());
      } catch (Exception e) {
        log.error("查询用水计划原始数据为空,数据id为:" + useWaterPlanAddWXVO.getUseWaterUnitId());
      }

//      "0"为未审核
      if ("0".equals(useWaterPlanAddWXVO.getAuditStatusCode())) {
        useWaterPlanAddWXVO.setAudit(true);
      }
    }
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
  public ApiResponse printed(List<String> ids, User user) {
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
      systemLogService.logInsert(user, "用水计划调整审核", "打印用水计划调整审核列表", "");
      return response;
    }
    return response;
  }

  //===============
  @Override
  @Transactional(rollbackFor = Exception.class)//多个表中修改数据时，一个出错全部回滚
  public ApiResponse audit(String auditPersonId, String userName, String id, String auditStatus,
      String auditResult, Double firstWater, Double secondWater, User user, String auditorName,
      String auditorId, String businessJson, String detailConfig, String nextNodeId) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(auditPersonId) || StringUtils.isBlank(userName) || StringUtils
        .isBlank(id) || StringUtils.isBlank(auditStatus)) {
      response.recordError("系统异常");
      return response;
    }
    int i = 0;
//    审核增加或调整水量申请
    i = this.baseMapper
        .updateAudit(auditPersonId, userName, id, auditStatus, auditResult, new Date(),
            firstWater,
            secondWater);

    if (i == 0) {
      response.recordError("审核失败");
      return response;
    }
    ApiResponse response1 = null;
//审核通过后进入办结单审核流程,向办结单中增加数据
    UseWaterPlanAddWX useWaterPlanAddWX = this.baseMapper.selectById(id);
    String messageContent = null;
    if ("1".equals(auditStatus)) {

      messageContent =
          "用水单位" + useWaterPlanAddWX.getUnitCode() +
              "(" + useWaterPlanAddWX.getUnitName() + ")" +
              "用水计划增加或调整申请,第一季度计划:" + useWaterPlanAddWX.getFirstQuarter() +
              " 方,第二季度计划:" + useWaterPlanAddWX.getSecondQuarter() +
              "方,第三季度计划:" + useWaterPlanAddWX.getThirdQuarter() +
              "方,第四季度计划:" + useWaterPlanAddWX.getFourthQuarter() + "方,年计划水量:" +
              useWaterPlanAddWX.getCurYearPlan() + ",审核未通过,已被驳回。";
      /**发送消息**/
      messageService
          .add(useWaterPlanAddWX.getNodeCode(), auditPersonId, userName,
              AuditConstants.NOT_APPROVED, messageContent, id);

      try {
        /**发送短信**/
        smsService.sendMsgToPromoter(user, auditPersonId, userName, messageContent, "计划通知");
      } catch (Exception e) {
        log.error("用水计划增加或调整审核短信发送失败");
      }
    } else if ("2".equals(auditStatus)) {

      JSONObject jsonObject = new JSONObject();
//      单位编号
      jsonObject.put("waterPlanWXId", useWaterPlanAddWX.getId());
//      单位编号
      jsonObject.put("unitCode", useWaterPlanAddWX.getUnitCode());
//      单位名称
      jsonObject.put("unitName", useWaterPlanAddWX.getUnitName());
//      数据来源
      jsonObject.put("dataSources", useWaterPlanAddWX.getSource());
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

//==========================================================================
//       审批申请附件id列表\"]没有时传[]
      String[] auditFileIds = useWaterPlanAddWX.getAuditFileId().split(",");
      Map<String, Object> map = new HashMap<>(16);
      List<Map<String, Object>> auditFiles = new ArrayList<>();
      for (String dd : auditFileIds) {
        map.put("id", dd);
        map.put("deleted", "0");
        auditFiles.add(map);
      }
      jsonObject.put("auditFiles", auditFiles);

//       近2月水量凭证附件id列表\"]没有时传[]
      String[] waterProofFileIds = useWaterPlanAddWX.getWaterProofFileId().split(",");
      Map<String, Object> map1 = new HashMap<>(16);
      List<Map<String, Object>> waterProofFiles = new ArrayList<>();
      for (String ss : waterProofFileIds) {
        map1.put("id", ss);
        map1.put("deleted", 0);
        waterProofFiles.add(map1);
      }
      jsonObject.put("waterProofFiles", waterProofFiles);

//        其他证明材料id列表\"]没有时传[]
      String[] otherFileIds = new String[0];
      try {
        otherFileIds = useWaterPlanAddWX.getOtherFileId().split(",");
      } catch (Exception e) {
        log.error("其他附件往办结单中存储异常:" + e.getMessage());
      }
      Map<String, Object> map2 = new HashMap<>(16);
      List<Map<String, Object>> otherFiles = new ArrayList<>();
      for (String aa : otherFileIds) {
        map2.put("id", aa);
        map.put("deleted", "0");
        otherFiles.add(map);
      }
      jsonObject.put("otherFiles", otherFiles);

//==========================================================================

//      下一环节审核人员名称
      jsonObject.put("auditorName", auditorName);
//      下一环节审核人员id
      jsonObject.put("auditorId", auditorId);
      // TODO: 2021/1/21 待办相关数据来源,增加办结单表的对应数据
//      关联业务json数据(待办相关)
      jsonObject.put("businessJson", JSONObject.toJSONString(useWaterPlanAddWX));
//      详情配置文件(待办相关)
      jsonObject.put("detailConfig", detailConfig);
//     下一审核环节id
      jsonObject.put("nextNodeId", nextNodeId);
      try {
        response1 = planDailyAdjustmentService.initiateSettlement(user, jsonObject);
      } catch (Exception e) {
        log.error("转换json数据异常" + e.getMessage());
        response.recordError("系统异常");
        return response;
      }

      messageContent =
          "用水单位" + useWaterPlanAddWX.getUnitCode() +
              "(" + useWaterPlanAddWX.getUnitName() + ")" +
              "用水计划增加或调整申请,第一季度计划:" + useWaterPlanAddWX.getFirstQuarter() +
              " 方,第二季度计划:" + useWaterPlanAddWX.getSecondQuarter() +
              "方,第三季度计划:" + useWaterPlanAddWX.getThirdQuarter() +
              "方,第四季度计划:" + useWaterPlanAddWX.getFourthQuarter() + "方,年计划水量:" +
              useWaterPlanAddWX.getCurYearPlan() + ",审核已通过。";
      messageService
          .add(useWaterPlanAddWX.getNodeCode(), auditPersonId, userName,
              AuditConstants.GET_APPROVED, messageContent, id);

      log.info("审核通过往办结单中增加数据返回状态:" + response1.getCode());
      if (Objects.requireNonNull(response1).getCode() == 200) {
        response.setCode(200);
//      日志记录
        systemLogService.logInsert(user, "用水计划调整审核", "用水计划增加/调整审核", "");
        List<Person> personList1 = null;
        try {
//          调整计划
          List<Person> personList = personService
              .selectPersonByResCode("quarterApply", user.getNodeCode());
          personList1.addAll(personList);
//          增加计划
          List<Person> personList2 = personService
              .selectPersonByResCode("addApply", user.getNodeCode());
          personList1.addAll(personList2);
        } catch (Exception e) {
          log.error("根据资源code查询,资源下所有角色的所有人异常:" + e.getMessage());
        }
        assert personList1 != null;
        for (Person person : personList1) {
          //      取消待办
          try {
            todoService.edit(id, person.getNodeCode(), person.getId());
          } catch (Exception e) {
            log.error("审核人信息为空");
          }
        }
//        //     发起待办
//        List<Person> personList = null;
//        try {
//          personList = personService.selectPersonAll(auditorId);
//        } catch (Exception e) {
//          log.error("查询下一环节审核人员为空:" + e.getMessage());
//        }
//        assert personList != null;
//        for (Person person : personList) {
//          todoService.add(
//              useWaterPlanAddWX.getId(),
//              user,
//              person.getId(),
//              person.getUserName(),
//              messageContent,
//              JSONObject.toJSONString(useWaterPlanAddWX),
//              detailConfig,
//              AuditConstants.END_PAPER_TODO_TYPE);
//        webSocket消息推送
//          webSocketUtil.pushWaterNews(person.getNodeCode(), person.getId());
//        }
        return response;
      } else if (Objects.requireNonNull(response1).getCode() == 500) {
        log.info(response1.getMessage() + ",数据id为:" + useWaterPlanAddWX.getId());
        response.recordError(response1.getMessage());
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return response;
      }
    }
    return response;
  }

  @Override
  public boolean update(UseWaterPlanAddWX useWaterPlanAddWX, User user) {
    if (StringUtils.isBlank(useWaterPlanAddWX.getId())) {
      return false;
    }
    int i = this.baseMapper.update(useWaterPlanAddWX);
    systemLogService.logInsert(user, "用水计划调整审核", "修改审核/执行状态", "");
    return i > 0;
  }

  //获取当前年份
  public static Integer getYear() {
    Calendar date = Calendar.getInstance();
    String year = String.valueOf(date.get(Calendar.YEAR));
    return Integer.valueOf(year);
  }


  /**
   * 查询附件
   */
  private UseWaterPlanAddWXVO getFiles(UseWaterPlanAddWXVO waterPlanAddWXVO, String path) {
    String auditFileId = waterPlanAddWXVO.getAuditFileId();
    String otherFileId = waterPlanAddWXVO.getOtherFileId();
    String waterProofFileId = waterPlanAddWXVO.getWaterProofFileId();
    if (StringUtils.isNotBlank(auditFileId)) {
      waterPlanAddWXVO.setAuditFileIds(
          fileService.findByIds(Arrays.asList(auditFileId.split(",")), path));
    }
    if (StringUtils.isNotBlank(otherFileId)) {
      waterPlanAddWXVO.setOtherFileIds(
          fileService.findByIds(Arrays.asList(otherFileId.split(",")), path));
    }
    if (StringUtils.isNotBlank(waterProofFileId)) {
      waterPlanAddWXVO.setWaterProofFileIds(
          fileService.findByIds(Arrays.asList(waterProofFileId.split(",")), path));
    }
    return waterPlanAddWXVO;
  }

  @Override
  public UseWaterPlanAddWX selectByIdAll(String id) {
    return this.baseMapper.selectById(id);
  }

  @Override
  public UseWaterPlanAddWXVO selectNowPlan(String id) {
    UseWaterPlanAddWXVO useWaterPlanAddWXVO = new UseWaterPlanAddWXVO();
    UseWaterPlanAddWX useWaterPlanAddWX = this.baseMapper.selectById(id);

    UseWaterPlan useWaterPlan = useWaterPlanService
        .selectUseWaterPlanAll(useWaterPlanAddWX.getNodeCode(), getYear(),
            useWaterPlanAddWX.getUseWaterUnitId(), useWaterPlanAddWX.getUnitCode());

    //调整后
    useWaterPlanAddWXVO.setCurYearPlan(useWaterPlanAddWX.getCurYearPlan());
    useWaterPlanAddWXVO.setPlanYear(useWaterPlanAddWX.getPlanYear());
    useWaterPlanAddWXVO.setFirstQuarter(useWaterPlanAddWX.getFirstQuarter());
    useWaterPlanAddWXVO.setSecondQuarter(useWaterPlanAddWX.getSecondQuarter());
    useWaterPlanAddWXVO.setThirdQuarter(useWaterPlanAddWX.getThirdQuarter());
    useWaterPlanAddWXVO.setFourthQuarter(useWaterPlanAddWX.getFourthQuarter());
//    当前
    try {
      useWaterPlanAddWXVO.setFrontCurYearPlan(useWaterPlan.getCurYearPlan());
      useWaterPlanAddWXVO.setFrontPlanYear(useWaterPlan.getPlanYear());
      useWaterPlanAddWXVO.setFrontFirstQuarter(useWaterPlan.getFirstQuarter());
      useWaterPlanAddWXVO.setFrontSecondQuarter(useWaterPlan.getSecondQuarter());
      useWaterPlanAddWXVO.setFrontThirdQuarter(useWaterPlan.getThirdQuarter());
      useWaterPlanAddWXVO.setFrontFourthQuarter(useWaterPlan.getFourthQuarter());
    } catch (Exception e) {
      log.error("查询用水计划原始数据为空,数据id为:" + useWaterPlanAddWXVO.getUseWaterUnitId());
    }
    return useWaterPlanAddWXVO;
  }
}
