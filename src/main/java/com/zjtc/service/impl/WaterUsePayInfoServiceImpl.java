package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.CommonUtil;
import com.zjtc.base.util.DictUtils;
import com.zjtc.base.util.FileUtil;
import com.zjtc.mapper.UseWaterUnitMapper;
import com.zjtc.mapper.WaterUsePayInfoMapper;
import com.zjtc.model.Contacts;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.UseWaterUnitInvoice;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.model.vo.UseWaterUnitRefVo;
import com.zjtc.service.CommonService;
import com.zjtc.service.ContactsService;
import com.zjtc.service.FileService;
import com.zjtc.service.FlowExampleService;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.RefundOrRefundService;
import com.zjtc.service.TodoService;
import com.zjtc.service.UseWaterUnitInvoiceService;
import com.zjtc.service.UseWaterUnitRefService;
import com.zjtc.service.WaterUsePayInfoService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
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
    //baseMapper.deleteByParam(jsonObject);
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
    List<com.zjtc.model.File> files = jsonObject.getJSONArray("sysFiles")
        .toJavaList(com.zjtc.model.File.class);
    /**查询流程节点记录第二个流程*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .secondAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**退减免单新增一条数据*/
    entity.setNodeCode(user.getNodeCode());
    entity.setDrawer(user.getUsername());
    entity.setIsRevoke("0");
    entity.setStatus("1");
    entity.setType("1");
    refundOrRefundService.insert(entity);
    /**新增附件*/
    if (!entity.getSysFiles().isEmpty()) {
      fileService.updateBusinessId(entity.getId(),files);
    }
    /**流程节点记录表、流程节点线记录表创建数据*/
    String newFirStCodeId = flowNodeInfoService
        .selectAndInsert(user.getNodeCode(), entity.getId(), AuditConstants.PAY_FLOW_CODE,
            firStAudit.get(0).get("flowNodeId").toString());
    entity.setNextNodeId(newFirStCodeId);
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
    List<com.zjtc.model.File> files = jsonObject.getJSONArray("sysFiles")
        .toJavaList(com.zjtc.model.File.class);
    /**查询流程节点记录第二个流程*/
    List<Map<String, Object>> firStAudit = flowNodeInfoService
        .secondAuditRole(AuditConstants.PAY_FLOW_CODE, user.getNodeCode());
    /**退减免单新增一条数据*/
    entity.setNodeCode(user.getNodeCode());
    entity.setDrawer(user.getUsername());
    entity.setIsRevoke("0");
    entity.setStatus("1");
    entity.setType("2");
    refundOrRefundService.insert(entity);
    /**新增附件*/
    if (!entity.getSysFiles().isEmpty()) {
      fileService.updateBusinessId(entity.getId(),files);
    }
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
  public boolean updateActualAmount(String id, double actualAmount) {
    return baseMapper.updateActualAmount(id,actualAmount);
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
  public void exportQueryData(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<WaterUsePayInfo> list = jsonObject.getJSONArray("data").toJavaList(WaterUsePayInfo.class);
    if (!list.isEmpty()) {
      for (WaterUsePayInfo item : list) {
        //支付方式
        if ("2".equals(item.getPayType())) {
          item.setPayType("现金");
        } else if ("3".equals(item.getPayType())) {
          item.setPayType("转账");
        } else {
          item.setPayType("");
        }
        //缴费时间
        item.setCountDate(item.getCountYear() + "第" + item.getCountQuarter() + "季度");
        //是否托收
        if ("1".equals(item.getPayStatus())) {
          item.setIsSigning("1");
        } else {
          item.setIsSigning("0");
        }
      }
    }
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "缴费管理查询结果.xlsx";
    String templateName = "template/waterUsePayInfoData.xlsx";
    commonService.export(fileName, templateName, request, response, data);
  }

  @Override
  public void exportUser(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    String nodeCode = jsonObject.getString("nodeCode");
    String year = jsonObject.getString("year");
    String quarter = jsonObject.getString("quarter");
    //所有单位编号
    List<Map<String, Object>> list = baseMapper.selectUser(jsonObject);
//    //根据单位编号查询单位信息
//    List<Map<String, Object>> map = useWaterUnitMapper.selectByIds(unitCodeList);
    if (!list.isEmpty()) {
      for (Map item : list) {
        //所属区域
        item.put("areaCountryName", dictUtils
            .getDictItemName("area_country_code", item.get("areaCountry").toString(), nodeCode));
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
          }
        }
      }
    }
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = year + "年" + quarter + "季度加价用户信息.xlsx";
    if (quarter.equals("") && "" == quarter) {
      fileName = year + "年加价用户信息.xlsx";
    }
    String templateName = "template/waterUsePayInfoUserData.xlsx";
    commonService.export(fileName, templateName, request, response, data);

  }

  @Override
  public void exportPayInfo(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    String year = jsonObject.getString("year");
    String quarter = jsonObject.getString("quarter");
    //List<Map> list = jsonObject.getJSONArray("data").toJavaList(Map.class);
    List<Map<String, Object>> list = baseMapper.exportPayInfo(jsonObject);
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = year + "年" + quarter + "季度计划用水户超计划用水情况汇总表.xlsx";
    String templateName = "template/payInfo.xlsx";
    commonService.export(fileName, templateName, request, response, data);
  }

  @Override
  public ApiResponse exportBankInfo(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    ApiResponse apiResponse = new ApiResponse();
    String year = jsonObject.getString("year");
    String quarter = jsonObject.getString("quarter");
    //List<Map> list = jsonObject.getJSONArray("data").toJavaList(Map.class);
    List<Map<String, Object>> list = baseMapper.exportBankInfo(jsonObject);
    String str = "";
    if (!list.isEmpty()) {
      for (Map map : list) {
        String unitCode = map.get("unitCode").toString();
        if (StringUtils.isBlank(map.get("agreementNumber").toString())) {
          apiResponse.recordError(unitCode + " 协议号为空 无法导出");
          return apiResponse;
        }
        if (StringUtils.isBlank(map.get("bankOfDeposit").toString())) {
          apiResponse.recordError(unitCode + " 开户行为空 无法导出");
          return apiResponse;
        }
        if (StringUtils.isBlank(map.get("bankAccount").toString())) {
          apiResponse.recordError(unitCode + " 银行账户为空 无法导出");
          return apiResponse;
        }
        str += StringUtils.trim(map.get("agreementNumber").toString()) + "|" + StringUtils
            .trim(map.get("bankOfDeposit").toString()) + "|"
            + StringUtils.trim(map.get("bankAccount").toString()) + "|" + StringUtils
            .trim(map.get("entrustUnitName").toString()) + "|"
            + StringUtils.trim(map.get("actualAmount").toString()) + "|" + StringUtils
            .trim(map.get("remark").toString()) + "|\r\n";
      }
    } else {
      apiResponse.recordError("无本行数据 无法导出");
      return apiResponse;
    }
    //写文件
    String fileName = year + "年" + quarter + "季度本行托收数据.txt";
    File file = new File(
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName);
    FileOutputStream fos = new FileOutputStream(file.getPath());
    fos.write(str.getBytes());//注意字符串编码
    boolean result = FileUtil.writeBytes(file, fileName, request, response);
    if (result) {
      //删除文件
      file.delete();
    }
    return apiResponse;
  }

  @Override
  public ApiResponse exportOtherBankInfo(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    ApiResponse apiResponse = new ApiResponse();
    String year = jsonObject.getString("year");
    String quarter = jsonObject.getString("quarter");
    //List<Map> list = jsonObject.getJSONArray("data").toJavaList(Map.class);
    List<Map<String, Object>> list = baseMapper.exportOtherBankInfo(jsonObject);
    list.addAll(list);
    String str = "";
    if (!list.isEmpty()) {
      for (Map map : list) {
        String unitCode = map.get("unitCode").toString();
        if (StringUtils.isBlank(map.get("agreementNumber").toString())) {
          apiResponse.recordError(unitCode + " 协议号为空 无法导出");
          return apiResponse;
        }
        if (StringUtils.isBlank(map.get("bankOfDeposit").toString())) {
          apiResponse.recordError(unitCode + " 开户行为空 无法导出");
          return apiResponse;
        }
        if (StringUtils.isBlank(map.get("bankAccount").toString())) {
          apiResponse.recordError(unitCode + " 银行账户为空 无法导出");
          return apiResponse;
        }
        if ("0".equals(map.get("signed").toString())) {
          apiResponse.recordError(unitCode + " 未开通托收 无法导出");
          return apiResponse;
        }
        str += StringUtils.trim(map.get("agreementNumber").toString()) + "|" + StringUtils
            .trim(map.get("bankOfDeposit").toString()) + "|"
            + StringUtils.trim(map.get("bankAccount").toString()) + "|" + StringUtils
            .trim(map.get("entrustUnitName").toString()) + "|"
            + StringUtils.trim(map.get("actualAmount").toString()) + "|" + StringUtils
            .trim(map.get("remark").toString()) + "|\r\n";
      }
    } else {
      apiResponse.recordError("无他行数据 无法导出");
      return apiResponse;
    }
    //写文件
    String fileName = year + "年" + quarter + "季度他行托收数据.txt";
    File file = new File(
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName);
    FileOutputStream fos = new FileOutputStream(file.getPath());
    fos.write(str.getBytes());//注意字符串编码
    boolean result = FileUtil.writeBytes(file, fileName, request, response);
    if (result) {
      //删除文件
      file.delete();
    }
    return apiResponse;
  }
}