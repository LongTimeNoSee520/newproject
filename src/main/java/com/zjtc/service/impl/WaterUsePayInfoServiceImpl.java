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
 * WaterUsePayInfo的服务接口的实现类
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

      //勾选了财务转账、现金复核
      if ("1".equals(entity.getCashCheck()) || "1".equals(entity.getTransferCheck())) {
        entity.setPayStatus("5");
      }
      //勾选了托收缴费状态
      if ("1".equals(entity.getPayStatus())) {
        entity.setPayStatus("1");
      }
      if (StringUtils.isNotBlank(entity.getInvoiceId())) {
        /**绑定发票号*/
        UseWaterUnitInvoice useWaterUnitInvoice = new UseWaterUnitInvoice();
        useWaterUnitInvoice.setId(entity.getInvoiceId());
        useWaterUnitInvoice.setPayInfoId(entity.getId());
        useWaterUnitInvoice.setInvoiceMoney(entity.getActualAmount());//开票时，实收金额为开票金额
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
    systemLogService.logInsert(user, "缴费管理", "保存", null);
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
    Calendar now = Calendar.getInstance();
    //获取本年
    int newYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH) + 1;
    //获取当前季度
    int countQuarter = nowMonth % 3 == 0 ? nowMonth / 3 : nowMonth / 3 + 1;
    /**重算加价考核，考核过的数据不更新*/
    /**1.本年第1季度，上年第3季度已考核*/
    /**2.本年第2季度，上年第4季度已考核*/
    /**3.本年第3季度，本年第1季度已考核*/
    /**4.本年第4季度，本年第2季度已考核*/
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
    /**重算加价前先删除之前的数据*/
    //三种情况:1.托收缴费已托收，2.已选择发票，3：有退减免过程
    baseMapper.deleteByParam(jsonObject);
    /**初始化加价*/
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
    /**判断当前缴费记录是否有退减免流程*/
    if (refundOrRefundService.auditCount(entity.getPayId(), user.getNodeCode())) {
      apiResponse.recordError("当前缴费记录退减免流程尚未结束");
      apiResponse.setCode(501);
      return apiResponse;
    }
    String nextPersonId = jsonObject.getString("nextPersonId");
    String nextPersonName = jsonObject.getString("nextPersonName");
    String detailConfig = jsonObject.getString("detailConfig");
    List<com.zjtc.model.File> files = jsonObject.getJSONArray("sysFiles")
        .toJavaList(com.zjtc.model.File.class);
    /**查询流程节点记录第二个流程*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .secondAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**退减免单新增一条数据*/
    entity.setNodeCode(user.getNodeCode());
    entity.setDrawer(user.getUsername());
    entity.setIsRevoke("0");
    entity.setStatus("0");
    entity.setType("1");
    entity.setCreateTime(new Date());
    refundOrRefundService.save(entity);
    /**新增附件*/
    if (!entity.getSysFiles().isEmpty()) {
      fileService.updateBusinessId(entity.getId(), files);
    }
    /**流程节点记录表、流程节点线记录表创建数据*/
    String newFirStCodeId = flowNodeInfoService
        .selectAndInsert(user.getNodeCode(), entity.getId(), AuditConstants.PAY_FLOW_CODE,
            firStAudit.get(0).get("flowNodeId").toString());
    entity.setNextNodeId(newFirStCodeId);
    /**修改业务表数据*/
    //待谁审核，谁审核过
    entity.setToAuditPerson(nextPersonId);
    StringBuffer str = new StringBuffer(user.getId());
    str.append(",");
    entity.setAuditPersons(str.toString());
    refundOrRefundService.updateById(entity);
    /**流程进度（操作记录）表 新增三条数据*/
    flowProcessService
        .create(user, entity.getId(), entity.getTreatmentAdvice(), nextPersonName, nextPersonId);
    /**发起待办*/
    String todoContent =
        "用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ") 申请退款" + entity.getMoney()
            + "元";
    entity.setAuditFlow(flowProcessMapper.queryAuditList(entity.getId(), user.getNodeCode()));
    todoService
        .add(entity.getId(), user, nextPersonId, nextPersonName, todoContent,
            JSONObject.toJSONString(entity, SerializerFeature.WriteMapNullValue),
            detailConfig,
            AuditConstants.PAY_TODO_TYPE);
    /**websocket推送*/
    webSocketUtil.pushWaterTodo(user.getNodeCode(), nextPersonId);
    /**新增流程实例表数据*/
    flowExampleService.add(user, entity.getId(), AuditConstants.PAY_TODO_TYPE);
    systemLogService.logInsert(user, "缴费管理", "发起退款单", null);
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse toStartReduction(JSONObject jsonObject, User user) {
    RefundOrRefund entity = jsonObject.toJavaObject(RefundOrRefund.class);
    ApiResponse apiResponse = new ApiResponse();
    if (refundOrRefundService.auditCount(entity.getPayId(), user.getNodeCode())) {
      apiResponse.recordError("当前缴费记录退减免流程尚未结束");
      apiResponse.setCode(501);
      return apiResponse;
    }
    String nextPersonId = jsonObject.getString("nextPersonId");
    String nextPersonName = jsonObject.getString("nextPersonName");
    String businessJson = jsonObject.getString("businessJson");
    String detailConfig = jsonObject.getString("detailConfig");
    List<com.zjtc.model.File> files = jsonObject.getJSONArray("sysFiles")
        .toJavaList(com.zjtc.model.File.class);
    /**查询流程节点记录第二个流程*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .secondAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**退减免单新增一条数据*/
    entity.setNodeCode(user.getNodeCode());
    entity.setDrawer(user.getUsername());
    entity.setIsRevoke("0");
    entity.setStatus("0");
    entity.setType("2");
    entity.setCreateTime(new Date());
    refundOrRefundService.save(entity);
    /**新增附件*/
    if (!entity.getSysFiles().isEmpty()) {
      fileService.updateBusinessId(entity.getId(), files);
    }
    /**流程节点记录表、流程节点线记录表创建数据*/
    String newFirStCodeId = flowNodeInfoService
        .selectAndInsert(user.getNodeCode(), entity.getId(), AuditConstants.PAY_FLOW_CODE,
            firStAudit.get(0).get("flowNodeId").toString());
    entity.setNextNodeId(newFirStCodeId);
    /**修改业务表数据*/
    //待谁审核，谁审核过
    entity.setToAuditPerson(nextPersonId);
    StringBuffer str = new StringBuffer(user.getId());
    str.append(",");
    entity.setAuditPersons(str.toString());
    refundOrRefundService.updateById(entity);
    refundOrRefundService.updateById(entity);
    /**流程进度（操作记录）表 新增三条数据*/
    flowProcessService
        .create(user, entity.getId(), entity.getTreatmentAdvice(), nextPersonName, nextPersonId);
    /**发起待办*/
    String todoContent =
        "用水单位" + entity.getUnitCode() + "(" + entity.getUnitName() + ") 申请减免" + entity.getMoney()
            + "元";
    entity.setAuditFlow(flowProcessMapper.queryAuditList(entity.getId(), user.getNodeCode()));
    todoService
        .add(entity.getId(), user, nextPersonId, nextPersonName, todoContent,
            JSONObject.toJSONString(entity, SerializerFeature.WriteMapNullValue),
            detailConfig,
            AuditConstants.PAY_TODO_TYPE);
    /**websocket推送*/
    webSocketUtil.pushWaterTodo(user.getNodeCode(), nextPersonId);
    /**新增流程实例表数据*/
    flowExampleService.add(user, entity.getId(), AuditConstants.PAY_TODO_TYPE);
    systemLogService.logInsert(user, "缴费管理", "发起减免单", null);
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
    //查询当年
    Calendar date = Calendar.getInstance();
    int year = Integer.valueOf(date.get(Calendar.YEAR));
    return baseMapper.ThreePayMess(year, unitId);
  }

  @Override
  public Map<String, Object> selectPayNotice(JSONObject jsonObject) {
    Map<String, Object> page = new HashMap<>();
    Integer year = jsonObject.getInteger("countYear");
    if (null == year || "0".equals(year)) {
      //默认当年
      Calendar now = Calendar.getInstance();
      year = now.get(Calendar.YEAR);
      jsonObject.put("year", year);
    }
    //数据字典
    String messageTypecode = dictUtils
        .getDictItemCode("messageType", "催缴通知", jsonObject.getString("nodeCode"));
    jsonObject.put("messageTypecode", messageTypecode);
    //查询所有未缴费信息数据
    List<SendListVO> noticeData = baseMapper.selectPayNotice(jsonObject);
    if (noticeData.isEmpty()) {
      return page;
    }
    //查询短信状态
    //sql关键字
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
    systemLogService.logInsert(user, "缴费管理", "催缴通知短信发送", null);
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
      SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
      data.put("dateFormat", dateFmt);
      String fileName = "缴费管理查询结果.xls";
      String templateName = "template/waterUsePayInfoData.xls";
      commonService.export(fileName, templateName, request, response, data);
      systemLogService.logInsert(user, "缴费管理", "导出查询结果", null);
    } else {
      result.setMessage("无导出数据");
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
    //所有单位编号
    List<Map<String, Object>> list = baseMapper.selectUser(jsonObject);
//    //根据单位编号查询单位信息
//    List<Map<String, Object>> map = useWaterUnitMapper.selectByIds(unitCodeList);
    if (!list.isEmpty()) {
      for (Map item : list) {
        String dictItemCode =
            null == item.get("areaCountry") ? null : item.get("areaCountry").toString();
        //所属区域
        if (StringUtils.isNotBlank(dictItemCode)) {
          item.put("areaCountryName", dictUtils
              .getDictItemNameCountry("area_country_code",
                  dictItemCode, nodeCode));
        }
        //查询相关编号
        List<String> idList = useWaterUnitRefService
            .findIdList(item.get("id").toString(), nodeCode);
        String useWaterUnitIdRef = "";
        if (!idList.isEmpty()) {
          //相关编号数据集合
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
        //查询电话号码
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
      SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
      data.put("dateFormat", dateFmt);
      String fileName = "加价用户信息.xls";
      if (null == quarter || quarter.equals("")) {
        fileName = year + "年" + fileName;
      } else {
        fileName = year + "年" + quarter + "季度" + fileName;
      }
      String templateName = "template/waterUsePayInfoUserData.xls";
      commonService.export(fileName, templateName, request, response, data);
      systemLogService.logInsert(user, "缴费管理", "导出用户信息", null);
    } else {
      result.setMessage("无导出数据");
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
      SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
      data.put("dateFormat", dateFmt);
      String fileName = "计划用水户超计划用水情况汇总表.xls";
      if (null == quarter || quarter.equals("")) {
        fileName = year + "年" + fileName;
      } else {
        fileName = year + "年" + quarter + "季度" + fileName;
      }
      String templateName = "template/payInfo.xls";
      commonService.export(fileName, templateName, request, response, data);
      systemLogService.logInsert(user, "缴费管理", "导出计划用水户超计划情况汇总", null);
    } else {
      result.setMessage("无导出数据");
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
          apiResponse.recordError("单位编号:" + unitCode + " 协议号为空 无法导出");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankOfDeposit =
            null == map.get("bankOfDeposit") ? "" : map.get("bankOfDeposit").toString();
        if (StringUtils.isBlank(bankOfDeposit)) {
          apiResponse.recordError("单位编号:" + unitCode + " 开户行为空 无法导出");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankAccount =
            null == map.get("bankAccount") ? "" : map.get("bankAccount").toString();
        if (StringUtils.isBlank(bankAccount)) {
          apiResponse.recordError("单位编号:" + unitCode + " 银行账户为空 无法导出");
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
      apiResponse.recordError("无本行数据 无法导出");
      apiResponse.setCode(501);
      return apiResponse;
    }
    //写文件
    String fileName = "本行托收数据.txt";
    if (null == quarter || quarter.equals("")) {
      fileName = year + "年" + fileName;
    } else {
      fileName = year + "年" + quarter + "季度" + fileName;
    }
    File file = new File(
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName);
    FileOutputStream fos = new FileOutputStream(file.getPath());
    fos.write(str.getBytes());//注意字符串编码
    boolean result = FileUtil.writeBytes(file, fileName, request, response);
    if (result) {
      //删除文件
      file.delete();
    }
    systemLogService.logInsert(user, "缴费管理", "导出本行数据", null);
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
          apiResponse.recordError(unitCode + " 协议号为空 无法导出");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankOfDeposit =
            null == map.get("bankOfDeposit") ? "" : map.get("bankOfDeposit").toString();
        if (StringUtils.isBlank(bankOfDeposit)) {
          apiResponse.recordError("单位编号:" + unitCode + " 开户行为空 无法导出");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String bankAccount =
            null == map.get("bankAccount") ? "" : map.get("bankAccount").toString();
        if (StringUtils.isBlank(bankAccount)) {
          apiResponse.recordError("单位编号:" + unitCode + " 银行账户为空 无法导出");
          apiResponse.setCode(501);
          return apiResponse;
        }
        String signed =
            null == map.get("signed") ? "" : map.get("signed").toString();
        if ("0".equals(signed)) {
          apiResponse.recordError("单位编号:" + unitCode + " 未开通托收 无法导出");
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
      apiResponse.recordError("无他行数据 无法导出");
      apiResponse.setCode(501);
      return apiResponse;
    }
    //写文件
    String fileName = "他行托收数据.txt";
    if (null == quarter || quarter.equals("")) {
      fileName = year + "年" + fileName;
    } else {
      fileName = year + "年" + quarter + "季度" + fileName;
    }
    File file = new File(
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName);
    FileOutputStream fos = new FileOutputStream(file.getPath());
    fos.write(str.getBytes());//注意字符串编码
    boolean result = FileUtil.writeBytes(file, fileName, request, response);
    if (result) {
      //删除文件
      file.delete();
    }
    systemLogService.logInsert(user, "缴费管理", "导出他行数据", null);
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
    //查询当前用户的用户类型
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
    //查询当前用户的用户类型
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

    //要保持的打印记录
    List<PayInfoPrint> payInfoPrintList = new ArrayList<>();

    if (!list.isEmpty()) {
      //新增催缴通知 打印编号打印记录
      for (WaterUsePayInfoVo item : list) {
        //要打印记录的集合
        PayInfoPrint entity = new PayInfoPrint();
        String newPrintNum;
        PayInfoPrint payInfoPrint
            = payInfoPrintService.selectPrintMess(item.getId());
        //如果当季没有,按规则新增打印编号
        if (null == payInfoPrint) {
          newPrintNum = payInfoPrintService
              .createPrintNum(item.getUnitCode(), item.getCountYear().toString(),
                  item.getCountQuarter());
          item.setPrintNum(newPrintNum);

        }
        //如果当季度已存在，未打印，取已有打印编号
        else if ("0".equals(payInfoPrint.getStatus())) {
          newPrintNum = payInfoPrint.getPrintNum();
          entity.setId(payInfoPrint.getId());
        }
        //如果当季度已存在，且已打印，在打印编号上累加1
        else {
          newPrintNum = payInfoPrint.getPrintNum();
          newPrintNum = craeatRank(newPrintNum);
        }
        //在结果集中，插入打印编号
        item.setPrintNum(newPrintNum);
        entity.setPrintNum(newPrintNum);
        entity.setNodeCode(user.getNodeCode());
        entity.setPayId(item.getId());
        entity.setStatus("0");
        payInfoPrintList.add(entity);
      }
      /**新增或修改打印记录*/
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
      //修改缴费记录的是否打印状态
      result1 = baseMapper.updatePrinted(param);
      //修改打印记录的打印状态
      result2 = payInfoPrintService.updatePrinted(param, user);
    }
    return result1 && result2;
  }

  //生成3位排序号
  private String craeatRank(String printNum) {
    String result;
    String maxCount = printNum.substring(printNum.length() - 3);
    //生成新的排序号，最大值加1
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