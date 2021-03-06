package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.mapper.waterBiz.RefundOrRefundMapper;
import com.zjtc.mapper.waterSys.FlowProcessMapper;
import com.zjtc.model.File;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.service.FileService;
import com.zjtc.service.FlowExampleService;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.MessageService;
import com.zjtc.service.RefundOrRefundService;
import com.zjtc.service.SmsService;
import com.zjtc.service.TodoService;
import com.zjtc.service.WaterUsePayInfoService;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefundOrRefund的服务接口的实现类
 *
 * @author
 */
@Service
public class RefundOrRefundServiceImpl extends
    ServiceImpl<RefundOrRefundMapper, RefundOrRefund> implements
    RefundOrRefundService {

  @Autowired
  private FlowNodeInfoService flowNodeInfoService;
  @Autowired
  private FlowProcessService flowProcessService;
  @Autowired
  private TodoService todoService;
  @Autowired
  private FlowExampleService flowExampleService;
  @Autowired
  private WaterUsePayInfoService waterUsePayInfoService;
  @Autowired
  private FileService fileService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private SmsService smsService;
  @Autowired
  private FlowProcessMapper flowProcessMapper;
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

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    boolean result = this.insert(entity);
    return result;
  }

  @Override
  public boolean updateModel(JSONObject jsonObject) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    boolean flag = true;
    if (!entity.getSysFiles().isEmpty()) {
      flag = fileService.updateBusinessId(entity.getId(), entity.getSysFiles());
    }
    return this.updateById(entity) && flag;
  }

  @Override
  public boolean deleteModel(JSONObject jsonObject) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    boolean result = this.deleteById(entity);
    return result;
  }

  @Override
  public List<RefundOrRefund> queryAll(JSONObject jsonObject) {
    String startTime = jsonObject.getString("startTime");
    String endTime = jsonObject.getString("endTime");
    if (StringUtils.isNotBlank(startTime)) {
      jsonObject.put("startTime", startTime + " 00:00:00");
    }
    if (StringUtils.isNotBlank(endTime)) {
      jsonObject.put("endTime", endTime + " 23:59:59");
    }
    List<RefundOrRefund> list = baseMapper.queryAll(jsonObject);
    if (!list.isEmpty()) {
      for (RefundOrRefund refundOrRefund : list) {
        if (!refundOrRefund.getSysFiles().isEmpty()) {
          for (File file : refundOrRefund.getSysFiles()) {
            file.setUrl(preViewRealPath + contextPath + "/" + file.getFilePath());
          }
        }
      }
    }
    return list;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    String startTime = jsonObject.getString("startTime");
    String endTime = jsonObject.getString("endTime");
    jsonObject.put("flowCode", AuditConstants.PAY_FLOW_CODE);
    if (StringUtils.isNotBlank(startTime)) {
      jsonObject.put("startTime", startTime + " 00:00:00");
    }
    if (StringUtils.isNotBlank(endTime)) {
      jsonObject.put("endTime", endTime + " 23:59:59");
    }
    List<RefundOrRefund> list = baseMapper.queryPage(jsonObject);
    if (!list.isEmpty()) {
      for (RefundOrRefund refundOrRefund : list) {
        refundOrRefund.setAuditFlow(
            flowProcessMapper.queryAuditList(refundOrRefund.getId(), refundOrRefund.getNodeCode()));
        if (!refundOrRefund.getSysFiles().isEmpty()) {
          for (File file : refundOrRefund.getSysFiles()) {
            file.setUrl(preViewRealPath + contextPath + "/" + file.getFilePath());
          }
        }
      }
    }
    page.put("records", list);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //查询总数据条数
    long total = baseMapper.queryListTotal(jsonObject);
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean audit(JSONObject jsonObject, User user) throws Exception {
    //退减免单id
    String id = jsonObject.getString("id");
    RefundOrRefund entity = this.selectById(id);
    //审核内容
    String content = jsonObject.getString("content");
    //下一环节审核人id
    String nextPersonId = jsonObject.getString("nextPersonId");
    //下一环节审核人
    String nextPersonName = jsonObject.getString("nextPersonName");
    //审核传入的判定 1:同意,2 不同意
    String auditBtn = jsonObject.getString("auditBtn");
    String businessJson = jsonObject.getString("businessJson");
    String detailConfig = jsonObject.getString("detailConfig");
    //查询下一环节
    List<Map<String, Object>> hasNext = flowNodeInfoService
        .nextAuditRole(id, AuditConstants.PAY_TABLE, user.getNodeCode(), auditBtn);
    //获取当前环节的审核操作记录
    FlowProcess flowProcess = flowProcessService.getLastData(user.getNodeCode(), entity.getId());
    //查询流程的发起人
    FlowProcess firstProcess = flowProcessService.selectFirstRecords(id);
    /**当前环节为最后环节：审核流程结束*/
    if (hasNext.isEmpty()) {
      //不通过
      if ("0".equals(auditBtn)) {
        entity.setStatus(AuditConstants.NOT_APPROVED);
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
      }
      //通过
      if ("1".equals(auditBtn)) {
        entity.setStatus(AuditConstants.GET_APPROVED);
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
        //审核开始，更改审核状态
      }
      /**1.修改当前退减免单：下一环节id、审核人、审核时间、审核状态*/
      entity.setAuditPerson(user.getId());
      entity.setAuditTime(new Date());
      entity.setNextNodeId("");
      this.updateById(entity);
      /**2.修改当前审核操作记录表：当前环节审核状态，操作时间*/
      flowProcess.setOperationTime(new Date());
      flowProcess.setAuditContent(content);
      flowProcessService.updateById(flowProcess);
      /**3.修改代办表：状态改为已办*/
      todoService.edit(entity.getId(), user.getNodeCode(), user.getId());
      /**4.修改实例表数据：流转状态*/
      flowExampleService.edit(user.getNodeCode(), entity.getId());
      if ("1".equals(auditBtn)) {
        /**如果是退款单，修改过实收*/
        WaterUsePayInfo waterUsePayInfo = waterUsePayInfoService.selectById(entity.getPayId());
        if ("1".equals(entity.getType()) && !waterUsePayInfo.getActualAmount()
            .equals(entity.getActualAmount())) {
          waterUsePayInfoService.updateActualAmount(entity.getPayId(), entity.getActualAmount());
        }
        /**5.修改缴费记录表数据(退款/减免金额)：修改应收、实收金额,是否修改过实收*/
        waterUsePayInfoService.updateMoney(entity.getPayId(), entity.getMoney());
        /**6.流程结束：通知发起人*/
        String messageContent = "";
        if ("1".equals(entity.getType())) {
          messageContent =
              "您发起的[用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ")" + "申请退款"
                  + entity
                  .getMoney() + " 元，审核已通过。";
        }
        if ("2".equals(entity.getType())) {
          messageContent =
              "您发起的[用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ")" + "申请减免"
                  + entity
                  .getMoney() + " 元，审核已通过。";
        }
        messageService
            .add(user.getNodeCode(), firstProcess.getOperatorId(), firstProcess.getOperator(),
                AuditConstants.PAY_MESSAGE_TYPE, messageContent);
        /**短信通知发起人:*/
        smsService
            .sendMsgToPromoter(user, firstProcess.getOperatorId(), firstProcess.getOperator(),
                messageContent,
                "退减免通知");
      }
    } else {
      /**审核流程继续*/
      /**1.修改当前退减免单:下一环节id*/
      String nextFlowNodeId = hasNext.get(0).get("flowNodeId").toString();
      entity.setNextNodeId(nextFlowNodeId);
      /**2.流程操作记录表：修改当前环节审核状态*/
      if ("0".equals(auditBtn)) {
        //不通过
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
        //下一环节是第一环节
        if (flowNodeInfoService.isFirst(nextFlowNodeId)) {
          entity.setStatus("0");
        }
      }
      if ("1".equals(auditBtn)) {
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
        //审核开始，更改审核状态
        entity.setStatus("1");
      }
      this.updateById(entity);
      flowProcess.setAuditContent(content);
      flowProcessService.updateById(flowProcess);
      /**3.流程操作记录表:新增下一环节操作记录*/
      flowProcessService.add(user.getNodeCode(), entity.getId(), nextPersonName, nextPersonId);
      /**4：待办表：修改当前待办状态*/
      todoService.edit(entity.getId(), user.getNodeCode(), user.getId());
      /**待办表：新增一条待办*/
      String todoContent = "";
      if ("1".equals(entity.getType())) {
        todoContent =
            "用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ") 申请退款" + entity
                .getMoney()
                + "元";
      }
      if ("2".equals(entity.getType())) {
        todoContent =
            "用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ") 申请减免" + entity
                .getMoney()
                + "元";
      }
      todoService.add(entity.getId(), user, nextPersonId, nextPersonName, todoContent, JSONObject.toJSONString(entity),
          detailConfig, AuditConstants.PAY_TODO_TYPE);
    }
    return true;
  }

  @Override
  public boolean revoke(JSONObject jsonObject) {
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    Wrapper wrapper = new EntityWrapper();
    wrapper.in("id", ids);
    RefundOrRefund refundOrRefund = new RefundOrRefund();
    refundOrRefund.setIsRevoke("1");
    boolean result = this.update(refundOrRefund, wrapper);
    /**更新撤销状态*/
    /**删除待办数据*/
    return result && todoService.deleteBatchIds(ids);
  }

  @Override
  public boolean auditCount(String payId, String nodeCode) {
    Wrapper entityWrapper = new EntityWrapper();
    entityWrapper.eq("pay_id", payId);
    entityWrapper.eq("node_code", nodeCode);
    entityWrapper.eq("status", AuditConstants.AWAIT_APPROVED);
    return this.selectCount(entityWrapper) > 0 ? true : false;
  }

  @Override
  public List<Map<String, Object>> nextAuditRole(String id, String nodeCode, String auditBtn) {
    return flowNodeInfoService.nextAuditRole(id, AuditConstants.PAY_TABLE, nodeCode, auditBtn);
  }

}