package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.constant.SmsConstants;
import com.zjtc.base.constant.SystemConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.DictUtils;
import com.zjtc.base.util.HttpUtil;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.base.util.WebSocketUtil;
import com.zjtc.mapper.waterBiz.EndPaperMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.Todo;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.User;
import com.zjtc.model.vo.EndPaperVO;
import com.zjtc.model.vo.SendListVO;
import com.zjtc.service.DictItemService;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.FileService;
import com.zjtc.service.FlowExampleService;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.MessageService;
import com.zjtc.service.PlanDailyAdjustmentService;
import com.zjtc.service.SmsSendService;
import com.zjtc.service.SmsService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterPlanAddService;
import com.zjtc.service.UseWaterPlanAddWXService;
import com.zjtc.service.WaterUsePayInfoService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lianghao
 * @date 2021/01/09
 */
@Service
public class EndPaperServiceImpl extends ServiceImpl<EndPaperMapper, EndPaper> implements
    EndPaperService {

  @Autowired
  private PlanDailyAdjustmentService planDailyAdjustmentService;

  @Autowired
  private UseWaterPlanAddService useWaterPlanAddService;

  @Autowired
  private UseWaterPlanAddWXService useWaterPlanAddWXService;

  @Autowired
  private TodoService todoService;

  @Autowired
  private FlowNodeInfoService flowNodeInfoService;

  @Autowired
  private FlowProcessService flowProcessService;

  @Autowired
  private FlowExampleService flowExampleService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private WaterUsePayInfoService waterUsePayInfoService;

  @Autowired
  private SmsService smsService;

  @Autowired
  private DictItemService dictItemService;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private WebSocketUtil webSocketUtil;

  @Autowired
  private SystemLogService systemLogService;

  @Autowired
  private SmsSendService smsSendService;

  @Autowired
  private DictUtils dictUtils;

  @Autowired
  private FileService fileService;

  @Value("${file.preViewRealPath}")
  private String preViewRealPath;

  @Value("${server.servlet-path}")
  private String contextPath;

  @Value("${waterReport.reportUrl}")
  private String reportUrl;

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {

    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    //  String nodeCode = user.getNodeCode();//节点编码
    String userId = user.getId();
    String unitCode = jsonObject.getString("unitCode");//单位编号
    String unitName = jsonObject.getString("unitName");//单位名称
    String executed = jsonObject.getString("executed");//是否已执行
    String waterMeterCode = jsonObject.getString("waterMeterCode");//水表档案号
    Date applyTimeStart = jsonObject.getDate("applyTimeStart");//申请时间起始
    Date applyTimeEnd = jsonObject.getDate("applyTimeEnd");//申请时间截止

    Map<String, Object> map = new HashMap();
    map.put("current", current);
    map.put("size", size);
//    map.put("nodeCode", nodeCode);
    if (StringUtils.isNotBlank(jsonObject.getString("nodeCode"))) {
      map.put("nodeCode", jsonObject.getString("nodeCode"));
    } else {
      map.put("nodeCode", user.getNodeCode());
    }
    map.put("userId", userId);
    map.put("preViewRealPath", preViewRealPath + contextPath + "/");
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(executed)) {
      map.put("executed", executed);
    }
    if (StringUtils.isNotBlank(waterMeterCode)) {
      map.put("waterMeterCode", waterMeterCode);
    }
    if (null != applyTimeStart) {
      map.put("applyTimeStart", applyTimeStart);
    }
    if (null != applyTimeEnd) {
      map.put("applyTimeEnd", applyTimeEnd);
    }

    /**查出满足条件的共有多少条*/
    int num = this.baseMapper.queryNum(map);
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("total", num);//满足条件的总条数
    result.put("size", size);//每页条数
    result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
    result.put("current", current);//当前页

    /**查出满足条件的数据*/
    List<EndPaperVO> records = this.baseMapper.queryPage(map);
    for (EndPaperVO paperVO : records) {
      /**查询流程信息*/
      paperVO.setAuditMessages(
          flowProcessService.queryAuditList(paperVO.getId(), paperVO.getNodeCode()));
      /** 查询附件 **/
      paperVO = this.getFiles(paperVO, preViewRealPath + contextPath + "/");
      /**查询是否需要当前登录人员审核*/
      int i = flowProcessService.ifNeedAudit(paperVO.getId(), userId);
      if (i == 0) {
        paperVO.setNeedAudit(false);
      } else {
        paperVO.setNeedAudit(true);
      }
      /**查询字典项名称*/
      if (StringUtils.isNotBlank(paperVO.getPaperType())) {
        paperVO.setPaperTypeName(
            dictUtils.getDictItemName("changeType", paperVO.getPaperType(), paperVO.getNodeCode()));
      }
    }
    result.put("records", records);
    return result;
  }

  @Override
  public ApiResponse cancelSettlement(List<String> ids) {
    ApiResponse response = new ApiResponse();
    /**根据id查询*/
    List<EndPaper> endPapers = new ArrayList<>(this.listByIds(ids));
    for (EndPaper endPaper : endPapers) {
      /**处于审核中的办结单不能撤销*/
      if (!"0".equals(endPaper.getAuditStatus())) {//0为办结单提交审核还未经过下一环节审核的状态
        //如果不是刚提交未审核状态，则不能撤销
        response.recordError("处于审核中或者审核通过的办结单不能撤销");
        return response;
      } else {
        /**根据单位编号更新是否存在办结单状态为否*/
        planDailyAdjustmentService
            .updateExistSettlement("0", endPaper.getUnitCode(), endPaper.getNodeCode(),
                endPaper.getPlanYear());
        /**更新撤销状态和时间*/
        endPaper.setRescinded("1");
        endPaper.setRescindTime(new Date());
        this.updateById(endPaper);
        /**删除待办数据*/
        todoService.deleteByBusinessId(endPaper.getId());
      }
    }
    return response;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void examineSettlement(User user, JSONObject jsonObject) throws Exception {
    String id = jsonObject.getString("id");
    Double firstQuarter = jsonObject.getDouble("firstQuarter");
    Double secondQuarter = jsonObject.getDouble("secondQuarter");
    Double thirdQuarter = jsonObject.getDouble("thirdQuarter");
    Double fourthQuarter = jsonObject.getDouble("fourthQuarter");
    Double firstWater = jsonObject.getDouble("firstWater");
    Double secondWater = jsonObject.getDouble("secondWater");
    String addWay = jsonObject.getString("addWay");//加计划的方式：1-平均，2-最高
    String quarter = jsonObject.getString("quarter");
    Boolean year = jsonObject.getBoolean("year"); //是否在年计划上增加
    Double addNumber = jsonObject.getDouble("addNumber");
    String auditBtn = jsonObject.getString("auditBtn");
    String opinions = jsonObject.getString("opinions");//意见
    String auditorName = jsonObject.getString("auditorName");//审核人员名称
    String auditorId = jsonObject.getString("auditorId");//审核人员id
//    String businessJson = jsonObject.getString("businessJson");
    String detailConfig = jsonObject.getString("detailConfig");

    EndPaper endPaper = this.baseMapper.findById(id);
    endPaper = this.getFiles(endPaper, preViewRealPath);
    endPaper.setAuditMessages(
        flowProcessService.queryAuditList(endPaper.getId(), endPaper.getNodeCode()));
    if (null != year && year) {
      /**选择了年计划*/
      endPaper.setAlgorithmRules("1");
    }
    endPaper.setFirstWater(firstWater);
    endPaper.setSecondWater(secondWater);
    endPaper.setAddWay(addWay);
    endPaper.setChangeQuarter(quarter);
    endPaper.setAddNumber(addNumber);
    //查询审核流程下一环节信息
    List<Map<String, Object>> hasNext = flowNodeInfoService
        .nextAuditRole(endPaper.getNextNodeId(), id, user.getNodeCode(), auditBtn);
    //获取当前环节的审核操作记录
    FlowProcess flowProcess = flowProcessService.getLastData(user.getNodeCode(), endPaper.getId());
    if (hasNext.isEmpty()) { //审核流程结束(没有下一环节)
      //设置办结单审核下一环节id为""
      endPaper.setNextNodeId("");
      if ("1".equals(auditBtn)) {//本次审核通过
        //审核操作记录审核状态更新
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
        //设置办结单审核状态
        endPaper.setAuditStatus("1");//通过(本次通过且没有下一环节)
        /**超额向市级发待办，并且办结单状态任然设为审核中*/
        endPaper = this.report(user, endPaper);
        if ("1".equals(endPaper.getDataSources())) {//网上申报的办结单
          /**网上申报的 需要通知用户到微信端确认(现场的不发通知)*/
          String messageContent1 = "";
          if ("0".equals(endPaper.getPaperType())) {//调整计划
            messageContent1 = "[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")"
                + "申请调整计划，调整后4个季度水量：第一季度" + firstQuarter + "方,第二季度" + secondQuarter + "方，第三季度"
                + thirdQuarter + "方,第四季度" + fourthQuarter + "方]审核已通过,请到微信端确认。";
          } else if ("1".equals(endPaper.getPaperType())) {//增加计划
            messageContent1 =
                "[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
                    + addNumber + "方(第一水量"
                    + firstWater + "方，第二水量" + secondWater + "方)]审核已通过,请到微信端确认。";
          }
          if (!"2".equals(endPaper.getAuditStatus())) {//没有超额
            UseWaterPlanAddWX waterPlanAddWX = new UseWaterPlanAddWX();
            waterPlanAddWX.setId(endPaper.getWaterPlanWXId());
            //只有通过时(且审核流程走完)，不通过则不修改
            waterPlanAddWX.setAuditStatus("4");//微信端提交审核通过后办结单审核也通过
            waterPlanAddWX.setAddNumber(addNumber);
            useWaterPlanAddWXService.update(waterPlanAddWX, user);
            /**新增通知给用水单位*/
            messageService.messageToUnit(endPaper.getUnitCode(), messageContent1,
                AuditConstants.END_PAPER_TODO_TITLE);
            /**短信通知给用水单位*/
            smsService.sendMsgToUnit(user, endPaper.getUnitCode(), messageContent1, "调整结果通知");
            // webSocket推送到公共服务端
            webSocketUtil.pushPublicNews(endPaper.getNodeCode(), endPaper.getUnitCode());
          }
        }
        /**新增通知给发起人*/
        //查询 流程的发起人
        FlowProcess firstProcess = flowProcessService.selectFirstRecords(id);
        //通知表新增
        String messageContent = "";
        if ("0".equals(endPaper.getPaperType())) {//调整计划
          messageContent = "您发起的[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")"
              + "申请调整计划，调整后4个季度水量：第一季度" + firstQuarter + "方,第二季度" + secondQuarter + "方，第三季度"
              + thirdQuarter + "方,第四季度" + fourthQuarter + "方]办结单审核已通过。";
        } else if ("1".equals(endPaper.getPaperType())) {//增加计划
          messageContent =
              "您发起的[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
                  + addNumber + "方(第一水量"
                  + firstWater + "方，第二水量" + secondWater + "方)]办结单审核已通过。";
        }
        if (!"2".equals(endPaper.getAuditStatus())) {
          messageService
              .add(user.getNodeCode(), firstProcess.getOperatorId(), firstProcess.getOperator(),
                  AuditConstants.END_PAPER_MESSAGE_TYPE, messageContent, id);
          /**短信通知给发起人*/
          smsService
              .sendMsgToPromoter(user, firstProcess.getOperatorId(), firstProcess.getOperator(),
                  messageContent, "计划通知");
          //webSocket推送到前端
          webSocketUtil.pushWaterNews(firstProcess.getNodeCode(), firstProcess.getOperatorId());
        }
      }
      if ("0".equals(auditBtn)) {//本次审核不通过(没有下一环节则是回到提交人，由提交人本人审核的本条数据，不发起通知)
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
        endPaper.setAuditStatus("0");//0发起办结节点状态(已有审核记录的只要不是最终环节通过都为审核中状态)
        // 没有下一环节则是回到提交人，由提交人本人审核的本条数据，不发起通知和发短信给提交人
        /**通知发给用水单位*/
        String messageContent = "";
        if ("0".equals(endPaper.getPaperType())) {//调整计划
          messageContent = "[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")"
              + "申请调整计划，调整后4个季度水量：第一季度" + firstQuarter + "方,第二季度" + secondQuarter + "方，第三季度"
              + thirdQuarter + "方,第四季度" + fourthQuarter + "方]审核未通过。";
        } else if ("1".equals(endPaper.getPaperType())) {//增加计划
          messageContent =
              "[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
                  + addNumber + "方(第一水量"
                  + firstWater + "方，第二水量" + secondWater + "方)]审核未通过。";
        }
        messageService.messageToUnit(endPaper.getUnitCode(), messageContent,
            AuditConstants.END_PAPER_TODO_TITLE);
        /**短信通知给用水单位*/
        smsService.sendMsgToUnit(user, endPaper.getUnitCode(), messageContent, "计划通知");
        // webSocket推送到公共服务端
        webSocketUtil.pushPublicNews(endPaper.getNodeCode(), endPaper.getUnitCode());
      }
      //修改待办状态
      todoService.edit(endPaper.getId(), user.getNodeCode(), user.getId());
      //修改实例表数据
      flowExampleService.edit(user.getNodeCode(), endPaper.getId());
      /**根据单位编号更新是否存在办结单状态为否*/
      planDailyAdjustmentService
          .updateExistSettlement("0", endPaper.getUnitCode(), endPaper.getNodeCode(),
              endPaper.getPlanYear());
    } else if (!hasNext.isEmpty()) {//审核流程继续
      //修改当前办结单下一环节id
      endPaper.setNextNodeId(hasNext.get(0).get("flowNodeId").toString());
      //修改办结单状态为2处于审核中
      endPaper.setAuditStatus("2");
      //当前操作记录修改状态
      if ("0".equals(auditBtn)) {
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
      }
      if ("1".equals(auditBtn)) {
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
      }
      //新增审核流程下一环节记录
      flowProcessService.add(user.getNodeCode(), endPaper.getId(), auditorName, auditorId);
      //修改待办状态
      todoService.edit(endPaper.getId(), user.getNodeCode(), user.getId());
      //新增一条待办
      String todoContent = "";
      if ("0".equals(endPaper.getPaperType())) {//调整计划
        todoContent = "用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")"
            + "申请调整计划，调整后4个季度水量：第一季度" + firstQuarter + "方,第二季度" + secondQuarter + "方，第三季度"
            + thirdQuarter + "方,第四季度" + fourthQuarter + "方。";
      } else if ("1".equals(endPaper.getPaperType())) {//增加计划
        todoContent =
            "用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
                + addNumber + "方(第一水量" + firstWater + "方，第二水量" + secondWater + "方)。";
      }
      todoService.add(endPaper.getId(), user, auditorId, auditorName, todoContent,
          JSONObject.toJSONString(endPaper),
          detailConfig, AuditConstants.END_PAPER_TODO_TYPE);
      //webSocket 推送消息给下一审核人员
      webSocketUtil.pushWaterTodo(user.getNodeCode(), auditorId);
    }
    //更新审核流程数据
    flowProcess.setAuditContent(opinions);
    flowProcess.setOperationTime(new Date());
    flowProcessService.updateById(flowProcess);
    //更新办结单数据
    this.baseMapper.updateById(endPaper);
    /**日志*/
    systemLogService.logInsert(user, "办结单管理", "审核", null);
  }

  private EndPaper report(User user, EndPaper endPaper) throws Exception {
    if ("1".equals(endPaper.getPaperType())) {//增加计划超额才上报
      /**查询计划表信息*/
      QueryWrapper wrapper = new QueryWrapper();
      wrapper.eq("node_code", endPaper.getNodeCode());
      wrapper.eq("unit_code", endPaper.getUnitCode());
      wrapper.eq("plan_year", endPaper.getPlanYear());
      UseWaterPlan useWaterPlan = planDailyAdjustmentService.getOne(wrapper);
      Double increaseLimit = Double.valueOf(
          dictItemService.findByDictCode("increaseLimit", user.getNodeCode(), user.getNodeCode())
              .getDictItemName());
      if (!endPaper.getNodeCode().equals(SystemConstants.MUNICIPAL_NODE_CODE)
          && endPaper.getAddNumber().compareTo(increaseLimit) > 0) {
        //当前办结单不属于市级且计划超额
        Todo todo = new Todo();
        todo.setNodeCode(SystemConstants.MUNICIPAL_NODE_CODE);
        todo.setExecutePersonId(SystemConstants.PLAN_EXCESS_AUDITOR_ID);
        todo.setExecutePersonName(SystemConstants.PLAN_EXCESS_AUDITOR_NAME);
        todo.setTodoTitle(AuditConstants.PLAN_EXCESS_TITLE);
        todo.setTodoContent(
            "取水单位" + endPaper.getUnitCode() + "（" + endPaper.getUnitName() + "）计划增加水量超过限额，需要市审批");
        todo.setTodoType(AuditConstants.PLAN_EXCESS_TYPE);
        todo.setTableName(AuditConstants.END_PAPER_TABLE);
        todo.setBusinessId(endPaper.getId());
        todo.setRegistrantId(user.getId());
        todo.setRegistrant(user.getUsername());
        todo.setSubmitNodeCode(user.getNodeCode());
        todo.setSubmitNodeName(
            dictItemService.findByDictCode("area", user.getNodeCode(), user.getNodeCode())
                .getDictItemName());
        JSONObject businessJson = new JSONObject();
        businessJson.put("unitCode", endPaper.getUnitCode());
        businessJson.put("unitName", endPaper.getUnitName());
//        businessJson.put("planType", endPaper.getPaperType());
        JSONObject originalPlan = new JSONObject();

        originalPlan.put("yearPlan", useWaterPlan.getCurYearPlan());
        originalPlan.put("firstQuarter", useWaterPlan.getFirstQuarter());
        originalPlan.put("secondQuarter", useWaterPlan.getSecondQuarter());
        originalPlan.put("thirdQuarter", useWaterPlan.getThirdQuarter());
        originalPlan.put("fourthQuarter", useWaterPlan.getFourthQuarter());
        businessJson.put("originalPlan", originalPlan);
//      if ("0".equals(endPaper.getPaperType())){
//        businessJson.put("reason","季度用水超额");
//        JSONObject afterAdjust = new JSONObject();
//        afterAdjust.put("firstQuarter",endPaper.getFirstQuarter());
//        afterAdjust.put("secondQuarter",endPaper.getSecondQuarter());
//        afterAdjust.put("thirdQuarter",endPaper.getThirdQuarter());
//        afterAdjust.put("fourthQuarter",endPaper.getFourthQuarter());
//        businessJson.put("afterAdjust",afterAdjust);
//      }else if ("1".equals(endPaper.getPaperType())){
        businessJson
            .put("reason", "增加计划:" + endPaper.getAddNumber() + "方，超过规定额度" + increaseLimit + "方。");
//      }
        todo.setBusinessJson(businessJson.toJSONString());
        String publicKey = jwtUtil.getPublicKey();
        String token = jwtUtil.creatToken(user, publicKey);
        HttpUtil.doPost(token, preViewRealPath + reportUrl, JSONObject.toJSONString(todo));
        endPaper.setAuditStatus("2");
      }
    }
    return endPaper;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse executeSettlement(User user, JSONObject jsonObject) throws Exception {
    ApiResponse response = new ApiResponse();
    String id = jsonObject.getString("id");
    Double firstQuarter = (null == jsonObject.getDouble("firstQuarter") ? 0
        : jsonObject.getDouble("firstQuarter"));
    Double secondQuarter = (null == jsonObject.getDouble("secondQuarter") ? 0
        : jsonObject.getDouble("secondQuarter"));
    Double thirdQuarter = (null == jsonObject.getDouble("thirdQuarter") ? 0
        : jsonObject.getDouble("thirdQuarter"));
    Double fourthQuarter = (null == jsonObject.getDouble("fourthQuarter") ? 0
        : jsonObject.getDouble("fourthQuarter"));
    Double curYearPlan = jsonObject.getDouble("curYearPlan");

    if (curYearPlan != firstQuarter + secondQuarter + thirdQuarter + fourthQuarter) {
      response.recordError("4个季度水量和不等于年计划");
      return response;
    }
    /**查询办结单信息*/
    EndPaper endPaper = this.getById(id);
    if (!("1".equals(endPaper.getAuditStatus()) && "1".equals(endPaper.getConfirmed()))) {
      //审核没有完成或没有通过、没有确认
      response.recordError("该数据没有审核通过或者还未确认，不能执行");
      return response;
    }
    /**查询计划表信息*/
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("node_code", endPaper.getNodeCode());
    wrapper.eq("unit_code", endPaper.getUnitCode());
    wrapper.eq("plan_year", endPaper.getPlanYear());
    UseWaterPlan useWaterPlan = planDailyAdjustmentService.getOne(wrapper);
    /**计划调整表信息*/
    UseWaterPlanAdd useWaterPlanAdd = new UseWaterPlanAdd();
    useWaterPlanAdd.setCreateTime(new Date());
    useWaterPlanAdd.setCreaterId(user.getId());
    useWaterPlanAdd.setCreater(user.getUsername());
    useWaterPlanAdd.setPlanYear(endPaper.getPlanYear());
    useWaterPlanAdd.setNodeCode(endPaper.getNodeCode());
    useWaterPlanAdd.setUnitCode(endPaper.getUnitCode());
    useWaterPlanAdd.setUnitName(endPaper.getUnitName());
    useWaterPlanAdd.setUseWaterUnitId(endPaper.getUseWaterUnitId());
    useWaterPlanAdd.setWaterMeterCode(endPaper.getWaterMeterCode());
    useWaterPlanAdd.setPrinted("0");
    useWaterPlanAdd.setStatus("3");//已审核，已累加
    useWaterPlanAdd.setPlanType(endPaper.getPaperType());

    if ("1".equals(endPaper.getPaperType())) {//增加计划
      useWaterPlan.setCurYearPlan(useWaterPlan.getCurYearPlan() + curYearPlan);
      useWaterPlan.setFirstQuarter(useWaterPlan.getFirstQuarter() + firstQuarter);
      useWaterPlan.setSecondQuarter(useWaterPlan.getSecondQuarter() + secondQuarter);
      useWaterPlan.setThirdQuarter(useWaterPlan.getThirdQuarter() + thirdQuarter);
      useWaterPlan.setFourthQuarter(useWaterPlan.getFourthQuarter() + fourthQuarter);
      /**调整表“增加计划”的年计划、4个季度数据*/
      useWaterPlanAdd.setFirstQuarter(firstQuarter);
      useWaterPlanAdd.setSecondQuarter(secondQuarter);
      useWaterPlanAdd.setThirdQuarter(thirdQuarter);
      useWaterPlanAdd.setFourthQuarter(fourthQuarter);
      useWaterPlanAdd.setCurYearPlan(curYearPlan);
      /**调整结果*/
      endPaper.setResult(
          "增加计划" + endPaper.getAddNumber() + "m3(" + endPaper.getChangeQuarter() + "季度)");
    } else {//调整计划(4个季度间调整)
      /**调整表“计划调整”的年计划、4个季度数据(相对于计划表数据的改变量)*/
      useWaterPlanAdd.setCurYearPlan(0d);//double
      useWaterPlanAdd.setFirstQuarter(firstQuarter - useWaterPlan.getFirstQuarter());
      useWaterPlanAdd.setSecondQuarter(secondQuarter - useWaterPlan.getSecondQuarter());
      useWaterPlanAdd.setThirdQuarter(thirdQuarter - useWaterPlan.getThirdQuarter());
      useWaterPlanAdd.setFourthQuarter(fourthQuarter - useWaterPlan.getFourthQuarter());
      /**修改后计划表4个季度水量*/
      useWaterPlan.setFirstQuarter(firstQuarter);
      useWaterPlan.setSecondQuarter(secondQuarter);
      useWaterPlan.setThirdQuarter(thirdQuarter);
      useWaterPlan.setFourthQuarter(fourthQuarter);
      /**调整结果*/
      endPaper.setResult(
          "各季度依次变化:" + useWaterPlanAdd.getFirstQuarter().toString() + ";" + useWaterPlanAdd
              .getSecondQuarter().toString()
              + ";" + useWaterPlanAdd.getThirdQuarter().toString() + ";" + useWaterPlanAdd
              .getFourthQuarter().toString()
              + "。");
    }
    /**更新计划表数据*/
    useWaterPlan.setUpdateUserId(user.getId());
    useWaterPlan.setUpdateTime(new Date());
    useWaterPlan.setExistSettlementForm("0");//执行办结单后将是否存在未完成的办结单状态设置为否
    planDailyAdjustmentService.updateById(useWaterPlan);
    /**重算加价*/
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("countYear", useWaterPlanAdd.getPlanYear());
    List<String> unitIds = new ArrayList<>();
    unitIds.add(useWaterPlanAdd.getUseWaterUnitId());
    jsonObject1.put("unitIds", unitIds);
    waterUsePayInfoService.initPayInfo(jsonObject1);
    /**调整表新增*/
    useWaterPlanAddService.save(useWaterPlanAdd);
    /**更新办结单信息*/
    endPaper.setExecuted("1");//已执行
    endPaper.setExecutorId(user.getId());
    endPaper.setExecutor(user.getUsername());
    endPaper.setExecuteTime(new Date());
    endPaper.setFirstQuarter(firstQuarter);
    endPaper.setSecondQuarter(secondQuarter);
    endPaper.setThirdQuarter(thirdQuarter);
    endPaper.setFourthQuarter(fourthQuarter);
    this.updateById(endPaper);
    /**如果是来自微信(网上申报)，则更新微信调整表核定数*/
    if ("1".equals(endPaper.getDataSources())) {
      UseWaterPlanAddWX useWaterPlanAddWX = new UseWaterPlanAddWX();
      useWaterPlanAddWX.setId(endPaper.getWaterPlanWXId());
      useWaterPlanAddWX.setCheckAdjustWater(curYearPlan);
      useWaterPlanAddWX.setFirstQuarterQuota(firstQuarter);
      useWaterPlanAddWX.setSecondQuarterQuota(secondQuarter);
      useWaterPlanAddWX.setThirdQuarterQuota(thirdQuarter);
      useWaterPlanAddWX.setFourthQuarterQuota(fourthQuarter);
      useWaterPlanAddWX.setExecuted("1");//已执行
      useWaterPlanAddWXService.update(useWaterPlanAddWX, user);
    }
    /**向用水单位发起通知*/
    String messageContent =
        "用水单位" + useWaterPlan.getUnitCode() + "(" + useWaterPlan.getUnitName() + ")"
            + "调整计划/增加计划已执行,调整后水量分别为：年计划" + useWaterPlan.getCurYearPlan() + "方,一季度" + useWaterPlan
            .getFirstQuarter() + "方,二季度"
            + useWaterPlan.getSecondQuarter() + "方,三季度" + useWaterPlan.getThirdQuarter() + "方,四季度"
            + useWaterPlan.getFourthQuarter() + "方。";
    messageService.messageToUnit(useWaterPlan.getUnitCode(), messageContent,
        AuditConstants.END_PAPER_TODO_TITLE);
    /**向用水单位发送短信*/
    smsService.sendMsgToUnit(user, useWaterPlan.getUnitCode(), messageContent, "计划通知");
    // webSocket向公共服务平台推送消息
    webSocketUtil.pushPublicNews(useWaterPlan.getNodeCode(), useWaterPlan.getUnitCode());
    /**日志*/
    systemLogService.logInsert(user, "办结单管理", "执行", null);
    return response;

  }

  @Override
  public boolean updateFromWeChat(EndPaper endPaper) {
    /**微信端确认后更新数据*/
    if (null != endPaper.getCurYearPlan()) {//如果微信端传入的参数有年计划则表示是“增加计划”
      endPaper.setAddNumber(endPaper.getCurYearPlan());
    }
    return this.baseMapper.updateFromWeChat(endPaper);
  }

  @Override
  public List<Map<String, Object>> nextAuditRole(String id, String nodeCode, String auditBtn) {
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("id", id);
    EndPaper endPaper = getOne(wrapper);
    return flowNodeInfoService.nextAuditRole(endPaper.getNextNodeId(), id, nodeCode, auditBtn);
  }

  @Override
  public Map<String, Object> sendInfoPage(User user, JSONObject jsonObject) {
    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    String userId = user.getId();
    String unitCode = jsonObject.getString("unitCode");//单位编号
    String status = jsonObject.getString("status");//发送状态

    Map<String, Object> map = new HashMap();
    if (StringUtils.isNotBlank(jsonObject.getString("nodeCode"))) {
      map.put("nodeCode", jsonObject.getString("nodeCode"));
    } else {
      map.put("nodeCode", user.getNodeCode());
    }
    map.put("userId", userId);
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    String messageTypecode = SmsConstants.SEND_NOTIFICATION_ADJUST_RESULT;
    JSONObject json = new JSONObject();
    json.put("messageTypecode", messageTypecode);
    if (StringUtils.isNotBlank(status)) {
      json.put("status", status);
    }
    Map<String, Object> result = new LinkedHashMap<>();
    List<Map<String, Object>> records = new ArrayList<>();
    /**查询单位信息*/
    List<SendListVO> list = this.baseMapper.queryAfterAdjustUnit(map);
    if (list.isEmpty()) {
      result.put("total", 0);//满足条件的总条数
      result.put("size", size);//每页条数
      result.put("pages", 0);//一共有多少页
      result.put("current", current);//当前页
      result.put("records", records);
    } else {
      /**查出满足条件的共有多少条*/
      int num = smsSendService.sendResultNum(list, json);
      result.put("total", num);//满足条件的总条数
      result.put("size", size);//每页条数
      result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
      result.put("current", current);//当前页
      /**查出满足条件的数据*/
      json.put("current", current);
      json.put("pageSize", size);
      records = smsSendService.sendResultPage(list, json);
      result.put("records", records);
    }
    return result;
  }

  @Override
  public void adjustResultNotification(User user, List<SendListVO> sendList) throws Exception {
    smsService.sendNotification(user, sendList, SmsConstants.SEND_NOTIFICATION_ADJUST_RESULT, null);
  }


  /**
   * 查询附件
   */
  private EndPaperVO getFiles(EndPaperVO paperVO, String path) {
    try {
      String auditFileId = paperVO.getAuditFileId();
      String otherFileId = paperVO.getOtherFileId();
      String waterProofFileId = paperVO.getWaterProofFileId();
      if (StringUtils.isNotBlank(auditFileId)) {
        paperVO.setAuditFiles(
            fileService.findByIds(Arrays.asList(auditFileId.split(",")), path));
      }
      if (StringUtils.isNotBlank(otherFileId)) {
        paperVO.setOtherFiles(
            fileService.findByIds(Arrays.asList(otherFileId.split(",")), path));
      }
      if (StringUtils.isNotBlank(waterProofFileId)) {
        paperVO.setWaterProofFiles(
            fileService.findByIds(Arrays.asList(waterProofFileId.split(",")), path));
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("查询附件信息异常======={}" + e.getMessage());
    }
    return paperVO;
  }

  private EndPaper getFiles(EndPaper paper, String path) {
    try {
      String auditFileId = paper.getAuditFileId();
      String otherFileId = paper.getOtherFileId();
      String waterProofFileId = paper.getWaterProofFileId();
      if (StringUtils.isNotBlank(auditFileId)) {
        paper.setAuditFiles(
            fileService.findByIds(Arrays.asList(auditFileId.split(",")), path));
      }
      if (StringUtils.isNotBlank(otherFileId)) {
        paper.setOtherFiles(
            fileService.findByIds(Arrays.asList(otherFileId.split(",")), path));
      }
      if (StringUtils.isNotBlank(waterProofFileId)) {
        paper.setWaterProofFiles(
            fileService.findByIds(Arrays.asList(waterProofFileId.split(",")), path));
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("查询附件信息异常======={}" + e.getMessage());
    }
    return paper;
  }
}