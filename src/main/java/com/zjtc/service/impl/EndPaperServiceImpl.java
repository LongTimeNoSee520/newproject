package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.EndPaperMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.User;
import com.zjtc.model.vo.EndPaperVO;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.FlowExampleService;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.MessageService;
import com.zjtc.service.PlanDailyAdjustmentService;
import com.zjtc.service.SmsService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterPlanAddService;
import com.zjtc.service.UseWaterPlanAddWXService;
import com.zjtc.service.WaterUsePayInfoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/9
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

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {

    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    String nodeCode = user.getNodeCode();//节点编码
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
    map.put("nodeCode", nodeCode);
    map.put("userId", userId);
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
    result.put("records", records);
    return result;
  }

  @Override
  public ApiResponse cancelSettlement(List<String> ids) {
    ApiResponse response = new ApiResponse();
    /**根据id查询*/
    List<EndPaper> endPapers = this.selectBatchIds(ids);
    for (EndPaper endPaper : endPapers) {
         /**类型为增加水量的办结单且处于审核中的办结单不能撤销*/
        if ("1".equals(endPaper.getPaperType()) && !"0".equals(endPaper.getAuditStatus())) {//0为办结单提交审核还未经过下一环节审核的状态
          //如果不是刚提交未审核状态，则不能撤销
          response.recordError("处于审核中或者审核通过的增加计划办结单不能撤销");
          return response;
        }else{
          /**根据单位编号更新是否存在办结单状态为否*/
          planDailyAdjustmentService
            .updateExistSettlement("0", endPaper.getUnitCode(), endPaper.getNodeCode(),
                endPaper.getPlanYear());
          /**更新撤销状态和时间*/
          endPaper.setRescinded("1");
          endPaper.setRescindTime(new  Date());
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
//    Double firstQuarter =jsonObject.getDouble("firstQuarter");
//    Double secondQuarter = jsonObject.getDouble("secondQuarter");
//    Double thirdQuarter = jsonObject.getDouble("thirdQuarter");
//    Double fourthQuarter = jsonObject.getDouble("fourthQuarter");
    Double firstWater = jsonObject.getDouble("firstWater");
    Double secondWater = jsonObject.getDouble("secondWater");
    String addWay = jsonObject.getString("addWay");//加计划的方式：1-平均，2-最高
    Integer quarter = jsonObject.getInteger("quarter");
    Boolean year = jsonObject.getBoolean("year"); //是否在年计划上增加
    Double addNumber = jsonObject.getDouble("addNumber");
    String auditStatus = jsonObject.getString("auditStatus");
    String opinions = jsonObject.getString("opinions");//意见
    String auditorName =jsonObject.getString("auditorName");//审核人员名称
    String auditorId =jsonObject.getString("auditorId");//审核人员id
    String businessJson = jsonObject.getString("businessJson");
    String detailConfig = jsonObject.getString("detailConfig");

    EndPaper endPaper = this.baseMapper.selectById(id);
    if (null != year && year) {
      /**选择了年计划*/
     endPaper.setAlgorithmRules("1");
    }
    endPaper.setFirstWater(firstWater);
    endPaper.setSecondWater(secondWater);
    endPaper.setAddWay(addWay);
    endPaper.setChangeQuarter(quarter.toString());
    endPaper.setAddNumber(addNumber);
    //查询审核流程下一环节信息
    List<Map<String, Object>> hasNext = flowNodeInfoService
        .nextAuditRole(id, AuditConstants.END_PAPER_TABLE, user.getNodeCode(), auditStatus);
    //获取当前环节的审核操作记录
    FlowProcess flowProcess = flowProcessService.getLastData(user.getNodeCode(), endPaper.getId());
    if (hasNext.isEmpty()) { //审核流程结束(本次通过且没有下一环节)
      //设置办结单审核下一环节id为""
      endPaper.setNextNodeId("");
      if ("1".equals(auditStatus)) {//本次审核通过
        //审核操作记录审核状态更新
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
        //设置办结单审核状态
        endPaper.setAuditStatus("1");//通过
        //网上申报的办结单
        if ("1".equals(endPaper.getPaperType())){
          UseWaterPlanAddWX waterPlanAddWX = new UseWaterPlanAddWX();
          waterPlanAddWX.setId(endPaper.getWaterPlanWXId());
          //只有通过时(且审核流程走完)，不通过则不修改
          waterPlanAddWX.setAuditStatus("4");//微信端提交审核通过后办结单审核也通过
          useWaterPlanAddWXService.update(waterPlanAddWX);
        }
        /**新增通知给发起人*/
        //查询 流程的发起人
        FlowProcess firstProcess = flowProcessService.selectFirstRecords(id);
        //通知表新增
        String messageContent =
            "您发起的[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
                + addNumber + "方(第一水量"
                + firstWater + "方，第二水量" + secondWater + "方)]办结单审核已通过。";
        messageService
            .add(user.getNodeCode(), firstProcess.getOperatorId(), firstProcess.getOperator(),
                AuditConstants.END_PAPER_MESSAGE_TYPE, messageContent);
        /**短信通知给发起人*/
        smsService.sendMsgToPromoter(user, firstProcess.getOperatorId(), firstProcess.getOperator(),
            messageContent, "计划通知");
        //TODO ,webSocket推送到前端
        /**新增通知给用水单位*/
        String messageContent1 =
            "[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
                + addNumber + "方(第一水量"
                + firstWater + "方，第二水量" + secondWater + "方)]审核已通过,请到微信端确认。";
        messageService.messageToUnit(endPaper.getUnitCode(), messageContent1,
            AuditConstants.END_PAPER_TODO_TITLE);
        /**短信通知给用水单位*/
        smsService.sendMsgToUnit(user, endPaper.getUnitCode(), messageContent1, "计划通知");
        //TODO webSocket推送到公共服务端
      }
      if ("0".equals(auditStatus)) {//本次审核不通过(没有下一环节则是回到提交人，由提交人本人审核的本条数据，不发起通知)
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
        endPaper.setAuditStatus("2");//2处于审核中(已有审核记录的只要不是最终环节通过都为审核中状态)
        // 没有下一环节则是回到提交人，由提交人本人审核的本条数据，不发起通知和发短信给提交人
        /**通知发给用水单位*/
        String messageContent =
            "[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
                + addNumber + "方(第一水量"
                + firstWater + "方，第二水量" + secondWater + "方)]审核未通过。";
        messageService.messageToUnit(endPaper.getUnitCode(), messageContent,
            AuditConstants.END_PAPER_TODO_TITLE);
        /**短信通知给用水单位*/
        smsService.sendMsgToUnit(user, endPaper.getUnitCode(), messageContent, "计划通知");
        //TODO webSocket推送到公共服务端
      }
      //修改代办状态
      todoService.edit(endPaper.getId(), user.getNodeCode(), user.getId());
      //修改实例表数据
      flowExampleService.edit(user.getNodeCode(), user.getId());
    } else if (!hasNext.isEmpty()){//审核流程继续
      //修改当前办结单下一环节id
      endPaper.setNextNodeId(hasNext.get(0).get("flowNodeId").toString());
      //修改办结单状态为2处于审核中
      endPaper.setAuditStatus("2");
      //当前操作记录修改状态
      if ("0".equals(auditStatus)) {
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
      }
      if ("1".equals(auditStatus)) {
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
      }
      //新增审核流程下一环节记录
      flowProcessService.add(user.getNodeCode(), endPaper.getId(), auditorName, auditorId);
      //修改待办状态
      todoService.edit(endPaper.getId(), user.getNodeCode(), user.getId());
      //新增一条待办
      String todoContent =
          "用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"+addNumber+"方(第一水量"
              + firstWater + "方，第二水量" + secondWater+"方)。";
      todoService.add(endPaper.getId(), user, auditorId, auditorName, todoContent, businessJson,
          detailConfig, AuditConstants.END_PAPER_TODO_TYPE);
    }
    //更新审核流程数据
    flowProcess.setAuditContent(opinions);
    flowProcess.setOperationTime(new Date());
    flowProcessService.updateById(flowProcess);
    //更新办结单数据
    this.baseMapper.updateById(endPaper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse executeSettlement(User user, JSONObject jsonObject) throws Exception {
    ApiResponse response = new ApiResponse();
    String id = jsonObject.getString("id");
    Double firstQuarter = (null ==jsonObject.getDouble("firstQuarter")? 0:jsonObject.getDouble("firstQuarter"));
    Double secondQuarter = (null == jsonObject.getDouble("secondQuarter")? 0:jsonObject.getDouble("secondQuarter"));
    Double thirdQuarter = (null == jsonObject.getDouble("thirdQuarter")? 0:jsonObject.getDouble("thirdQuarter")) ;
    Double fourthQuarter = (null ==jsonObject.getDouble("fourthQuarter")? 0:jsonObject.getDouble("fourthQuarter"));
    Double curYearPlan = jsonObject.getDouble("curYearPlan");

    if (curYearPlan != firstQuarter + secondQuarter + thirdQuarter + fourthQuarter) {
      response.recordError("4个季度水量和不等于年计划");
      return response;
    }
    /**查询办结单信息*/
    EndPaper endPaper = this.selectById(id);
    if (!("1".equals(endPaper.getAuditStatus()) && "1".equals(endPaper.getConfirmed()))){
      //审核没有完成或没有通过、没有确认
      response.recordError("该数据没有审核通过或者还未确认，不能执行");
       return response;
    }
    /**查询计划表信息*/
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("node_code", endPaper.getNodeCode());
    wrapper.eq("unit_code", endPaper.getUnitCode());
    wrapper.eq("plan_year", endPaper.getPlanYear());
    UseWaterPlan useWaterPlan = planDailyAdjustmentService.selectOne(wrapper);
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
          "各季度依次变化:" + useWaterPlanAdd.getFirstQuarter().toString() + ";" + useWaterPlanAdd.getSecondQuarter().toString()
              + ";" + useWaterPlanAdd.getThirdQuarter().toString() + ";" + useWaterPlanAdd.getFourthQuarter().toString()
              + "。");
    }
    /**更新计划表数据*/
    useWaterPlan.setUpdateUserId(user.getId());
    useWaterPlan.setUpdateTime(new Date());
    useWaterPlan.setExistSettlementForm("0");//执行办结单后将是否存在未完成的办结单状态设置为否
    planDailyAdjustmentService.updateById(useWaterPlan);
    /**重算加价*/
    JSONObject jsonObject1 = new JSONObject();
    jsonObject.put("countYear",useWaterPlanAdd.getPlanYear());
    List<String> unitIds = new ArrayList<>();
    unitIds.add(useWaterPlanAdd.getUseWaterUnitId());
    jsonObject.put("unitIds",unitIds);
    waterUsePayInfoService.initPayInfo(jsonObject1);
    /**调整表新增*/
    useWaterPlanAddService.insert(useWaterPlanAdd);
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
    if("1".equals(endPaper.getDataSources())){
      UseWaterPlanAddWX useWaterPlanAddWX = new UseWaterPlanAddWX();
      useWaterPlanAddWX.setId(endPaper.getWaterPlanWXId());
      useWaterPlanAddWX.setCheckAdjustWater(curYearPlan);
      useWaterPlanAddWX.setFirstQuarterQuota(firstQuarter);
      useWaterPlanAddWX.setSecondQuarterQuota(secondQuarter);
      useWaterPlanAddWX.setThirdQuarterQuota(thirdQuarter);
      useWaterPlanAddWX.setFourthQuarterQuota(fourthQuarter);
      useWaterPlanAddWX.setExecuted("1");//已执行
      useWaterPlanAddWXService.update(useWaterPlanAddWX);
    }
    /**向用水单位发起通知*/
    String messageContent =
        "用水单位" + useWaterPlan.getUnitCode() + "(" + useWaterPlan.getUnitName() + ")"
            + "调整计划/增加计划已执行,调整后水量分别为：年计划" + useWaterPlan.getCurYearPlan() + "方,一季度" + useWaterPlan
            .getFirstQuarter() + "方,二季度"
            + useWaterPlan.getSecondQuarter() + "方,三季度" + useWaterPlan.getThirdQuarter() + "方,四季度"
            + useWaterPlan.getFourthQuarter() + "方。";
    messageService.messageToUnit(useWaterPlan.getUnitCode(),messageContent,AuditConstants.END_PAPER_TODO_TITLE);
    /**向用水单位发送短信*/
    smsService.sendMsgToUnit(user,useWaterPlan.getUnitCode(),messageContent,"计划通知");
    //TODO webSocket向公共服务平台推送消息
    return response;
  }

  @Override
  public boolean updateFromWeChat(EndPaper endPaper) {
    /**来自办结单的更新或者新增*/
    if (null != endPaper.getCurYearPlan()){//如果微信端传入的参数有年计划则表示是“增加计划”
      endPaper.setAddNumber(endPaper.getCurYearPlan());
    }
    return this.baseMapper.updateFromWeChat(endPaper);
  }

  @Override
  public List<Map<String, Object>> nextAuditRole(String id, String nodeCode, String auditBtn) {
    return flowNodeInfoService.nextAuditRole(id, AuditConstants.END_PAPER_TABLE, nodeCode, auditBtn);
  }
}