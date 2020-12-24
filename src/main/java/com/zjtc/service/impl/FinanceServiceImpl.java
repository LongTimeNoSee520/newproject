package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.FinanceMapper;
import com.zjtc.model.Finance;
import com.zjtc.service.FinanceService;
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
    for (Finance finance : finances) {
      finance.setDeleted("0");
    }
    boolean b = this.insertBatch(finances);
    if (b) {
      response.setCode(200);
      return response;
    }
    return response;
  }

  @Override
  public ApiResponse updateFinance(List<Finance> finances) {
    ApiResponse response = new ApiResponse();
    if (finances.size() == 0) {
      response.setMessage("系统错误");
      return response;
    }
    for (Finance finance : finances) {
      String invoiceState = finance.getInvoiceState();
      if ("1".equals(invoiceState)) {
        response.setMessage("单位名称为:" + finance.getUnitName() + "的数据已开票不能修改");
        return response;
      }
    }
    boolean b = this.insertBatch(finances);
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
//    Finance finance1 = new Finance();
//    List<Finance> financeList = new ArrayList<>(10);
////    先查询到所有的加价费开票记录数据
//    EntityWrapper<Finance> wrapper = new EntityWrapper<>();
//    wrapper.eq("deleted", "0");
//    List<Finance> finances = this.baseMapper.selectList(wrapper);
////    依次遍历加价费开票记录数据
//    for (Finance finance : finances) {
////      依次遍历传入的需要删除的id
//      for (String id : ids) {
////        匹配加价费开票记录数据的id
//        if (finance.getId().equals(id)) {
////          匹配到后判断加价费开票记录数据的是否开票状态
//          if (finance.getInvoiceState() == 1) {
//            response.setMessage("缴费为:" + finance.getUnitName() + "的数据已开票不能删除");
//            return response;
//          }
//        }
//      }
//    }
//    for (String id : ids) {
//      finance1.setId(id);
//      finance1.setDeleted("1");
//      financeList.add(finance1);
//    }
//    boolean b = this.updateBatchById(financeList);
//    if (b) {
//      response.setCode(200);
//      return response;
//    }
//    for (String id : ids){
//
//    }
    EntityWrapper<Finance> wrapper = new EntityWrapper<>();
    wrapper.in("id", ids);
    int b = 0;
    char a = 1;
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
    Integer total = this.baseMapper.selectCountAll(nodeCode,unitName,paymentDateBegin,paymentDateFinish,money,invoiceState,drawer);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<Finance> templates = this.baseMapper
        .queryList(currPage, pageSize, nodeCode,unitName,paymentDateBegin,paymentDateFinish,money,invoiceState,drawer);
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", templates);
    response.setCode(200);
    response.setMessage("分页查询成功");
    response.setData(map);
    return response;
  }

  @Override
  public ApiResponse countMoney(String nodeCode) {
    return null;
  }
}
