package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.WebSocketUtil;
import com.zjtc.mapper.waterBiz.UseWaterSelfDefinePlanMapper;
import com.zjtc.model.Person;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.UseWaterSelfDefinePlan;
import com.zjtc.model.User;
import com.zjtc.model.vo.UseWaterSelfDefinePlanVO;
import com.zjtc.service.MessageService;
import com.zjtc.service.PersonService;
import com.zjtc.service.SmsService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterPlanAddService;
import com.zjtc.service.UseWaterPlanService;
import com.zjtc.service.UseWaterSelfDefinePlanService;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TWUseWaterSelfDefinePlan的服务接口的实现类 用水自平计划表
 *
 * @Author: ZhouDaBo
 * @Date: 2021/1/4
 */
@Service
@Slf4j
public class UseWaterSelfDefinePlanServiceImpl extends
    ServiceImpl<UseWaterSelfDefinePlanMapper, UseWaterSelfDefinePlan> implements
    UseWaterSelfDefinePlanService {

  @Autowired
  private UseWaterPlanAddService useWaterPlanAddService;

  @Autowired
  private UseWaterPlanService useWaterPlanService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private SmsService smsService;

  /**
   * 附件存储目录
   */
  @Value("${server.servlet-path}")
  private String contextPath;

  /**
   * 上下文
   */
  @Value("${file.preViewRealPath}")
  private String preViewRealPath;

  @Autowired
  private SystemLogService systemLogService;

  @Autowired
  private UseWaterSelfDefinePlanMapper useWaterSelfDefinePlanMapper;

  @Autowired
  private TodoService todoService;

  @Autowired
  private PersonService personService;

  @Autowired
  private WebSocketUtil webSocketUtil;

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
//    自平年份
    Integer planYear = null;
    if (null != jsonObject.getInteger("planYear")) {
      planYear = jsonObject.getInteger("planYear");
    }

//    单位编号
    String unitCode = "";
    if (null != jsonObject.getString("unitCode")) {
      unitCode = jsonObject.getString("unitCode");
    }
////    排序
//    String rank = "";
//    if (null != jsonObject.getString("rank")) {
//      rank = jsonObject.getString("rank");
//    }
    if (StringUtils.isBlank(nodeCode)) {
      response.recordError("系统异常");
      return response;
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
        .selectCount(unitName, userType, planYear, executed, unitCode, nodeCode, auditStatus, userId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
//    附件展示路径
    String path = preViewRealPath + contextPath + "/";
    // TODO: 2021/2/18 因为没有附件id,需要根据业务id在附件表里去查对应的业务id,现在的做法是默认有附件id的情况,错误
    List<UseWaterSelfDefinePlanVO> waterSelfDefinePlans = this.baseMapper
        .queryList(currPage, pageSize, unitName, userType, planYear, executed, unitCode, nodeCode, auditStatus, userId,path);
    for (UseWaterSelfDefinePlanVO useWaterSelfDefinePlanVO : waterSelfDefinePlans){
//     未被审核和未被执行
      if ("0".equals(useWaterSelfDefinePlanVO.getAuditStatus()) && "0".equals(useWaterSelfDefinePlanVO.getExecuted())){
        useWaterSelfDefinePlanVO.setAuditOperationStatus(true);
      }
    }
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", waterSelfDefinePlans);
    response.setCode(200);
    response.setData(map);
    return response;
  }

  @Override
  public ApiResponse audit(User user, String id, String auditPerson, String auditPersonId,
      String auditStatus, String auditResult, String auditorName,
      String auditorId, String businessJson, String detailConfig, String nextNodeId) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(id) || StringUtils.isBlank(auditPerson) || StringUtils
        .isBlank(auditPersonId) || StringUtils.isBlank(auditStatus)) {
      response.recordError("审核失败");
    }
    UseWaterSelfDefinePlan useWaterSelfDefinePlan = this.baseMapper.selectById(id);
    if ("1".equals(useWaterSelfDefinePlan.getAuditStatus()) || "2".equals(useWaterSelfDefinePlan.getAuditStatus())){
      response.recordError("已经审核过,不能再审核");
      return response;
    }
    String messageContent = null;
    if ("1".equals(auditStatus)) {
//      消息推送用水单位
      messageContent =
          "用水单位" + useWaterSelfDefinePlan.getUnitCode() +
              "(" + useWaterSelfDefinePlan.getUnitName() + ")" +
              "自平水量申请,第一季度计划:" + useWaterSelfDefinePlan.getFirstQuarter() +
              " 方,第二季度计划:" + useWaterSelfDefinePlan.getSecondQuarter() +
              "方,第三季度计划:" + useWaterSelfDefinePlan.getThirdQuarter() +
              "方,第四季度计划:" + useWaterSelfDefinePlan.getFourthQuarter() + "方,审核已驳回。";
      messageService
          .add(useWaterSelfDefinePlan.getNodeCode(), auditPersonId, auditPerson,
              AuditConstants.NOT_APPROVED, messageContent);
      try {
        smsService.sendMsgToPromoter(user, auditPersonId, auditPerson, messageContent, "计划通知");
      } catch (Exception e) {
        log.error("用水计划自平审核消息推送失败");
      }
    } else if ("2".equals(auditStatus)) {
//      消息推送用水单位
      messageContent =
          "用水单位" + useWaterSelfDefinePlan.getUnitCode() +
              "(" + useWaterSelfDefinePlan.getUnitName() + ")" +
              "自平水量申请,第一季度计划:" + useWaterSelfDefinePlan.getFirstQuarter() +
              " 方,第二季度计划:" + useWaterSelfDefinePlan.getSecondQuarter() +
              "方,第三季度计划:" + useWaterSelfDefinePlan.getThirdQuarter() +
              "方,第四季度计划:" + useWaterSelfDefinePlan.getFourthQuarter() + "方,审核已通过。";
      messageService
          .add(useWaterSelfDefinePlan.getNodeCode(), auditPersonId, auditPerson,
              AuditConstants.GET_APPROVED, messageContent);
      try {
        smsService.sendMsgToPromoter(user, auditPersonId, auditPerson, messageContent, "计划通知");
      } catch (Exception e) {
        log.error("用水计划自平审核消息推送失败");
      }
    }
    UseWaterSelfDefinePlan waterSelfDefinePlan = new UseWaterSelfDefinePlan();
    waterSelfDefinePlan.setId(id);
//    审核状态(1:审核不通过,2:审核通过)
    waterSelfDefinePlan.setAuditStatus(auditStatus);
//    审核时间
    waterSelfDefinePlan.setAuditTime(new Date());
//    审核人
    waterSelfDefinePlan.setAuditPerson(auditPerson);
//    审核人id
    waterSelfDefinePlan.setAuditPersonId(auditPersonId);
//    审核结果
    waterSelfDefinePlan.setAuditResult(auditResult);
    int integer = this.baseMapper.updateById(waterSelfDefinePlan);
    if (integer > 0) {
      response.setCode(200);
      response.setMessage("审核成功");
      systemLogService.logInsert(user,"用水计划自平","用水计划自平审核通过","");
 //      取消待办
      todoService.edit(id, user.getNodeCode(), user.getId());
//     发起待办
      List<Person> personList = null;
      try {
        personList = personService.selectPersonAll(auditorId);
      } catch (Exception e) {
        log.error("查询下一环节审核人员为空:"+e.getMessage());
      }
      for (Person person : personList) {
        todoService.add(
            id,
            user,
            person.getId(),
            person.getUserName(),
            messageContent,
            businessJson,
            detailConfig,
            AuditConstants.END_PAPER_TODO_TYPE);
//        webSocket消息推送
        webSocketUtil.pushWaterNews(user.getNodeCode(),user.getId());
      }
      return response;
    }
    response.recordError("审核失败");
    return response;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)//多个表中修改数据时，一个出错全部回滚
  public ApiResponse execute(User user, List<String> ids, String executor, String executorId,
      String codeNode) {
    ApiResponse response = new ApiResponse();
    if (ids.isEmpty() || StringUtils.isBlank(executor) || StringUtils.isBlank(executorId)
        || StringUtils.isBlank(codeNode)) {
      response.recordError("系统异常");
    }
//    自平表响应结果
    int zp = 0;
//    计划用水调整响应结果
    boolean planAdd = false;
//    用水计划表数据响应结果
    int water = 0;
//>>>>>>>>> 第一步:更新自平表数据<<<<<<<<<
    for (String id : ids) {
//    查询未审核过审核不通过信息
      UseWaterSelfDefinePlan auditStatus = useWaterSelfDefinePlanMapper.selectAuditStatus(id);
//      查询已经被执行信息
      UseWaterSelfDefinePlan executed = useWaterSelfDefinePlanMapper.selectExecuted(id);
//      如果是未审核的数据不能被执行
      if (null != auditStatus) {
        response.recordError("用水单位:" +auditStatus.getUnitCode()+"(" +auditStatus.getUnitName()+")" + "的数据未审核或审核不通过,不能执行");
        return response;
//      如果是已经执行过的数据不能再被执行
      } else if (null != executed) {
        response.recordError("用水单位:" +auditStatus.getUnitCode()+"(" +auditStatus.getUnitName()+")" +"的数据已经被执行,不能再被执行");
        return response;
      }
    }
//>>>>>>>>>> 第二步:新增用水计划调整表<<<<<<<<<<
    EntityWrapper<UseWaterSelfDefinePlan> wrapper = new EntityWrapper<>();
    List<UseWaterSelfDefinePlan> useWaterSelfDefinePlans = null;
//    根据执行id查询出对应的数据
    wrapper.in("id", ids);
    useWaterSelfDefinePlans = this.baseMapper.selectList(wrapper);
    if (useWaterSelfDefinePlans.isEmpty()) {
      response.recordError("系统异常,操作失败");
      return response;
    }
    UseWaterPlanAdd waterPlanAdd = new UseWaterPlanAdd();

    for (UseWaterSelfDefinePlan useWaterSelfDefinePlan : useWaterSelfDefinePlans) {
      Wrapper<UseWaterPlan> wrapper1 = new EntityWrapper<>();
      wrapper1.eq("node_code", useWaterSelfDefinePlan.getNodeCode());
      wrapper1.eq("unit_code", useWaterSelfDefinePlan.getUnitCode());
      wrapper1.eq("plan_year", useWaterSelfDefinePlan.getPlanYear());
      UseWaterPlan useWaterPlanModel = useWaterPlanService.selectOne(wrapper1);//实际上只有一条数据
      if (null == useWaterPlanModel) {
        response.recordError("系统异常,操作失败");
        return response;
      }
      if (useWaterPlanModel.getCurYearPlan()
          != (useWaterSelfDefinePlan.getFirstQuarter() + useWaterSelfDefinePlan.getSecondQuarter()
          + useWaterSelfDefinePlan.getThirdQuarter() + useWaterSelfDefinePlan.getFourthQuarter())) {
        response.recordError("四个季度水量总和与年计划水量不符");
        return response;
      }
//      主键
      waterPlanAdd.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//      节点编码
      waterPlanAdd.setNodeCode(codeNode);
//      单位id
      waterPlanAdd.setUseWaterUnitId(useWaterSelfDefinePlan.getUseWaterUnitId());
//      单位名称
      waterPlanAdd.setUnitName(useWaterSelfDefinePlan.getUnitName());
//      单位编号
      waterPlanAdd.setUnitCode(useWaterSelfDefinePlan.getUnitCode());
//      水表档案号
      waterPlanAdd.setWaterMeterCode(useWaterSelfDefinePlan.getWaterMeterCode());
//      编制年度
      waterPlanAdd.setPlanYear(useWaterSelfDefinePlan.getPlanYear());
//      本年计划（当前年计划）
//      waterPlanAdd.setCurYearPlan(useWaterSelfDefinePlan.getCurYearPlan());
      waterPlanAdd.setCurYearPlan(0.0);
//      第一季度计划
      waterPlanAdd.setFirstQuarter(
          useWaterSelfDefinePlan.getFirstQuarter() - useWaterPlanModel.getFirstQuarter());
//      第二季度计划
      waterPlanAdd.setSecondQuarter(
          useWaterSelfDefinePlan.getSecondQuarter() - useWaterPlanModel.getSecondQuarter());
//      第三季度计划
      waterPlanAdd.setThirdQuarter(
          useWaterSelfDefinePlan.getThirdQuarter() - useWaterPlanModel.getThirdQuarter());
//      第四季度计划
      waterPlanAdd.setFourthQuarter(
          useWaterSelfDefinePlan.getFourthQuarter() - useWaterPlanModel.getFourthQuarter());
//      调整类型(0代表日常调整)
      waterPlanAdd.setPlanType("0");
//      创建时间
      waterPlanAdd.setCreateTime(new Date());
//      创建者
      waterPlanAdd.setCreater(executor);
//      创建者id
      waterPlanAdd.setCreaterId(executorId);
      // TODO: 2021/2/18 附件信息代办 
////      审批申请附件id
//      waterPlanAdd.setAuditFileId(useWaterSelfDefinePlan.getSelfDefineFileId());
//      状态(1:草稿、2:审核、3:累加)
      waterPlanAdd.setStatus("3");
//>>>>>>>>第三步:更新用水计划表数据<<<<<<<<<<
//    匹配用水计划表里的那条数据
      List<UseWaterPlan> waterPlan = this.baseMapper.selectWaterPlan(
          useWaterSelfDefinePlan.getNodeCode(),
          useWaterSelfDefinePlan.getUseWaterUnitId(),
          useWaterSelfDefinePlan.getUnitName(),
          useWaterSelfDefinePlan.getUnitCode(),
          useWaterSelfDefinePlan.getPlanYear());
      System.out.println("匹配到的用水计划表数据:" + waterPlan);
//      修改自平数据
      for (String id : ids) {
        zp = this.baseMapper.updateExecuteData(id, executor, executorId, new Date());
      }
//      用水计划日常调整
      planAdd = useWaterPlanAddService.insert(waterPlanAdd);
//      修改用水计划表季度水量
      for (UseWaterPlan useWaterPlan : waterPlan) {
        water = this.baseMapper
            .updateUseWaterPlanWater(useWaterPlan.getId(), useWaterSelfDefinePlan.getFirstQuarter(),
                useWaterSelfDefinePlan.getSecondQuarter(), useWaterSelfDefinePlan.getThirdQuarter(),
                useWaterSelfDefinePlan.getFourthQuarter(), useWaterSelfDefinePlan.getCurYearPlan(),
                executorId, new Date());
      }
//      消息通知用水单位
      String messageContent =
          "用水单位" + useWaterSelfDefinePlan.getUnitCode() +
              "(" + useWaterSelfDefinePlan.getUnitName() + ")" +
              "自平水量申请,第一季度计划:" + useWaterSelfDefinePlan.getFirstQuarter() +
              " 方,第二季度计划:" + useWaterSelfDefinePlan.getSecondQuarter() +
              "方,第三季度计划:" + useWaterSelfDefinePlan.getThirdQuarter() +
              "方,第四季度计划:" + useWaterSelfDefinePlan.getFourthQuarter() + "方,已执行。";
      messageService.messageToUnit(useWaterSelfDefinePlan.getUnitCode(), messageContent, "自平计划执行");
      try {
//        短信通知用水单位
        smsService.sendMsgToUnit(user, useWaterSelfDefinePlan.getUnitCode(), messageContent, "计划通知");
//      webSocket消息推送
        webSocketUtil.pushPublicNews(useWaterSelfDefinePlan.getNodeCode(),useWaterSelfDefinePlan.getUnitCode());
      } catch (Exception e) {
        log.error("用水计划自平审核消息推送失败");
      }
    }
    if (zp > 0 && planAdd ) {
      response.setCode(200);
      systemLogService.logInsert(user,"用水计划自平","用水计划自平执行","");
//      取消待办
      for (String id : ids){
      todoService.edit(id, user.getNodeCode(), user.getId());
      }
      return response;
    } else {
      response.recordError("操作失败");
      return response;
    }
  }
}