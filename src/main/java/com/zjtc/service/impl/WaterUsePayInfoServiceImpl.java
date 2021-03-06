package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.constant.SmsConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.DictUtils;
import com.zjtc.base.util.FileUtil;
import com.zjtc.base.util.WebSocketUtil;
import com.zjtc.mapper.waterBiz.UseWaterUnitMapper;
import com.zjtc.mapper.waterBiz.WaterUsePayInfoMapper;
import com.zjtc.mapper.waterSys.FlowProcessMapper;
import com.zjtc.model.Contacts;
import com.zjtc.model.PayInfoPrint;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.UseWaterUnitInvoice;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.model.vo.PayPrintVo;
import com.zjtc.model.vo.SendListVO;
import com.zjtc.model.vo.UseWaterUnitRefVo;
import com.zjtc.model.vo.WaterUsePayInfoVo;
import com.zjtc.service.CommonService;
import com.zjtc.service.ContactsService;
import com.zjtc.service.FileService;
import com.zjtc.service.FlowExampleService;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.PayInfoPrintService;
import com.zjtc.service.RefundOrRefundService;
import com.zjtc.service.SmsSendService;
import com.zjtc.service.SmsService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterUnitInvoiceService;
import com.zjtc.service.UseWaterUnitRefService;
import com.zjtc.service.WaterUsePayInfoService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * WaterUsePayInfo???????????????????????????
 *
 * @author
 */
@Service
public class WaterUsePayInfoServiceImpl extends
    ServiceImpl<WaterUsePayInfoMapper, WaterUsePayInfo> implements
    WaterUsePayInfoService {

  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;
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
  @Autowired
  private CommonService commonService;
  @Autowired
  private UseWaterUnitMapper useWaterUnitMapper;
  @Autowired
  private DictUtils dictUtils;
  @Autowired
  private UseWaterUnitRefService useWaterUnitRefService;
  @Autowired
  private ContactsService contactsService;
  @Autowired
  private FileService fileService;
  @Autowired
  private SmsService smsService;
  @Autowired
  private SmsSendService smsSendService;
  @Autowired
  private WebSocketUtil webSocketUtil;
  @Autowired
  private SystemLogService systemLogService;

  @Autowired
  private FlowProcessMapper flowProcessMapper;
  @Autowired
  private PayInfoPrintService payInfoPrintService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse updateModel(JSONObject jsonObject, User user) {
    ApiResponse apiResponse = new ApiResponse();
    List<WaterUsePayInfo> entityList = jsonObject.getJSONArray("payInfoList")
        .toJavaList(WaterUsePayInfo.class);
    if (entityList.isEmpty()) {
      apiResponse.recordError(500);
      return apiResponse;
    }
    for (WaterUsePayInfo entity : entityList) {

      //????????????????????????????????????
      if ("1".equals(entity.getCashCheck()) || "1".equals(entity.getTransferCheck())) {
        entity.setPayStatus("5");
      }
      //???????????????????????????
      if ("1".equals(entity.getPayStatus())) {
        entity.setPayStatus("1");
      }
      if (StringUtils.isNotBlank(entity.getInvoiceId())) {
        /**???????????????*/
        UseWaterUnitInvoice useWaterUnitInvoice = new UseWaterUnitInvoice();
        useWaterUnitInvoice.setId(entity.getInvoiceId());
        useWaterUnitInvoice.setPayInfoId(entity.getId());
        useWaterUnitInvoice.setInvoiceMoney(entity.getActualAmount());//???????????????????????????????????????
        useWaterUnitInvoice.setInvoiceUnitCode(entity.getUnitCode());
        useWaterUnitInvoice.setInvoiceUnitName(entity.getUnitName());
        apiResponse = useWaterUnitInvoiceService
            .updateInvoicesUnitMessage(useWaterUnitInvoice, user.getUsername(), user.getNodeCode());
        if (501 == apiResponse.getCode()) {
          return apiResponse;
        }
      }
      boolean result = this.updateById(entity);
      if (!result) {
        apiResponse.recordError(500);
      }
    }
    systemLogService.logInsert(user, "????????????", "??????", null);
    return apiResponse;
  }


  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject, User user) {
    Map<String, Object> page = new LinkedHashMap<>();
    List<WaterUsePayInfoVo> result = baseMapper.queryPage(jsonObject);
    result = this.printAdvice(result, user);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //?????????????????????
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
    Calendar now = Calendar.getInstance();
    //????????????
    int newYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH) + 1;
    //??????????????????
    int countQuarter = nowMonth % 3 == 0 ? nowMonth / 3 : nowMonth / 3 + 1;
    /**????????????????????????????????????????????????*/
    /**1.?????????1??????????????????3???????????????*/
    /**2.?????????2??????????????????4???????????????*/
    /**3.?????????3??????????????????1???????????????*/
    /**4.?????????4??????????????????2???????????????*/
    switch (countQuarter) {
      case 1:
      case 2:
        newYear = newYear - 1;
        break;
    }
    String newQuarter = dictUtils
        .getDictItemName("increaseMoneyQuarterCode", String.valueOf(countQuarter),
            jsonObject.getString("nodeCode"));
    jsonObject.put("countYear", newYear);
    jsonObject.put("countQuarter", newQuarter);
    /**???????????????????????????????????????*/
    //????????????:1.????????????????????????2.??????????????????3?????????????????????
    baseMapper.deleteByParam(jsonObject);
    /**???????????????*/
    List<WaterUsePayInfo> waterUsePayInfos = baseMapper.initPayInfo(jsonObject);
    if (!waterUsePayInfos.isEmpty()) {
      result = this.saveBatch(waterUsePayInfos);
    }
    return result;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse toStartRefund(JSONObject jsonObject, User user) {
    ApiResponse apiResponse = new ApiResponse();
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    /**????????????????????????????????????????????????*/
    if (refundOrRefundService.auditCount(entity.getPayId(), user.getNodeCode())) {
      apiResponse.recordError("?????????????????????????????????????????????");
      apiResponse.setCode(501);
      return apiResponse;
    }
    String nextPersonId = jsonObject.getString("nextPersonId");
    String nextPersonName = jsonObject.getString("nextPersonName");
    String detailConfig = jsonObject.getString("detailConfig");
    List<com.zjtc.model.File> files = jsonObject.getJSONArray("sysFiles")
        .toJavaList(com.zjtc.model.File.class);
    /**???????????????????????????????????????*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .secondAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**??????????????????????????????*/
    entity.setNodeCode(user.getNodeCode());
    entity.setDrawer(user.getUsername());
    entity.setIsRevoke("0");
    entity.setStatus("0");
    entity.setType("1");
    entity.setCreateTime(new Date());
    refundOrRefundService.save(entity);
    /**????????????*/
    if (!entity.getSysFiles().isEmpty()) {
      fileService.updateBusinessId(entity.getId(), files);
    }
    /**????????????????????????????????????????????????????????????*/
    String newFirStCodeId = flowNodeInfoService
        .selectAndInsert(user.getNodeCode(), entity.getId(), AuditConstants.PAY_FLOW_CODE,
            firStAudit.get(0).get("flowNodeId").toString());
    entity.setNextNodeId(newFirStCodeId);
    /**?????????????????????*/
    //???????????????????????????
    entity.setToAuditPerson(nextPersonId);
    StringBuffer str = new StringBuffer(user.getId());
    str.append(",");
    entity.setAuditPersons(str.toString());
    refundOrRefundService.updateById(entity);
    /**????????????????????????????????? ??????????????????*/
    flowProcessService
        .create(user, entity.getId(), entity.getTreatmentAdvice(), nextPersonName, nextPersonId);
    /**????????????*/
    String todoContent =
        "????????????" + entity.getUnitCode() + "(" + entity.getUnitName() + ") ????????????" + entity.getMoney()
            + "???";
    entity.setAuditFlow(flowProcessMapper.queryAuditList(entity.getId(), user.getNodeCode()));
    todoService
        .add(entity.getId(), user, nextPersonId, nextPersonName, todoContent,
            JSONObject.toJSONString(entity, SerializerFeature.WriteMapNullValue),
            detailConfig,
            AuditConstants.PAY_TODO_TYPE);
    /**websocket??????*/
    webSocketUtil.pushWaterTodo(user.getNodeCode(), nextPersonId);
    /**???????????????????????????*/
    flowExampleService.add(user, entity.getId(), AuditConstants.PAY_TODO_TYPE);
    systemLogService.logInsert(user, "????????????", "???????????????", null);
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse toStartReduction(JSONObject jsonObject, User user) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    ApiResponse apiResponse = new ApiResponse();
    if (refundOrRefundService.auditCount(entity.getPayId(), user.getNodeCode())) {
      apiResponse.recordError("?????????????????????????????????????????????");
      apiResponse.setCode(501);
      return apiResponse;
    }
    String nextPersonId = jsonObject.getString("nextPersonId");
    String nextPersonName = jsonObject.getString("nextPersonName");
    String businessJson = jsonObject.getString("businessJson");
    String detailConfig = jsonObject.getString("detailConfig");
    List<com.zjtc.model.File> files = jsonObject.getJSONArray("sysFiles")
        .toJavaList(com.zjtc.model.File.class);
    /**???????????????????????????????????????*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .secondAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**??????????????????????????????*/
    entity.setNodeCode(user.getNodeCode());
    entity.setDrawer(user.getUsername());
    entity.setIsRevoke("0");
    entity.setStatus("0");
    entity.setType("2");
    entity.setCreateTime(new Date());
    refundOrRefundService.save(entity);
    /**????????????*/
    if (!entity.getSysFiles().isEmpty()) {
      fileService.updateBusinessId(entity.getId(), files);
    }
    /**????????????????????????????????????????????????????????????*/
    String newFirStCodeId = flowNodeInfoService
        .selectAndInsert(user.getNodeCode(), entity.getId(), AuditConstants.PAY_FLOW_CODE,
            firStAudit.get(0).get("flowNodeId").toString());
    entity.setNextNodeId(newFirStCodeId);
    /**?????????????????????*/
    //???????????????????????????
    entity.setToAuditPerson(nextPersonId);
    StringBuffer str = new StringBuffer(user.getId());
    str.append(",");
    entity.setAuditPersons(str.toString());
    refundOrRefundService.updateById(entity);
    refundOrRefundService.updateById(entity);
    /**????????????????????????????????? ??????????????????*/
    flowProcessService
        .create(user, entity.getId(), entity.getTreatmentAdvice(), nextPersonName, nextPersonId);
    /**????????????*/
    String todoContent =
        "????????????" + entity.getUnitCode() + "(" + entity.getUnitName() + ") ????????????" + entity.getMoney()
            + "???";
    entity.setAuditFlow(flowProcessMapper.queryAuditList(entity.getId(), user.getNodeCode()));
    todoService
        .add(entity.getId(), user, nextPersonId, nextPersonName, todoContent,
            JSONObject.toJSONString(entity, SerializerFeature.WriteMapNullValue),
            detailConfig,
            AuditConstants.PAY_TODO_TYPE);
    /**websocket??????*/
    webSocketUtil.pushWaterTodo(user.getNodeCode(), nextPersonId);
    /**???????????????????????????*/
    flowExampleService.add(user, entity.getId(), AuditConstants.PAY_TODO_TYPE);
    systemLogService.logInsert(user, "????????????", "???????????????", null);
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
  public boolean updateActualAmount(String id, double actualAmount) {
    return baseMapper.updateActualAmount(id, actualAmount);
  }

  @Override
  public List<Map<String, Object>> firstRole(User user) {
    List<Map<String, Object>> result = flowNodeInfoService
        .secondAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    return result;
  }

  @Override
  public List<Map<String, Object>> findPayBefor(String unitId) {
    return baseMapper.findPayBefor(unitId);
  }

  @Override
  public List<Map<String, Object>> ThreePayMess(String unitId) {
    //????????????
    Calendar date = Calendar.getInstance();
    int year = Integer.valueOf(date.get(Calendar.YEAR));
    return baseMapper.ThreePayMess(year, unitId);
  }

  @Override
  public Map<String, Object> selectPayNotice(JSONObject jsonObject) {
    Map<String, Object> page = new HashMap<>();
    Integer year = jsonObject.getInteger("countYear");
    if (null == year || "0".equals(year)) {
      //????????????
      Calendar now = Calendar.getInstance();
      year = now.get(Calendar.YEAR);
      jsonObject.put("year", year);
    }
    //????????????
    String messageTypecode = dictUtils
        .getDictItemCode("messageType", "????????????", jsonObject.getString("nodeCode"));
    jsonObject.put("messageTypecode", messageTypecode);
    //?????????????????????????????????
    List<SendListVO> noticeData = baseMapper.selectPayNotice(jsonObject);
    if (noticeData.isEmpty()) {
      return page;
    }
    //??????????????????
    //sql?????????
    jsonObject.put("pageSize", jsonObject.getInteger("size"));
    List<SendListVO> data = smsSendService.queryAll(noticeData, jsonObject);
    long total = smsSendService.count(noticeData, jsonObject);
    page.put("records", data);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  public boolean send(JSONObject jsonObject, User user) throws Exception {
    List<SendListVO> list = jsonObject.getJSONArray("data").toJavaList(SendListVO.class);
    smsService.sendNotification(user, list, SmsConstants.SEND_NOTIFICATION_PAY, null);
    systemLogService.logInsert(user, "????????????", "????????????????????????", null);
    return true;
  }

  @Override
  public ApiResponse exportQueryData(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse result = new ApiResponse();
    Map<String, Object> data = new HashMap<>();
    List<Map<String, Object>> list = baseMapper.exportQueryData(jsonObject);
    if (!list.isEmpty()) {
      for (Map map : list) {
        String payType = map.get("payType").toString();
        if (StringUtils.isNotBlank(payType)) {
          map.put("payType", dictUtils.getDictItemName("payTypeCode", payType, user.getNodeCode()));
        }
      }
      data.put("excelData", list);
      data.put("nowDate", new Date());
      SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy???MM???dd???");
      data.put("dateFormat", dateFmt);
      String fileName = "????????????????????????.xls";
      String templateName = "template/waterUsePayInfoData.xls";
      commonService.export(fileName, templateName, request, response, data);
      systemLogService.logInsert(user, "????????????", "??????????????????", null);
    } else {
      result.setMessage("???????????????");
      result.setCode(501);
    }
    return result;
  }

  @Override
  public ApiResponse exportUser(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    ApiResponse result = new ApiResponse();
    String nodeCode = jsonObject.getString("nodeCode");
    String year = jsonObject.getString("countYear");
    String quarter = jsonObject.getString("countQuarter");
    //??????????????????
    List<Map<String, Object>> list = baseMapper.selectUser(jsonObject);
//    //????????????????????????????????????
//    List<Map<String, Object>> map = useWaterUnitMapper.selectByIds(unitCodeList);
    if (!list.isEmpty()) {
      for (Map item : list) {
        String dictItemCode =
            null == item.get("areaCountry") ? null : item.get("areaCountry").toString();
        //????????????
        if (StringUtils.isNotBlank(dictItemCode)) {
          item.put("areaCountryName", dictUtils
              .getDictItemNameCountry("area_country_code",
                  dictItemCode, nodeCode));
        }
        //??????????????????
        List<String> idList = useWaterUnitRefService
            .findIdList(item.get("id").toString(), nodeCode);
        String useWaterUnitIdRef = "";
        if (!idList.isEmpty()) {
          //????????????????????????
          List<UseWaterUnitRefVo> useWaterUnitRefList = useWaterUnitMapper
              .queryUnitRef(idList, nodeCode, jsonObject.getString("userId"),
                  item.get("id").toString());
          if (!useWaterUnitRefList.isEmpty()) {
            for (UseWaterUnitRefVo useWaterUnitRefVo : useWaterUnitRefList) {
              useWaterUnitIdRef += useWaterUnitRefVo.getUnitCode() + ",";
            }
            useWaterUnitIdRef = useWaterUnitIdRef.substring(0, useWaterUnitIdRef.length() - 1);
          }
        }
        item.put("useWaterUnitIdRef", useWaterUnitIdRef);
        //??????????????????
        List<Contacts> contactsList = contactsService.queryByUnitId(item.get("id").toString());
        if (!contactsList.isEmpty()) {
          for (int i = 0; i < contactsList.size(); i++) {
            item.put("contacts" + (i + 1), contactsList.get(i).getContacts());
            item.put("mobileNumber" + (i + 1), contactsList.get(i).getMobileNumber());
            item.put("phoneNumber" + (i + 1), contactsList.get(i).getPhoneNumber());
            if (i == 2) {
              break;
            }
          }
        }
      }
      Map<String, Object> data = new HashMap<>();
      data.put("excelData", list);
      data.put("nowDate", new Date());
      SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy???MM???dd???");
      data.put("dateFormat", dateFmt);
      String fileName = "??????????????????.xls";
      if (null == quarter || quarter.equals("")) {
        fileName = year + "???" + fileName;
      } else {
        fileName = year + "???" + quarter + "??????" + fileName;
      }
      String templateName = "template/waterUsePayInfoUserData.xls";
      commonService.export(fileName, templateName, request, response, data);
      systemLogService.logInsert(user, "????????????", "??????????????????", null);
    } else {
      result.setMessage("???????????????");
      result.setCode(501);
    }
    return result;
  }

  @Override
  public ApiResponse exportPayInfo(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse result = new ApiResponse();
    String year = jsonObject.getString("countYear");
    String quarter = jsonObject.getString("countQuarter");
    List<Map<String, Object>> list = baseMapper.exportPayInfo(jsonObject);
    if (!list.isEmpty()) {
      Map<String, Object> data = new HashMap<>();
      data.put("excelData", list);
      data.put("nowDate", new Date());
      SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy???MM???dd???");
      data.put("dateFormat", dateFmt);
      String fileName = "?????????????????????????????????????????????.xls";
      if (null == quarter || quarter.equals("")) {
        fileName = year + "???" + fileName;
      } else {
        fileName = year + "???" + quarter + "??????" + fileName;
      }
      String templateName = "template/payInfo.xls";
      commonService.export(fileName, templateName, request, response, data);
      systemLogService.logInsert(user, "????????????", "??????????????????????????????????????????", null);
    } else {
      result.setMessage("???????????????");
      result.setCode(501);
    }
    return result;
  }

  @Override
  public ApiResponse exportBankInfo(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    ApiResponse apiResponse = new ApiResponse();
    String year = jsonObject.getString("countYear");
    String quarter = jsonObject.getString("countQuarter");
    //List<Map> list = jsonObject.getJSONArray("data").toJavaList(Map.class);
    List<Map<String, Object>> list = baseMapper.exportBankInfo(jsonObject);
    String str = "";
    if (!list.isEmpty()) {
      for (Map map : list) {
        String unitCode = null == map.get("unitCode") ? "" : map.get("unitCode").toString();
        String agreementNumber =
            null == map.get("agreementNumber") ? "" : map.get("agreementNumber").toString();
        if (StringUtils.isBlank(agreementNumber)) {
          apiResponse.recordError("????????????:" + unitCode + " ??????????????? ????????????");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankOfDeposit =
            null == map.get("bankOfDeposit") ? "" : map.get("bankOfDeposit").toString();
        if (StringUtils.isBlank(bankOfDeposit)) {
          apiResponse.recordError("????????????:" + unitCode + " ??????????????? ????????????");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankAccount =
            null == map.get("bankAccount") ? "" : map.get("bankAccount").toString();
        if (StringUtils.isBlank(bankAccount)) {
          apiResponse.recordError("????????????:" + unitCode + " ?????????????????? ????????????");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String entrustUnitName =
            null == map.get("entrustUnitName") ? "" : map.get("entrustUnitName").toString();
        String actualAmount =
            null == map.get("actualAmount") ? "" : map.get("actualAmount").toString();
        String remark = null == map.get("remark") ? "" : map.get("remark").toString();
        str += StringUtils.trim(agreementNumber) + "|" + StringUtils
            .trim(bankOfDeposit) + "|"
            + StringUtils.trim(bankAccount) + "|" + StringUtils
            .trim(entrustUnitName) + "|"
            + StringUtils.trim(actualAmount) + "|" + StringUtils
            .trim(remark + "|\r\n");
      }
    } else {
      apiResponse.recordError("??????????????? ????????????");
      apiResponse.setCode(501);
      return apiResponse;
    }
    //?????????
    String fileName = "??????????????????.txt";
    if (null == quarter || quarter.equals("")) {
      fileName = year + "???" + fileName;
    } else {
      fileName = year + "???" + quarter + "??????" + fileName;
    }
    File file = new File(
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName);
    FileOutputStream fos = new FileOutputStream(file.getPath());
    fos.write(str.getBytes());//?????????????????????
    boolean result = FileUtil.writeBytes(file, fileName, request, response);
    if (result) {
      //????????????
      file.delete();
    }
    systemLogService.logInsert(user, "????????????", "??????????????????", null);
    return apiResponse;
  }

  @Override
  public ApiResponse exportOtherBankInfo(User user, JSONObject jsonObject,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    ApiResponse apiResponse = new ApiResponse();
    String year = jsonObject.getString("countYear");
    String quarter = jsonObject.getString("countQuarter");
    //List<Map> list = jsonObject.getJSONArray("data").toJavaList(Map.class);
    List<Map<String, Object>> list = baseMapper.exportOtherBankInfo(jsonObject);
    list.addAll(list);
    String str = "";
    if (!list.isEmpty()) {
      for (Map map : list) {
        String unitCode = null == map.get("unitCode") ? "" : map.get("unitCode").toString();
        String agreementNumber =
            null == map.get("agreementNumber") ? "" : map.get("agreementNumber").toString();
        if (StringUtils.isBlank(agreementNumber)) {
          apiResponse.recordError(unitCode + " ??????????????? ????????????");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankOfDeposit =
            null == map.get("bankOfDeposit") ? "" : map.get("bankOfDeposit").toString();
        if (StringUtils.isBlank(bankOfDeposit)) {
          apiResponse.recordError("????????????:" + unitCode + " ??????????????? ????????????");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankAccount =
            null == map.get("bankAccount") ? "" : map.get("bankAccount").toString();
        if (StringUtils.isBlank(bankAccount)) {
          apiResponse.recordError("????????????:" + unitCode + " ?????????????????? ????????????");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String signed =
            null == map.get("signed") ? "" : map.get("signed").toString();
        if ("0".equals(signed)) {
          apiResponse.recordError("????????????:" + unitCode + " ??????????????? ????????????");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String entrustUnitName =
            null == map.get("entrustUnitName") ? "" : map.get("entrustUnitName").toString();
        String actualAmount =
            null == map.get("actualAmount") ? "" : map.get("actualAmount").toString();
        String remark = null == map.get("remark") ? "" : map.get("remark").toString();
        str += StringUtils.trim(agreementNumber) + "|" + StringUtils
            .trim(bankOfDeposit) + "|"
            + StringUtils.trim(bankAccount) + "|" + StringUtils
            .trim(entrustUnitName) + "|"
            + StringUtils.trim(actualAmount) + "|" + StringUtils
            .trim(remark) + "|\r\n";
      }
    } else {
      apiResponse.recordError("??????????????? ????????????");
      apiResponse.setCode(501);
      return apiResponse;
    }
    //?????????
    String fileName = "??????????????????.txt";
    if (null == quarter || quarter.equals("")) {
      fileName = year + "???" + fileName;
    } else {
      fileName = year + "???" + quarter + "??????" + fileName;
    }
    File file = new File(
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName);
    FileOutputStream fos = new FileOutputStream(file.getPath());
    fos.write(str.getBytes());//?????????????????????
    boolean result = FileUtil.writeBytes(file, fileName, request, response);
    if (result) {
      //????????????
      file.delete();
    }
    systemLogService.logInsert(user, "????????????", "??????????????????", null);
    return apiResponse;
  }

  @Override
  public boolean editInvoiceInfo(String id, String invoiceId, String invoiceNumber) {
    if (StringUtils.isBlank(id) || StringUtils.isBlank(invoiceId) || StringUtils
        .isNotBlank(invoiceNumber)) {
      return false;
    }
    return this.baseMapper.updateInvoiceNum(id, invoiceNumber);
  }

  @Override
  public Map<String, Object> printExPlan1(JSONObject jsonObject, User user
  ) {
    Map<String, Object> result = new HashMap<>();
    //?????????????????????????????????
    List<String> typeList = baseMapper.queryCodeTypeByPersonId(user.getId(), user.getNodeCode());
    if (typeList.isEmpty()) {
      return result;
    }
    for (String type : typeList) {
      jsonObject.put("unitCodeType", type);
      List<PayPrintVo> list = baseMapper.printExPlan1(jsonObject);
      if (!list.isEmpty()) {
        result.put(type, list);
      }
    }
    return result;
  }

  @Override
  public Map<String, Object> printExPlan2(JSONObject jsonObject, User user) {
    Map<String, Object> result = new HashMap<>();
    //?????????????????????????????????
    List<String> typeList = baseMapper.queryCodeTypeByPersonId(user.getId(), user.getNodeCode());
    if (typeList.isEmpty()) {
      return result;
    }
    for (String type : typeList) {
      jsonObject.put("unitCodeType", type);
      List<PayPrintVo> list = baseMapper.printExPlan2(jsonObject);

      if (!list.isEmpty()) {
        result.put(type, list);
      }
    }
    return result;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<WaterUsePayInfoVo> printAdvice(List<WaterUsePayInfoVo> list, User user) {

    //????????????????????????
    List<PayInfoPrint> payInfoPrintList = new ArrayList<>();

    if (!list.isEmpty()) {
      //?????????????????? ????????????????????????
      for (WaterUsePayInfoVo item : list) {
        //????????????????????????
        PayInfoPrint entity = new PayInfoPrint();
        String newPrintNum;
        PayInfoPrint payInfoPrint
            = payInfoPrintService.selectPrintMess(item.getId());
        //??????????????????,???????????????????????????
        if (null == payInfoPrint) {
          newPrintNum = payInfoPrintService
              .createPrintNum(item.getUnitCode(), item.getCountYear().toString(),
                  item.getCountQuarter());
          item.setPrintNum(newPrintNum);

        }
        //????????????????????????????????????????????????????????????
        else if ("0".equals(payInfoPrint.getStatus())) {
          newPrintNum = payInfoPrint.getPrintNum();
          entity.setId(payInfoPrint.getId());
        }
        //??????????????????????????????????????????????????????????????????1
        else {
          newPrintNum = payInfoPrint.getPrintNum();
          newPrintNum = craeatRank(newPrintNum);
        }
        //????????????????????????????????????
        item.setPrintNum(newPrintNum);
        entity.setPrintNum(newPrintNum);
        entity.setNodeCode(user.getNodeCode());
        entity.setPayId(item.getId());
        entity.setStatus("0");
        payInfoPrintList.add(entity);
      }
      /**???????????????????????????*/
      payInfoPrintService.saveOrUpdateBatch(payInfoPrintList);
    }
    return list;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean prinSuccess(JSONObject jsonObject, User user) {
    boolean result1 = true;
    boolean result2 = true;
    List<String> param = jsonObject.getJSONArray("ids")
        .toJavaList(String.class);
    if (!param.isEmpty()) {
      //???????????????????????????????????????
      result1 = baseMapper.updatePrinted(param);
      //?????????????????????????????????
      result2 = payInfoPrintService.updatePrinted(param, user);
    }
    return result1 && result2;
  }

  //??????3????????????
  private String craeatRank(String printNum) {
    String result;
    String maxCount = printNum.substring(printNum.length() - 3);
    //????????????????????????????????????1
    int count = 0;
    if (StringUtils.isNotBlank(maxCount)) {

      count = Integer.parseInt(maxCount) + 1;
    } else {
      maxCount = "001";
    }

    if (count > 0) {
      maxCount = "00" + count;
    }
    if (count > 9) {
      maxCount = "0" + count;
    }
    if (count > 99) {
      maxCount = String.valueOf(count);
    }
    result = printNum.substring(0, printNum.length() - 3) + maxCount;
    return result;
  }
}