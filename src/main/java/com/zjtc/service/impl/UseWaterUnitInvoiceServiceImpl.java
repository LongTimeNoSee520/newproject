package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterUnitInvoiceMapper;
import com.zjtc.model.UseWaterUnitInvoice;
import com.zjtc.service.UseWaterUnitInvoiceService;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * UseWaterUnitInvoice的服务接口的实现类
 *
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 计划用水户发票表
 */
@Service
public class UseWaterUnitInvoiceServiceImpl extends
    ServiceImpl<UseWaterUnitInvoiceMapper, UseWaterUnitInvoice> implements
    UseWaterUnitInvoiceService {

  @Override
  public ApiResponse saveModel(JSONObject jsonObject, String nodeCode) {
    ApiResponse response = new ApiResponse();
    if (null == jsonObject && StringUtils.isBlank(nodeCode)) {
      response.recordError("系统错误");
      return response;
    }
    List<UseWaterUnitInvoice> unitInvoices = jsonObject.getJSONArray("unitInvoices")
        .toJavaList(UseWaterUnitInvoice.class);
    List<UseWaterUnitInvoice> unitInvoiceList = new ArrayList<>();
//    判断数据是否为空
    if (unitInvoices.isEmpty()) {
      return response;
    }
    for (UseWaterUnitInvoice unitInvoice : unitInvoices) {
      String invoiceNumber = unitInvoice.getInvoiceNumber();
//      查看发票号是否为空
      int i = this.baseMapper.selectInvoiceNumber(invoiceNumber, nodeCode);
      if (i > 0) {
//        i不0的话说明已存在,直接退出
        response.recordError("发票号为:" + invoiceNumber + "已存在");
        return response;
      } else {
        unitInvoice.setCreateTime(new Date());
        unitInvoice.setNodeCode(nodeCode);
        unitInvoice.setDeleted("0");
        unitInvoiceList.add(unitInvoice);
      }
    }
    boolean b = this.insertBatch(unitInvoiceList);
    if (b) {
      response.setCode(200);
      return response;
    }
    return response;
  }

  @Override
  public ApiResponse updateModel(UseWaterUnitInvoice unitInvoice, String userName) {
    ApiResponse response = new ApiResponse();
    if (null == unitInvoice) {
      response.recordError("开票登记失败");
      return response;
    }
    int i = this.baseMapper.selectReceived(unitInvoice.getId());
    if (i > 0) {
      response.recordError("发票已被领取,不能被登记");
      return response;
    }
//    经手人
    unitInvoice.setDrawer(userName);
    unitInvoice.setEnabled("0");
    unitInvoice.setReceived("0");
    boolean b = this.updateById(unitInvoice);
    if (b) {
      response.setCode(200);
      return response;
    }
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse deleteModel(List<String> ids) {
    ApiResponse response = new ApiResponse();
    UseWaterUnitInvoice unitInvoice;
    List<UseWaterUnitInvoice> list = new ArrayList<>();
    boolean b = false;
    for (String id : ids) {
      int i = this.baseMapper.selectReceived(id);
      if (i > 0) {
        response.recordError("有发票已领取的发票,不能删除");
        return response;
      }
      unitInvoice = new UseWaterUnitInvoice();
      unitInvoice.setId(id);
      unitInvoice.setDeleted("1");
      list.add(unitInvoice);
    }
    b = this.updateBatchById(list);
    if (b) {
      response.setMessage("删除发票成功");
      response.setCode(200);
      return response;
    }
    response.setMessage("删除发票失败");
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse abolish(List<String> ids) {
    ApiResponse response = new ApiResponse();
    UseWaterUnitInvoice unitInvoice;
    List<UseWaterUnitInvoice> list = new ArrayList<>();
    boolean b = false;
    for (String id : ids) {
      unitInvoice = new UseWaterUnitInvoice();
      unitInvoice.setId(id);
      unitInvoice.setEnabled("1");
      list.add(unitInvoice);
    }
    b = this.updateBatchById(list);
    if (b) {
      response.setCode(200);
      return response;
    }
    response.setMessage("发票作废失败");
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse cancelAbolish(List<String> ids) {
    ApiResponse response = new ApiResponse();
    UseWaterUnitInvoice unitInvoice;
    List<UseWaterUnitInvoice> list = new ArrayList<>();
    boolean b = false;
    for (String id : ids) {
      unitInvoice = new UseWaterUnitInvoice();
      unitInvoice.setId(id);
      unitInvoice.setEnabled("0");
      list.add(unitInvoice);
    }
    b = this.updateBatchById(list);
    if (b) {
      response.setCode(200);
      return response;
    }
    response.setMessage("发票取消作废失败");
    response.setCode(500);
    return response;
  }


  @Override
  public ApiResponse exchange(String frontId, String rearId, String nodeCode) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(frontId) && StringUtils.isBlank(rearId)) {
      response.recordError("重置失败");
      return response;
    }
    UseWaterUnitInvoice unitInvoice1 = this.baseMapper.selectUseWaterUnitInvoice(frontId, nodeCode);
    UseWaterUnitInvoice unitInvoice2 = this.baseMapper.selectUseWaterUnitInvoice(rearId, nodeCode);
    if (null == unitInvoice1 || null == unitInvoice2) {
      response.recordError("重置失败");
      return response;
    }
    String invoiceNumber1 = unitInvoice1.getInvoiceNumber();
    String invoiceNumber2 = unitInvoice2.getInvoiceNumber();
    unitInvoice1.setInvoiceNumber(invoiceNumber2);
    unitInvoice2.setInvoiceNumber(invoiceNumber1);
    List<UseWaterUnitInvoice> list = new ArrayList<>();
    list.add(unitInvoice1);
    list.add(unitInvoice2);
    boolean b = false;
    if (!list.isEmpty()) {
      b = this.updateBatchById(list);
    }
    if (b) {
      response.setCode(200);
      return response;
    }
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse shift(String begin, String end, String personId, String loginId) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(begin) || StringUtils.isBlank(end) || StringUtils.isBlank(personId)) {
      response.recordError("系统异常");
    }

    int i = this.baseMapper
        .updateUid(Integer.parseInt(begin), Integer.parseInt(end), personId, loginId);
    if (i > 0) {
      response.setCode(200);
      response.setMessage("已移交:" + i + "张发票");
      return response;
    } else {
      response.recordError("移交发票失败");
      return response;
    }
  }

  @Override
  public ApiResponse sign(List<String> ids) {
    ApiResponse response = new ApiResponse();
    if (ids.isEmpty()) {
      response.recordError("标记失败");
      return response;
    }
    ArrayList<UseWaterUnitInvoice> list = new ArrayList<>();
    UseWaterUnitInvoice invoice;
    boolean b = false;
    for (String id : ids) {
      invoice = new UseWaterUnitInvoice();
      invoice.setId(id);
      invoice.setReceived("1");
      invoice.setReceiveTime(new Date());
      list.add(invoice);
    }
    b = this.updateBatchById(list);
    if (b) {
      response.setCode(200);
      return response;
    } else {
      response.recordError("标记失败");
      return response;
    }
  }


  @Override
  public ApiResponse queryPage(JSONObject jsonObject, String nodeCode, String loginId) {
    ApiResponse response = new ApiResponse();
    Map<String, Object> map = new LinkedHashMap<>(10);
//      页数
    Integer currPage = jsonObject.getInteger("current");
//    条数
    Integer pageSize = jsonObject.getInteger("size");

//    发票号
    String invoiceNumber = null;
    if (null != jsonObject.getString("invoiceNumber")) {
      invoiceNumber = jsonObject.getString("invoiceNumber").trim();
    }
    //    开始票段
    Integer begin = null;
    if (null != jsonObject.getInteger("begin")) {
      begin = jsonObject.getInteger("begin");
    }
    //    结束票段
    Integer end = null;
    if (null != jsonObject.getInteger("end")) {
      end = jsonObject.getInteger("end");
    }
    //    是否作废
    String enabled = null;
    if (null != jsonObject.getString("enabled")) {
      enabled = jsonObject.getString("enabled").trim();
    }
    //    是否领取
    String received = null;
    if (null != jsonObject.getString("received")) {
      received = jsonObject.getString("received").trim();
    }
    if (StringUtils.isBlank(nodeCode)) {
      response.recordError("系统异常");
      return response;
    }
//    总条数
    Integer total = this.baseMapper
        .selectCount(invoiceNumber, begin, end, enabled,
            received, nodeCode, loginId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<UseWaterUnitInvoice> templates = this.baseMapper
        .queryList(currPage, pageSize, invoiceNumber, begin,
            end, enabled, received, nodeCode, loginId);
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
}