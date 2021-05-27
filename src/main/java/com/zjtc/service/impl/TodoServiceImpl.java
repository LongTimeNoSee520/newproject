package com.zjtc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.util.DictUtils;
import com.zjtc.base.util.WebSocketUtil;
import com.zjtc.mapper.waterBiz.TodoMapper;
import com.zjtc.model.EndPaper;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.Todo;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.User;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.MessageService;
import com.zjtc.service.SmsService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterPlanAddWXService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

  @Autowired
  private EndPaperService endPaperService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private SmsService smsService;
  @Autowired
  private FlowProcessService flowProcessService;
  @Autowired
  private UseWaterPlanAddWXService useWaterPlanAddWXService;
  @Autowired
  private DictUtils dictUtils;
  @Autowired
  private WebSocketUtil webSocketUtil;
  @Override
  public void add(String businessId, User user, String auditorId, String auditorName,
      String todoContent, String businessJson, String detailConfig, String todoType) {

    Todo todo = new Todo();
    todo.setNodeCode(user.getNodeCode());
    todo.setExecutePersonId(auditorId);
    todo.setExecutePersonName(auditorName);
    todo.setTodoContent(todoContent);
    todo.setTodoType(todoType);
    if (AuditConstants.PAY_TODO_TYPE.equals(todoType)){
      todo.setTodoTitle(AuditConstants.PAY_TODO_TITLE);
      todo.setTableName(AuditConstants.PAY_TABLE);
    } else if (AuditConstants.END_PAPER_TODO_TYPE_AJUST.equals(todoType)
        || AuditConstants.END_PAPER_TODO_TYPE_ADD.equals(todoType)) {
      todo.setTodoTitle(AuditConstants.END_PAPER_TODO_TITLE);
      todo.setTableName(AuditConstants.END_PAPER_TABLE);
    }
    todo.setBusinessJson(businessJson);
    todo.setBusinessId(businessId);
    todo.setRegistrant(user.getUsername());
    todo.setRegistrantId(user.getId());
    todo.setDetailConfig(detailConfig);
    todo.setStatus(AuditConstants.BEFORE_TODO_STATUS);
    todo.setCreateTime(new Date());
    todo.setSubmitNodeCode(user.getNodeCode());
    String nodeName = dictUtils.getDictItemName("area",user.getNodeCode(),user.getNodeCode());
    todo.setSubmitNodeName(nodeName);
    this.baseMapper.insert(todo);
  }

  @Override
  public boolean edit(String businessId, String nodeCode,String executePersonId) {
//    QueryWrapper wrapper=new QueryWrapper();
//    wrapper.eq("business_id",businessId);
//    wrapper.eq("node_code",nodeCode);
//    wrapper.eq("execute_person_id",executePersonId);
//    Todo todo= this.getOne(wrapper);
    Todo todo=  this.baseMapper.selectTodoModel(businessId,nodeCode,executePersonId);
    todo.setStatus(AuditConstants.AFTER_TODO_STATUS);
    todo.setOperationTime(new Date());
    return this.updateById(todo);
  }

  @Override
  public boolean editByBusinessId(String businessId) {
    return this.baseMapper.editByBusinessId(businessId,AuditConstants.AFTER_TODO_STATUS);
  }

  @Override
  public boolean deleteByBusinessId(String businessId) {
    return this.baseMapper.deleteByBusinessId(businessId);
  }

  @Override
  public void report(User user, Todo todo) {
       todo.setCreateTime(new Date());
       todo.setStatus(AuditConstants.BEFORE_TODO_STATUS);
       this.baseMapper.insert(todo);
  }

  @Override
  public void edit(User user,String businessId, String pass) throws Exception {
    /**查询业务数据*/
   EndPaper endPaper = endPaperService.getById(businessId);
    String  passContent =
        "[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
            + endPaper.getAddNumber()+ "方(第一水量"
            + endPaper.getFirstWater() + "方，第二水量" +endPaper.getSecondWater() + "方)]审核已通过,请到公共服务平台或微信端确认。";
    String  passContent1 =
        "您发起的[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
            + endPaper.getAddNumber()+ "方(第一水量"
            + endPaper.getFirstWater() + "方，第二水量" +endPaper.getSecondWater() + "方)]办结单审核已通过。";

    String  unPassContent1 =
        "您发起的[用水单位" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "申请增加计划"
            + endPaper.getAddNumber()+ "方(第一水量"
            + endPaper.getFirstWater() + "方，第二水量" +endPaper.getSecondWater() + "方)]办结单未通过市审核。";
    if ("1".equals(pass)){//同意
      /**更新主数据状态*/
      endPaper.setAuditStatus("1");
      endPaperService.updateById(endPaper);
      if ("1".equals(endPaper.getDataSources())){
        UseWaterPlanAddWX waterPlanAddWX = new UseWaterPlanAddWX();
        waterPlanAddWX.setId(endPaper.getWaterPlanWXId());
        //只有通过时(且审核流程走完)，不通过则不修改
        waterPlanAddWX.setAuditStatus("4");//微信端提交审核通过后办结单审核也通过
        useWaterPlanAddWXService.update(waterPlanAddWX,user);
        /**新增通知给用水单位*/
        messageService.messageToUnit(endPaper.getUnitCode(), passContent,
            AuditConstants.END_PAPER_TODO_TITLE);
        /**短信通知给用水单位*/
        smsService.sendMsgToUnit(user,endPaper.getUnitName(), endPaper.getUnitCode(), passContent, "计划通知");
        // webSocket推送到公共服务端
        webSocketUtil.pushPublicNews(endPaper.getNodeCode(),endPaper.getUnitCode());
      }
      /**短信通知给发起人*/
      FlowProcess firstProcess = flowProcessService.selectFirstRecords(businessId);
      //通知表新增
      messageService
          .add(endPaper.getNodeCode(), firstProcess.getOperatorId(), firstProcess.getOperator(),
              AuditConstants.END_PAPER_MESSAGE_TYPE, passContent1,businessId);
      smsService
          .sendMsgToPromoter(user, firstProcess.getOperatorId(), firstProcess.getOperator(),
              passContent1, "计划通知");
      //webSocket推送给发起人
      webSocketUtil.pushWaterNews(firstProcess.getNodeCode(),firstProcess.getOperatorId());
    }
   else if("1".equals(pass)){
      FlowProcess firstProcess = flowProcessService.selectFirstRecords(businessId);
      /**短信通知给发起人*/
      //通知表新增
      messageService
          .add(endPaper.getNodeCode(), firstProcess.getOperatorId(), firstProcess.getOperator(),
              AuditConstants.END_PAPER_MESSAGE_TYPE, unPassContent1,businessId);
      smsService
          .sendMsgToPromoter(user, firstProcess.getOperatorId(), firstProcess.getOperator(),
              unPassContent1, "计划通知");
      //webSocket推送给发起人
      webSocketUtil.pushWaterNews(firstProcess.getNodeCode(),firstProcess.getOperatorId());
    }
    /**更新待办为已读*/
    this.edit(businessId,user.getNodeCode(),user.getId());
  }

  @Override
  public List<Map<String,Object>> queryList(User user) {

    return this.baseMapper.queryList(user.getId(),user.getNodeCode());
  }


}