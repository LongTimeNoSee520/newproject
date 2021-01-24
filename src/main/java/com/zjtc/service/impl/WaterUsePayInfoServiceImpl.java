package com.zjtc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.WaterUsePayInfoMapper;
import com.zjtc.model.FlowNodeInfo;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.UseWaterUnitInvoice;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.service.FlowExampleService;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.RefundOrRefundService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterUnitInvoiceService;
import com.zjtc.service.WaterUsePayInfoService;
import io.swagger.annotations.ApiParam;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * WaterUsePayInfo的服务接口的实现类
 *
 * @author
 */
@Service
public class WaterUsePayInfoServiceImpl extends
    ServiceImpl<WaterUsePayInfoMapper, WaterUsePayInfo> implements
    WaterUsePayInfoService {

  @Autowired
  private FlowNodeInfoService flowNodeInfoService;
  @Autowired
  private RefundOrRefundService refundOrRefundService;
  @Autowired
  private FlowProcessService flowProcessService;
  @Autowired
  private TodoService todoService;
  @Autowired
  private FlowExampleService flowExampleService;
  @Autowired
  private UseWaterUnitInvoiceService useWaterUnitInvoiceService;

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    WaterUsePayInfo entity = jsonObject.toJavaObject(WaterUsePayInfo.class);
    boolean result = this.insert(entity);
    return result;
  }

  @Override
  public boolean updateModel(JSONObject jsonObject, User user) {
    WaterUsePayInfo entity = jsonObject.toJavaObject(WaterUsePayInfo.class);
    String invoiceId = jsonObject.getString("invoiceId");
    //勾选了财务转账、现金复核
    if ("1".equals(entity.getCashCheck()) || "1".equals(entity.getTransferCheck())) {
      entity.setPayStatus("5");
    }
    //勾选了托收缴费状态
    if ("1".equals(entity.getPayStatus())) {
      entity.setPayStatus("1");
    }
    if (StringUtils.isNotBlank(invoiceId)) {
      /**绑定发票号*/
      UseWaterUnitInvoice useWaterUnitInvoice = new UseWaterUnitInvoice();
      useWaterUnitInvoice.setInvoiceDate(new Date());
      useWaterUnitInvoice.setPayInfoId(entity.getId());
      useWaterUnitInvoiceService
          .updateInvoicesUnitMessage(useWaterUnitInvoice, user.getUsername(), user.getNodeCode());
    }
    boolean result = this.updateById(entity);
    return result;
  }

  @Override
  public boolean deleteModel(JSONObject jsonObject) {
    WaterUsePayInfo entity = jsonObject.toJavaObject(WaterUsePayInfo.class);
    boolean result = this.deleteById(entity);
    return result;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    List<Map<String, Object>> result = baseMapper.queryPage(jsonObject);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //查询总数据条数
    Map<String, Object> sumData = baseMapper.queryListTotal(jsonObject);
    Integer total = (int) sumData.get("counts");
    page.put("total", sumData.get("counts"));
    page.put("sumData", sumData);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean initPayInfo(JSONObject jsonObject) {
    boolean result = true;
    /**重算加价前先删除之前的数据*/
    //三种情况:1.托收缴费已托收，2.已选择发票，3：有退减免过程
    baseMapper.deleteByParam(jsonObject);
    /**初始化加价*/
    List<WaterUsePayInfo> waterUsePayInfos = baseMapper.initPayInfo(jsonObject);
    if (!waterUsePayInfos.isEmpty()) {
      result = this.insertBatch(waterUsePayInfos);
    }
    return result;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse toStartRefund(JSONObject jsonObject, User user) {
    ApiResponse apiResponse = new ApiResponse();
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    /**判断当前缴费记录是否有退减免流程*/
    if (refundOrRefundService.auditCount(entity.getPayId(), user.getNodeCode())) {
      apiResponse.recordError("当前缴费记录退减免流程尚未结束");
      return apiResponse;
    }
    String nextPersonId = jsonObject.getString("nextPersonId");
    String nextPersonName = jsonObject.getString("nextPersonName");
    String businessJson = jsonObject.getString("businessJson");
    String detailConfig = jsonObject.getString("detailConfig");
    /**查询流程节点记录第一个流程*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .firStAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**退减免单新增一条数据*/
    entity.setNodeCode(user.getNodeCode());
    refundOrRefundService.insert(entity);
    /**流程节点记录表、流程节点线记录表创建数据*/
    String newFirStCodeId = flowNodeInfoService
        .selectAndInsert(user.getNodeCode(), entity.getId(), AuditConstants.PAY_FLOW_CODE,
            firStAudit.get(0).get("flowNodeId").toString());
    entity.setNextNodeId(newFirStCodeId);
    entity.setIsRevoke("0");
    entity.setStatus("1");
    entity.setType("1");
    /**修改业务表数据*/
    refundOrRefundService.updateById(entity);
    /**流程进度（操作记录）表 新增三条数据*/
    flowProcessService
        .create(user, entity.getId(), entity.getTreatmentAdvice(), nextPersonName, nextPersonId);
    /**发起待办*/
    String todoContent =
        "用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ") 申请退款" + entity.getMoney()
            + "元";
    todoService
        .add(entity.getId(), user, nextPersonId, nextPersonName, todoContent, businessJson,
            detailConfig,
            AuditConstants.PAY_TODO_TYPE);
    /**新增流程实例表数据*/
    flowExampleService.add(user, entity.getId(), AuditConstants.PAY_TODO_TYPE);
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse toStartReduction(JSONObject jsonObject, User user) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    ApiResponse apiResponse = new ApiResponse();
    if (refundOrRefundService.auditCount(entity.getPayId(), user.getNodeCode())) {
      apiResponse.recordError("当前缴费记录退减免流程尚未结束");
      return apiResponse;
    }
    String nextPersonId = jsonObject.getString("nextPersonId");
    String nextPersonName = jsonObject.getString("nextPersonName");
    String content = jsonObject.getString("content");
    String businessJson = jsonObject.getString("businessJson");
    String detailConfig = jsonObject.getString("detailConfig");
    /**查询流程节点记录第一个流程*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .firStAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**退减免单新增一条数据*/
    entity.setNodeCode(user.getNodeCode());
    entity.setIsRevoke("0");
    entity.setStatus("1");
    entity.setType("2");
    refundOrRefundService.insert(entity);
    /**流程节点记录表、流程节点线记录表创建数据*/
    String newFirStCodeId = flowNodeInfoService
        .selectAndInsert(user.getNodeCode(), entity.getId(), AuditConstants.PAY_FLOW_CODE,
            firStAudit.get(0).get("flowNodeId").toString());
    entity.setNextNodeId(newFirStCodeId);
    /**修改业务表数据*/
    refundOrRefundService.updateById(entity);
    /**流程进度（操作记录）表 新增三条数据*/
    flowProcessService.create(user, entity.getId(), content, nextPersonName, nextPersonId);
    /**发起待办*/
    String todoContent =
        "用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ") 申请减免" + entity.getMoney()
            + "元";
    todoService
        .add(entity.getId(), user, nextPersonId, nextPersonName, todoContent, businessJson,
            detailConfig,
            AuditConstants.PAY_TODO_TYPE);
    /**新增流程实例表数据*/
    flowExampleService.add(user, entity.getId(), AuditConstants.PAY_TODO_TYPE);
    return apiResponse;
  }

  @Override
  public boolean updateinvoiceNumRef(WaterUsePayInfo waterUsePayInfo) {
    waterUsePayInfo.setInvoiceNum(null);
    waterUsePayInfo.setInvoicePrintTime(null);
    return this.updateById(waterUsePayInfo);
  }

  @Override
  public boolean updateMoney(String id, double moeny) {
    return baseMapper.updateMoney(id, moeny);
  }

  @Override
  public List<Map<String, Object>> firstRole(User user) {
    List<Map<String, Object>> result = flowNodeInfoService
        .firStAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    return result;
  }

  @Override
  public List<Map<String, Object>> findPayBefor(String unitId) {
    return baseMapper.findPayBefor(unitId);
  }

  @Override
  public List<Map<String, Object>> ThreePayMess(String unitId) {
    //查询当年
    Calendar date = Calendar.getInstance();
    int year = Integer.valueOf(date.get(Calendar.YEAR));
    return baseMapper.ThreePayMess(year, unitId);
  }
}