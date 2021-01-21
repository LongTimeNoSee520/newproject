package com.zjtc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.mapper.WaterUsePayInfoMapper;
import com.zjtc.model.FlowNodeInfo;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.RefundOrRefundService;
import com.zjtc.service.WaterUsePayInfoService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    WaterUsePayInfo entity = jsonObject.toJavaObject(WaterUsePayInfo.class);
    boolean result = this.insert(entity);
    return result;
  }

  @Override
  public boolean updateModel(JSONObject jsonObject) {
    //todo:绑定发票号
    WaterUsePayInfo entity = jsonObject.toJavaObject(WaterUsePayInfo.class);
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
  public boolean toStartRefund(JSONObject jsonObject, User user) {
    String nodeCode = jsonObject.getString("nodeCode");
    RefundOrRefund refundOrRefund=jsonObject.toJavaObject(RefundOrRefund.class);
    //退减免单单新增一条数据
    refundOrRefundService.insert(refundOrRefund);
    //发起审核流程
    //创建流程节点记录表
    //流程节点线记录表
    flowNodeInfoService.selectAndInsert(nodeCode,refundOrRefund.getId(), AuditConstants.PAY_FLOW_CODE);
    //流程进度（操作记录）表 新增两条数据
   // flowProcessService.create(user,refundOrRefund.getId(),) ;
    //1：发起人，2：下一环节审核人
    //发起待办
    //流程实例表 todo:
    return false;
  }

  @Override
  public boolean toStartReduction(JSONObject jsonObject, User user) {
    return false;
  }

  @Override
  public boolean updateinvoiceNumRef(WaterUsePayInfo waterUsePayInfo) {
    return this.updateById(waterUsePayInfo);
  }


}