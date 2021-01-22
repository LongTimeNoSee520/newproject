package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.mapper.RefundOrRefundMapper;
import com.zjtc.model.FlowExample;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.User;
import com.zjtc.service.FlowExampleService;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.RefundOrRefundService;
import com.zjtc.service.TodoService;
import com.zjtc.service.WaterUsePayInfoService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    boolean result = this.insert(entity);
    return result;
  }

  @Override
  public boolean updateModel(JSONObject jsonObject) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    boolean result = this.updateById(entity);
    return result;
  }

  @Override
  public boolean deleteModel(JSONObject jsonObject) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    boolean result = this.deleteById(entity);
    return result;
  }

  @Override
  public List<RefundOrRefund> queryAll(JSONObject jsonObject) {
    List<RefundOrRefund> list = baseMapper.queryAll(jsonObject);
    return list;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean audit(JSONObject jsonObject, User user) {
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
    List<Map<String, Object>> hasNext = flowNodeInfoService
        .nextAuditRole(id, AuditConstants.PAY_TABLE, user.getNodeCode(), auditBtn);
    //最后环节
    //获取当前环节的审核操作记录
    FlowProcess flowProcess = flowProcessService.getLastData(user.getNodeCode(), entity.getId());
    if (hasNext.isEmpty() && "1".equals(auditBtn)) {
      //审核流程结束
      //修改当前退减免单下一环节id
      entity.setNextNodeId("");
      this.updateById(entity);
      //操作记录修改状态
      //不通过
      if ("0".equals(auditBtn)) {
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
      }
      if ("1".equals(auditBtn)) {
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
      }
      flowProcessService.updateById(flowProcess);
      //修改代办状态
      todoService.edit(entity.getId(), user.getNodeCode(), user.getId());
      //修改实例表数据
      flowExampleService.edit(user.getNodeCode(), user.getId());
      //修改缴费记录
      waterUsePayInfoService.updateMoney(entity.getPayId(), entity.getMoney());
      //发起通知 todo:
    } else {
      //审核流程继续
      //修改当前退减免单下一环节id
      entity.setNextNodeId(hasNext.get(0).get("flowNodeId").toString());
      this.updateById(entity);
      //当前操作记录修改状态
      if ("0".equals(auditBtn)) {
        flowProcess.setAuditStatus(AuditConstants.GET_APPROVED);
      }
      if ("1".equals(auditBtn)) {
        flowProcess.setAuditStatus(AuditConstants.NOT_APPROVED);
      }
      flowProcessService.updateById(flowProcess);
      //新增下一环节操作记录
      flowProcessService.add(user.getNodeCode(), entity.getId(), nextPersonId, nextPersonName);
      //修改待办状态
      todoService.edit(entity.getId(), user.getNodeCode(), user.getId());
      //新增一条待办
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
      todoService.add(entity.getId(), user, nextPersonId, nextPersonName, todoContent, businessJson,
          detailConfig, AuditConstants.PAY_TODO_TYPE);
    }
    return true;
  }

  @Override
  public boolean revoke(JSONObject jsonObject) {
    //
    return false;
  }

  @Override
  public boolean auditCount(String payId,String nodeCode) {
    Wrapper entityWrapper=new EntityWrapper();
    entityWrapper.eq("pay_id",payId);
    entityWrapper.eq("node_code",nodeCode);
    entityWrapper.eq("status",AuditConstants.AWAIT_APPROVED);
    return this.selectCount(entityWrapper)>0 ? true: false;
  }

}