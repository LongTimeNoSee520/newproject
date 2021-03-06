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
    /**??????????????????*/
   EndPaper endPaper = endPaperService.getById(businessId);
    String  passContent =
        "[????????????" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "??????????????????"
            + endPaper.getAddNumber()+ "???(????????????"
            + endPaper.getFirstWater() + "??????????????????" +endPaper.getSecondWater() + "???)]???????????????,?????????????????????????????????????????????";
    String  passContent1 =
        "????????????[????????????" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "??????????????????"
            + endPaper.getAddNumber()+ "???(????????????"
            + endPaper.getFirstWater() + "??????????????????" +endPaper.getSecondWater() + "???)]???????????????????????????";

    String  unPassContent1 =
        "????????????[????????????" + endPaper.getUnitCode() + "(" + endPaper.getUnitName() + ")" + "??????????????????"
            + endPaper.getAddNumber()+ "???(????????????"
            + endPaper.getFirstWater() + "??????????????????" +endPaper.getSecondWater() + "???)]??????????????????????????????";
    if ("1".equals(pass)){//??????
      /**?????????????????????*/
      endPaper.setAuditStatus("1");
      endPaperService.updateById(endPaper);
      if ("1".equals(endPaper.getDataSources())){
        UseWaterPlanAddWX waterPlanAddWX = new UseWaterPlanAddWX();
        waterPlanAddWX.setId(endPaper.getWaterPlanWXId());
        //???????????????(?????????????????????)????????????????????????
        waterPlanAddWX.setAuditStatus("4");//??????????????????????????????????????????????????????
        useWaterPlanAddWXService.update(waterPlanAddWX,user);
        /**???????????????????????????*/
        messageService.messageToUnit(endPaper.getUnitCode(), passContent,
            AuditConstants.END_PAPER_TODO_TITLE);
        /**???????????????????????????*/
        smsService.sendMsgToUnit(user,endPaper.getUnitName(), endPaper.getUnitCode(), passContent, "????????????");
        // webSocket????????????????????????
        webSocketUtil.pushPublicNews(endPaper.getNodeCode(),endPaper.getUnitCode());
      }
      /**????????????????????????*/
      FlowProcess firstProcess = flowProcessService.selectFirstRecords(businessId);
      //???????????????
      messageService
          .add(endPaper.getNodeCode(), firstProcess.getOperatorId(), firstProcess.getOperator(),
              AuditConstants.END_PAPER_MESSAGE_TYPE, passContent1,businessId);
      smsService
          .sendMsgToPromoter(user, firstProcess.getOperatorId(), firstProcess.getOperator(),
              passContent1, "????????????");
      //webSocket??????????????????
      webSocketUtil.pushWaterNews(firstProcess.getNodeCode(),firstProcess.getOperatorId());
    }
   else if("1".equals(pass)){
      FlowProcess firstProcess = flowProcessService.selectFirstRecords(businessId);
      /**????????????????????????*/
      //???????????????
      messageService
          .add(endPaper.getNodeCode(), firstProcess.getOperatorId(), firstProcess.getOperator(),
              AuditConstants.END_PAPER_MESSAGE_TYPE, unPassContent1,businessId);
      smsService
          .sendMsgToPromoter(user, firstProcess.getOperatorId(), firstProcess.getOperator(),
              unPassContent1, "????????????");
      //webSocket??????????????????
      webSocketUtil.pushWaterNews(firstProcess.getNodeCode(),firstProcess.getOperatorId());
    }
    /**?????????????????????*/
    this.edit(businessId,user.getNodeCode(),user.getId());
  }

  @Override
  public List<Map<String,Object>> queryList(User user) {

    return this.baseMapper.queryList(user.getId(),user.getNodeCode());
  }


}