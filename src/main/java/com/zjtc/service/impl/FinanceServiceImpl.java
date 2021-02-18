package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.TimeUtil;
import com.zjtc.mapper.FinanceMapper;
import com.zjtc.model.Finance;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.service.FinanceService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/24
 */
@Service
@Slf4j
public class FinanceServiceImpl extends ServiceImpl<FinanceMapper, Finance> implements
    FinanceService {


  @Override
  public ApiResponse insertFinance(List<Finance> finances, String nodeCode) {
    ApiResponse response = new ApiResponse();
    if (finances.size() == 0 || StringUtils.isBlank(nodeCode)) {
      response.setMessage("系统错误");
      return response;
    }
//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    for (Finance finance : finances) {
//      Date paymentDate = finance.getPaymentDate();
//      Date date = TimeUtil.formatTimeToDate(dateFormat.format(paymentDate));
//      finance.setPaymentDate(date);
      finance.setDeleted("0");
      finance.setNodeCode(nodeCode);
    }
    boolean b = this.insertBatch(finances);
    if (b) {
      response.setCode(200);
      return response;
    }
    return response;
  }

  @Override
  public ApiResponse updateFinance(Finance finance) {
    ApiResponse response = new ApiResponse();
    if (null == finance) {
      response.setMessage("系统错误");
      return response;
    }
    String invoiceState = finance.getInvoiceState();
    if ("1".equals(invoiceState)) {
      response.setMessage("单位名称为:" + finance.getUnitName() + "的数据已开票不能修改");
      return response;
    }
    boolean b = this.updateById(finance);
    if (b) {
      response.setCode(200);
      return response;
    }
    return response;
  }

  @Override
  public ApiResponse deletedFinance(List<String> ids) {
    ApiResponse response = new ApiResponse();
    if (ids.size() == 0) {
      response.setMessage("系统错误");
      return response;
    }
    EntityWrapper<Finance> wrapper = new EntityWrapper<>();
    wrapper.in("id", ids);
    int b = 0;
    List<Finance> finances = this.selectList(wrapper);
    for (Finance finance : finances) {
      String invoiceState = finance.getInvoiceState();
      if ("1".equals(invoiceState)) {
        response.recordError("单位名称为:" + finance.getUnitName() + "的数据已开票不能删除");
        return response;
      }
    }
    for (String id : ids) {
      b = this.baseMapper.updateFinanceDeleted(id);
    }
    if (b > 0) {
      response.setCode(200);
      return response;
    } else {
      response.setCode(500);
      return response;
    }
  }

  @Override
  public ApiResponse queryPageFinance(JSONObject jsonObject, String nodeCode) {
    ApiResponse response = new ApiResponse();
    if (null == jsonObject || StringUtils.isBlank(nodeCode)) {
      response.setMessage("系统错误");
      return response;
    }
    Map<String, Object> map = new LinkedHashMap<>(10);
    //    查询条件
//    缴费单位
    String unitName = "";
    if (null != jsonObject.getString("unitName")) {
      unitName = jsonObject.getString("unitName").trim();
    }

//    到账时间开始
    Date paymentDateBegin = null;
    if (null != jsonObject.getDate("paymentDateBegin")) {
      paymentDateBegin = jsonObject.getDate("paymentDateBegin");
    }

//    到账时间结束
    Date paymentDateFinish = null;
    if (null != jsonObject.getDate("paymentDateFinish")) {
      paymentDateFinish = jsonObject.getDate("paymentDateFinish");
    }

//    金额
    String money = "";
    if (null != jsonObject.getString("money")) {
      money = jsonObject.getString("money").trim();
    }

//    是否开票
    String invoiceState = "";
    if (null != jsonObject.getString("invoiceState")) {
      invoiceState = jsonObject.getString("invoiceState").trim();
    }

//    开票人
    String drawer = "";
    if (null != jsonObject.getString("drawer")) {
      drawer = jsonObject.getString("drawer").trim();
    }

//    页数
    Integer currPage = jsonObject.getInteger("current");
//    条数
    Integer pageSize = jsonObject.getInteger("size");

    if (StringUtils.isBlank(nodeCode)) {
      return null;
    }
//    总条数
    Integer total = this.baseMapper
        .selectCountAll(nodeCode, unitName, paymentDateBegin, paymentDateFinish, money,
            invoiceState, drawer);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<Finance> templates = this.baseMapper
        .queryList(currPage, pageSize, nodeCode, unitName, paymentDateBegin, paymentDateFinish,
            money, invoiceState, drawer);
//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    for (Finance finance : templates) {
//      Date paymentDate = finance.getPaymentDate();
//      Date date = TimeUtil.formatTimeToDate(dateFormat.format(paymentDate));
//      finance.setPaymentDate(date);
//    }
//    查询未开票金额和已开票金额
    List<String> sumMoney = this.baseMapper
        .countMoney(unitName, paymentDateBegin, paymentDateFinish, drawer, nodeCode);
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", templates);
    map.put("sumMoney", sumMoney);
    response.setData(map);
    return response;
  }

  @Override
  public ApiResponse countMoney(JSONObject jsonObject, String nodeCode) {
    ApiResponse response = new ApiResponse();
    if (null == jsonObject || StringUtils.isBlank(nodeCode)) {
      response.setMessage("系统错误");
      return response;
    }

    //    缴费单位
    String unitName = "";
    if (null != jsonObject.getString("unitName")) {
      unitName = jsonObject.getString("unitName").trim();
    }

//    到账时间开始
    Date paymentDateBegin = null;
    if (null != jsonObject.getDate("paymentDateBegin")) {
      paymentDateBegin = jsonObject.getDate("paymentDateBegin");
    }

//    到账时间结束
    Date paymentDateFinish = null;
    if (null != jsonObject.getDate("paymentDateFinish")) {
      paymentDateFinish = jsonObject.getDate("paymentDateFinish");
    }

//    开票人
    String drawer = "";
    if (null != jsonObject.getString("drawer")) {
      drawer = jsonObject.getString("drawer").trim();
    }

    List<String> money = this.baseMapper
        .countMoney(unitName, paymentDateBegin, paymentDateFinish, drawer, nodeCode);
    response.setData(money);
    return response;
  }
}
