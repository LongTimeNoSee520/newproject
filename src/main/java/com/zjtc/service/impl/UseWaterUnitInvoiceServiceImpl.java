package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.CommonUtil;
import com.zjtc.base.util.JxlsUtils;
import com.zjtc.base.util.TimeUtil;
import com.zjtc.mapper.waterBiz.UseWaterUnitInvoiceMapper;
import com.zjtc.model.UseWaterUnitInvoice;
import com.zjtc.model.User;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.UseWaterUnitInvoiceService;
import com.zjtc.service.WaterUsePayInfoService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * UseWaterUnitInvoice的服务接口的实现类
 *
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 计划用水户发票表
 */
@Service
@Slf4j
public class UseWaterUnitInvoiceServiceImpl extends
    ServiceImpl<UseWaterUnitInvoiceMapper, UseWaterUnitInvoice> implements
    UseWaterUnitInvoiceService {

  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;

  @Value("${file.fileUploadPath}")
  private String fileUploadPath;

  @Autowired
  private SystemLogService systemLogService;

  @Autowired
  private WaterUsePayInfoService waterUsePayInfoService;

  @Override
  public ApiResponse saveModel(List<String> list, User user) {
    ApiResponse response = new ApiResponse();
    List<UseWaterUnitInvoice> unitInvoiceList = new ArrayList<>();
    UseWaterUnitInvoice unitInvoice;
    for (String invoiceNumber : list) {
//      查看发票号是否为空
      int i = this.baseMapper.selectInvoiceNumber(invoiceNumber, user.getNodeCode());
      if (i > 0) {
//        i不0的话说明已存在,直接退出
        response.recordError("发票号为:" + invoiceNumber + "已存在");
        return response;
      } else {
        unitInvoice = new UseWaterUnitInvoice();
        unitInvoice.setInvoiceNumber(invoiceNumber);
        unitInvoice.setCreateTime(new Date());
        unitInvoice.setPerson(user.getId());
        unitInvoice.setNodeCode(user.getNodeCode());
        unitInvoice.setEnabled("0");
        unitInvoice.setReceived("0");
        unitInvoice.setDeleted("0");
        unitInvoiceList.add(unitInvoice);
      }
    }
    boolean b = this.saveBatch(unitInvoiceList);
    if (b) {
      systemLogService.logInsert(user, "发票管理", "新增发票管理", "");
      response.setCode(200);
      return response;
    }
    return response;
  }

  @Override
  public ApiResponse updateModel(UseWaterUnitInvoice unitInvoice, User user) {
    ApiResponse response = new ApiResponse();
    if (null == unitInvoice) {
      response.recordError("开票登记失败");
      return response;
    }
    String invoiceNumber = this.baseMapper.selectReceived(unitInvoice.getId());
    if (!StringUtils.isBlank(invoiceNumber)) {
      response.recordError("发票" + invoiceNumber + "已被领取,不能被登记");
      return response;
    }
//    经手人
    unitInvoice.setDrawer(user.getUsername());
    unitInvoice.setReceived("0");
    boolean b = this.updateById(unitInvoice);
    if (b) {
      response.setCode(200);
      systemLogService.logInsert(user, "发票管理", "开票登记", "");
      return response;
    }
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse deleteModel(List<String> ids, User user) {
    ApiResponse response = new ApiResponse();
    UseWaterUnitInvoice unitInvoice;
    List<UseWaterUnitInvoice> list = new ArrayList<>();
    boolean b;
    List<String> strings = new ArrayList<>();
    for (String id : ids) {
      String invoiceNumber = this.baseMapper.selectReceived(id);
      strings.add(invoiceNumber);
    }
//    去除null值
    List<String> newList = strings.stream().filter(Objects::nonNull)
        .collect(Collectors.toList());
    if (!newList.isEmpty()) {
      String lists = StringUtils.strip(newList.toString(), "[]")
          .replace(" ", "");
      response.recordError("发票:" + lists + "已被领取,不能被删除");
      return response;
    } else {
      for (String id : ids) {
        unitInvoice = new UseWaterUnitInvoice();
        unitInvoice.setId(id);
        unitInvoice.setDeleted("1");
        list.add(unitInvoice);
      }
    }
    b = this.updateBatchById(list);
    if (b) {
      response.setMessage("删除发票成功");
      response.setCode(200);
      systemLogService.logInsert(user, "发票管理", "删除发票", "");
      return response;
    }
    response.setMessage("删除发票失败");
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse abolish(List<String> ids, User user) {
    ApiResponse response = new ApiResponse();
    UseWaterUnitInvoice unitInvoice;
    List<UseWaterUnitInvoice> list = new ArrayList<>();
    boolean b;
    for (String id : ids) {
      unitInvoice = new UseWaterUnitInvoice();
      unitInvoice.setId(id);
      unitInvoice.setEnabled("1");
      list.add(unitInvoice);
    }
    b = this.updateBatchById(list);
    if (b) {
      response.setCode(200);
      systemLogService.logInsert(user, "发票管理", "发票作废", "");

      return response;
    }
    response.setMessage("发票作废失败");
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse cancelAbolish(List<String> ids, User user) {
    ApiResponse response = new ApiResponse();
    if (ids.isEmpty()) {
      response.recordError("系统异常");
      return response;
    }
    int b = 0;
    for (String id : ids) {
//      取消作废时判断之前是有被作废状态的数据,如果有,就根据开票时间提示取消该时间之后的数据
      UseWaterUnitInvoice unitInvoice1 = this.baseMapper.selectById(id);
      List<String> invoiceNumberList = this.baseMapper
          .selectEnabledStatus(unitInvoice1.getPayInfoId(), user.getNodeCode());
      if (!invoiceNumberList.isEmpty()) {
//        拼接发票号
        String invoiceNumbers = StringUtils.strip(invoiceNumberList.toString(), "[]")
            .replace(" ", "");
        response.recordError("请先取消作废发票号为:" + invoiceNumbers + "的数据");
        return response;
      }
      if ("0".equals(unitInvoice1.getEnabled())) {
        response.recordError("请先选择已作废的数据");
        return response;
      }
      b = this.baseMapper.updateEnabledStatus(id);
    }
    if (b > 0) {
      response.setCode(200);
      systemLogService.logInsert(user, "发票管理", "取消作废发票信息", "");
      return response;
    } else {
      response.recordError("操作失败");
      return response;
    }
  }


  @Override
  public ApiResponse exchange(String frontId, String rearId, User user) throws Exception {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(frontId) && StringUtils.isBlank(rearId)) {
      response.recordError("重置失败");
      return response;
    }
    UseWaterUnitInvoice unitInvoice1 = this.baseMapper
        .selectUseWaterUnitInvoice(frontId, user.getNodeCode());
    UseWaterUnitInvoice unitInvoice2 = this.baseMapper
        .selectUseWaterUnitInvoice(rearId, user.getNodeCode());
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

    //更新缴费信息
    try {
      waterUsePayInfoService
          .editInvoiceInfo(unitInvoice2.getPayInfoId(), rearId, unitInvoice2.getInvoiceNumber());
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("更新缴费信息异常");
    }

    if (b) {
      response.setCode(200);
      systemLogService.logInsert(user, "发票管理", "重置发票", "");
      return response;
    }
    response.setCode(500);
    return response;
  }

  @Override
  public ApiResponse shift(String begin, String end, String personId, User user) {
    ApiResponse response = new ApiResponse();
    if (StringUtils.isBlank(begin) || StringUtils.isBlank(end) || StringUtils.isBlank(personId)) {
      response.recordError("系统异常");
    }

    int i = this.baseMapper
        .updateUid(Integer.parseInt(begin), Integer.parseInt(end), personId, user.getId(),
            user.getNodeCode());
    if (i > 0) {
      response.setCode(200);
      response.setMessage("已移交:" + i + "张发票");
      systemLogService.logInsert(user, "发票管理", "移交发票", "");
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

  @Override
  public List<Map<String, Object>> selectInvoices(String loginId, String nodeCode) {
    return this.baseMapper.selectInvoices(loginId, nodeCode);
  }

  @Override
  public ApiResponse updateInvoicesUnitMessage(UseWaterUnitInvoice useWaterUnitInvoice,
      String userName, String nodeCode) {
    ApiResponse response = new ApiResponse();
    if (null == useWaterUnitInvoice || StringUtils.isBlank(userName)) {
      response.recordError("系统异常");
      return response;
    }
    UseWaterUnitInvoice unitInvoice = this.baseMapper.selectById(useWaterUnitInvoice.getId());
    if (unitInvoice.getPayInfoId() != null) {
      response.recordError("发票号已被使用");
      return response;
    } else if ("1".equals(unitInvoice.getEnabled())) {
      response.recordError("发票号已经被作废,不能使用");
      return response;
    }
    useWaterUnitInvoice.setInvoiceDate(new Date());
    int i = this.baseMapper.updateInvoicesUnitMessage(useWaterUnitInvoice, userName, nodeCode);
    if (i > 0) {
      response.setCode(200);
      return response;
    } else {
      response.recordError(" 单位信息关联发票失败");
      return response;
    }
  }

  @Override
  public void export(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response, User user) {
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
    List<UseWaterUnitInvoice> export = this.baseMapper
        .export(invoiceNumber, begin, end, enabled, received, user.getNodeCode(), user.getId());
    for (UseWaterUnitInvoice waterUnitInvoice : export) {
      String invoiceTime = TimeUtil.formatTimeStr(waterUnitInvoice.getInvoiceDate());
      waterUnitInvoice.setInvoiceTime(invoiceTime);
    }
    Map<String, Object> data = new HashMap<>(16);
    data.put("export", export);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    try {
      String fileName = "发票管理.xlsx";
      String saveFilePath =
          fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName;
      InputStream inputStream = getClass().getClassLoader()
          .getResourceAsStream("template/invoiceRecord.xlsx");
      OutputStream os = new FileOutputStream(saveFilePath);
      JxlsUtils.exportExcel(inputStream, os, data);
      os.close();
      File getPath = new File(saveFilePath);
      boolean downloadSuccess = CommonUtil.writeBytes(getPath, fileName, request, response);
      //下载完毕删除文件
      if (downloadSuccess && (getPath.exists() && getPath.isFile())) {
        getPath.delete();
      }
    } catch (Exception e) {
      log.error("发票管理导出异常:" + e.getMessage());
    }
  }
}