package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.DictUtils;
import com.zjtc.mapper.waterBiz.UseWaterUnitMapper;
import com.zjtc.model.Contacts;
import com.zjtc.model.File;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.UseWaterUnitRef;
import com.zjtc.model.User;
import com.zjtc.model.vo.RefEditData;
import com.zjtc.model.vo.UseWaterUnitRefVo;
import com.zjtc.model.vo.UseWaterUnitVo;
import com.zjtc.service.BankService;
import com.zjtc.service.CommonService;
import com.zjtc.service.ContactsService;
import com.zjtc.service.FileService;
import com.zjtc.service.UseWaterQuotaService;
import com.zjtc.service.UseWaterUnitMeterService;
import com.zjtc.service.UseWaterUnitModifyService;
import com.zjtc.service.UseWaterUnitRefService;
import com.zjtc.service.UseWaterUnitRoleService;
import com.zjtc.service.UseWaterUnitService;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Service
public class UseWaterUnitServiceImpl extends
    ServiceImpl<UseWaterUnitMapper, UseWaterUnit> implements
    UseWaterUnitService {

  private final static Map<String, String> DANWEI_MAPPER;

  static {
    DANWEI_MAPPER = new HashMap<>();
    DANWEI_MAPPER.put("industry", "industry");
    DANWEI_MAPPER.put("area_country", "areaCountry");
    DANWEI_MAPPER.put("unit_address", "unitAddress");
    DANWEI_MAPPER.put("zip_address", "zipAddress");
    DANWEI_MAPPER.put("zip_name", "zipName");
    DANWEI_MAPPER.put("department", "department");
    DANWEI_MAPPER.put("invoice_unit_name", "invoiceUnitName");
    DANWEI_MAPPER.put("abnormal", "abnormal");
    DANWEI_MAPPER.put("abnormal_cause", "abnormalCause");
  }

  @Autowired
  private UseWaterUnitRoleService useWaterUnitRoleService;
  @Autowired
  private UseWaterUnitRefService useWaterUnitRefService;
  @Autowired
  private UseWaterUnitMeterService useWaterUnitMeterService;
  @Autowired
  private BankService bankService;
  @Autowired
  private ContactsService contactsService;
  @Autowired
  private FileService fileService;
  @Autowired
  private UseWaterQuotaService useWaterQuotaService;
  @Autowired
  private UseWaterUnitModifyService useWaterUnitModifyService;
  @Autowired
  private CommonService commonService;
  @Autowired
  DictUtils dictUtils;
  /**
   * 附件上传盘符
   */
  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;

  /**
   * 附件上传目录
   */
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;
  /**
   * 所属区域字典码
   */
  private final static String AREA_COUNTRY_CODE = "area_country_code";

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse save(UseWaterUnit entity, User user) {
    ApiResponse apiResponse = new ApiResponse();
    //当前序号，取unitCode 7-9位
    String rank = entity.getUnitCode().substring(7, 9);
    /**验证当前用户是否有操作当前类型的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), entity.getUnitCode(), user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该类型权限");
      return apiResponse;
    }
    /**验证单位编号是否重复,先查询出当前节点编码*/
    if (ValidateUnit(entity.getUnitCode(), user.getNodeCode(), entity.getId())) {
      apiResponse.recordError("当前排序号重复");
      /**生成新的排序号*/
      String maxCount = baseMapper.maxUnitCode(entity.getUnitCode(), null, user.getNodeCode());
      maxCount = craeatRank(maxCount);
      apiResponse.setData(maxCount);
      return apiResponse;
    }

    /**新增用水单位表数据*/
    entity.setDeleted("0");
    entity.setCreateTime(new Date());
    entity.setNodeCode(user.getNodeCode());
    //是否是主户
    entity.setImain("0");
    //是否异常
    entity.setAbnormal("0");
    //批次
    entity.setUnitCodeGroup(entity.getUnitCode().substring(2, 4));
    //类型
    entity.setUnitCodeType(entity.getUnitCode().substring(4, 6));
    this.insert(entity);
    /**新增水表数据*/
    if (!entity.getMeterList().isEmpty()) {
      useWaterUnitMeterService
          .insertUseWaterUnitMeter(entity.getMeterList(), entity.getId(), user.getNodeCode());
    }
    /**新增银行数据*/
    if (!entity.getBankList().isEmpty()) {
      bankService.insertBank(entity.getBankList(), entity.getId(), user.getNodeCode());
    }
    /**新增联系人数据*/
    if (!entity.getContactsList().isEmpty()) {
      contactsService
          .add(entity.getContactsList(), entity.getId(), entity.getUnitCode(),
              user.getNodeCode());
    }
    /**新增责任书数据:附件*/
    if (!entity.getSysFile().isEmpty()) {
      //绑定附件表业务id
      fileService.updateBusinessId(entity.getId(), entity.getSysFile());
    }
    /**新增用水定额数据*/
    if (!entity.getQuotaFile().isEmpty()) {
      useWaterQuotaService
          .add(entity.getQuotaFile(), entity.getId(), user.getNodeCode());
    }
    /**新增相关编号信息数据*/
    if (StringUtils.isNotBlank(entity.getUseWaterUnitIdRef())) {
      useWaterUnitRefService
          .save(entity.getUseWaterUnitIdRef(), entity.getId(), user.getNodeCode());
    }

    return apiResponse;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse update(UseWaterUnit entity, User user) {
    ApiResponse apiResponse = new ApiResponse();
    /**验证当前用户是否有操作当前类型的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), entity.getUnitCode(), user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该类型权限");
      return apiResponse;
    }
    /**验证单位编号是否重复,先查询出当前节点编码*/
    if (ValidateUnit(entity.getUnitCode(), user.getNodeCode(), entity.getId())) {
      apiResponse.recordError("当前排序号重复");
      /**生成新的排序号*/
      String maxCount = baseMapper
          .maxUnitCode(entity.getUnitCode(), entity.getId(), user.getNodeCode());
      maxCount = craeatRank(maxCount);
      apiResponse.setData(maxCount);
      return apiResponse;    }
    /*********************************************************/
    /**1.修改数据*/
    /**1.1 修改用水单位信息*/
    //批次
    entity.setUnitCodeGroup(entity.getUnitCode().substring(2, 4));
    //类型
    entity.setUnitCodeType(entity.getUnitCode().substring(4, 6));
    /**判断名称是否被修改，新增单位名称修改日志信息数据*/
    useWaterUnitModifyService
        .insertUnitName(entity.getId(), user.getNodeCode(), entity.getUnitName(),
            user.getUsername(), user.getId());
    this.updateById(entity);
    /**1.2选择主户*/
    String userWaterUnitId = entity.getUseWaterUnitIdRef(); //选择的主户
    if (StringUtils.isNotBlank(userWaterUnitId)) {
      /**1.2.1如果选择主户，是相关联的数据，只修改主户字段*/
      List<String> redIds = useWaterUnitRefService.findIdList(entity.getId(), user.getNodeCode());
      if (isEmptyId(userWaterUnitId, redIds)) {
        //所有关联单位(不排己)，全部初始化为非主户
        List<UseWaterUnit> useWaterUnitsList = new ArrayList<>();
        for (String id : redIds) {
          UseWaterUnit param = new UseWaterUnit();
          param.setId(id);
          //是否是主户字段为否
          param.setImain("0");
          useWaterUnitsList.add(param);
        }
        this.updateBatchById(useWaterUnitsList);

      } else {
        /**1.2.2.如果是之前没有关联关系的数据，加入关联关系,这是选择关联的账户，为下级单位*/
        useWaterUnitRefService
            .save(entity.getId(), userWaterUnitId, user.getNodeCode());
      }
    }

    /**更改指定的单位为主户*/
    UseWaterUnit useWaterUnitAdd = new UseWaterUnit();
    useWaterUnitAdd.setId(userWaterUnitId);
    useWaterUnitAdd.setImain("1");
    this.updateById(useWaterUnitAdd);

    /**1.3. 修改水表数据*/
    useWaterUnitMeterService.deletedUseWaterUnitMeter(entity.getId());
    if (!entity.getMeterList().isEmpty()) {
      useWaterUnitMeterService
          .insertUseWaterUnitMeter(entity.getMeterList(), entity.getId(), user.getNodeCode());
    }
    /**1.4. 修改银行数据*/
    bankService.deletedBank(entity.getId());
    if (!entity.getBankList().isEmpty()) {
      bankService.insertBank(entity.getBankList(), entity.getId(), user.getNodeCode());
    }
    /**1.5. 修改联系人数据*/
    contactsService.deleteContacts(entity.getId());
    if (!entity.getContactsList().isEmpty()) {
      contactsService
          .add(entity.getContactsList(), entity.getId(), entity.getUnitCode(),
              user.getNodeCode());
    }
    /**1.6. 修改责任书数据,如果传入的附件删除状态为:1,删除附件,为0，绑定业务id*/
    if (!entity.getSysFile().isEmpty()) {
      fileService.updateBusinessId(entity.getId(), entity.getSysFile());
    }
    /**1.7.删除关联关表数据*/
    if (!entity.getUseWaterUnitRefList().isEmpty()) {
      //1.1传入的关联单位id,删除当前的关联关系，当前节点的子节点跟当前节点的父节点关联,
      //1.1.如果没有子节点或没有父节点，只删除自身
      List<UseWaterUnitRefVo> useWaterUnitRefs = entity.getUseWaterUnitRefList();
      for (UseWaterUnitRefVo item : useWaterUnitRefs) {
        //查询当前节点的下级节点
        Wrapper sonWrapper = new EntityWrapper();
        sonWrapper.eq("node_code", user.getNodeCode());
        sonWrapper.eq("use_water_unit_id", item.getId());
        List<UseWaterUnitRef> sonList = useWaterUnitRefService.selectList(sonWrapper);
        List<String> sonIds = new ArrayList<>();
        //查询当前节点的上级节点
        Wrapper parWrapper = new EntityWrapper();
        parWrapper.eq("node_code", user.getNodeCode());
        parWrapper.eq("use_water_unit_id_ref", item.getId());
        List<UseWaterUnitRef> parList = useWaterUnitRefService.selectList(parWrapper);
        if (!parList.isEmpty() && !sonList.isEmpty()) {
          //当前节点的子节点跟当前节点的父节点关联
          for (UseWaterUnitRef ref : sonList) {
            ref.setUseWaterUnitId(parList.get(0).getUseWaterUnitId());
            sonIds.add(ref.getId());
          }
          //删除当前节点与父节点的关联
          useWaterUnitRefService.deleteById(parList.get(0).getId());
          //当前节点的子节点与当前节点的父节点重新建立关联
          useWaterUnitRefService.updateBatchById(sonList);
        }
        if (parList.isEmpty() && !sonList.isEmpty()) {
          useWaterUnitRefService.deleteBatch(sonIds);
        }
        if (sonList.isEmpty() && !parList.isEmpty()) {
          useWaterUnitRefService.deleteById(parList.get(0).getId());
        }
      }
    }

    /**1.8.修改用水定额数据*/
    useWaterQuotaService.deleteQuotas(entity.getId());
    if (!entity.getQuotaFile().isEmpty()) {
      useWaterQuotaService
          .add(entity.getQuotaFile(), entity.getId(), user.getNodeCode());
    }

    /************************************************************************/
    /*****************************关联修改************************************/
    /************************************************************************/
    /**2.关联修改信息*/
    if (null != entity.getRefEditData()) {
      List<String> idsList = entity.getRefEditData().getRefIds();
      if (!idsList.isEmpty()) {
        RefEditData refEditData = entity.getRefEditData();
        /**2.1.基本信息同步*/
        List<String> useWaterUnitColumn = refEditData.getUseWaterUnitColumn();
        if (!useWaterUnitColumn.isEmpty()) {
          String sql1 = updateSql(useWaterUnitColumn, entity);
          baseMapper.updateUseWaterUnit(idsList, sql1);
        }
        /**2.2.银行信息同步*/
        if ("true".equals(refEditData.getBankColumn()) && !entity.getBankList().isEmpty()) {
          bankService.deletedBank(idsList);
          for (String item : idsList) {
            bankService.insertBank(entity.getBankList(), item, user.getNodeCode());
          }
        }
        /**2.3.联系人信息同步*/
        if ("true".equals(refEditData.getContactsColumn()) && !entity.getContactsList().isEmpty()) {
          contactsService.deleteContacts(idsList);
          for (String item : idsList) {
            contactsService
                .add(entity.getContactsList(), item, entity.getUnitCode(),
                    user.getNodeCode());
          }
        }
        /**2.4.责任书信息同步*/
        if ("true".equals(refEditData.getFileColumn()) && !entity.getSysFile().isEmpty()) {
          //删除之前的责任书
          List<File> file = entity.getSysFile();
          for (String id : idsList) {
            fileService.removeByBusinessId(id);
            //同步责任书编号
            UseWaterUnit updateParam = new UseWaterUnit();
            updateParam.setId(id);
            updateParam.setResponsibilityCode(entity.getResponsibilityCode());
            this.updateById(updateParam);
            for (File items : file) {
              items.setId("");
              items.setBusinessId(id);
            }
          }
          fileService.insertBatch(file);
        }
        /**2.5.用水定额信息同步*/
        if ("true".equals(refEditData.getQuotaFileColumn()) && !entity.getQuotaFile().isEmpty()) {
          useWaterQuotaService.deleteQuotas(idsList);
          for (String item : idsList) {
            useWaterQuotaService
                .add(entity.getQuotaFile(), item, user.getNodeCode());
          }
        }
      }

    }
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean delete(JSONObject jsonObject, User user) {
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    /**批量删除*/
    /**删除用水单位数据逻辑删除，逻辑删除*/
    UseWaterUnit useWaterUnit = new UseWaterUnit();
    useWaterUnit.setDeleted("1");
    useWaterUnit.setDeleteTime(new Date());
    Wrapper wrapper = new EntityWrapper();
    wrapper.in("id", ids);
    this.update(useWaterUnit, wrapper);
    /**删除水表数据*/
    useWaterUnitMeterService.deletedUseWaterUnitMeter(ids);
    /**删除银行数据，逻辑删除*/
    bankService.deletedBank(ids);
    /**删除联系人数据，逻辑删除*/
    contactsService.deleteContacts(ids);
    /**删除责任书数据*/
    fileService.removeByBusinessIds(ids);
    /**删除用水定额数据*/
    useWaterQuotaService.deleteQuotas(ids);
    /**删除相关编号信息数据 */
    for (String id : ids) {
      //查询当前节点的下级节点
      Wrapper sonWrapper = new EntityWrapper();
      sonWrapper.eq("node_code", user.getNodeCode());
      sonWrapper.eq("use_water_unit_id", id);
      List<UseWaterUnitRef> sonList = useWaterUnitRefService.selectList(sonWrapper);
      List<String> sonIds = new ArrayList<>();
      //查询当前节点的上级节点
      Wrapper parWrapper = new EntityWrapper();
      parWrapper.eq("node_code", user.getNodeCode());
      parWrapper.eq("use_water_unit_id_ref", id);
      List<UseWaterUnitRef> parList = useWaterUnitRefService.selectList(parWrapper);
      if (!parList.isEmpty() && !sonList.isEmpty()) {
        //当前节点的子节点跟当前节点的父节点关联
        for (UseWaterUnitRef ref : sonList) {
          ref.setUseWaterUnitId(parList.get(0).getUseWaterUnitId());
          sonIds.add(ref.getId());
        }
        //删除当前节点与父节点的关联
        useWaterUnitRefService.deleteById(parList.get(0).getId());
        //当前节点的子节点与当前节点的父节点重新建立关联
        useWaterUnitRefService.updateBatchById(sonList);
      }
      if (parList.isEmpty() && !sonList.isEmpty()) {
        useWaterUnitRefService.deleteBatch(sonIds);
      }
      if (sonList.isEmpty() && !parList.isEmpty()) {
        useWaterUnitRefService.deleteById(parList.get(0).getId());
      }
    }
    return true;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    String nodeCode = jsonObject.getString("nodeCode");
    Map<String, Object> page = new LinkedHashMap<>();
    jsonObject.put("dictCode", AREA_COUNTRY_CODE);
    List<UseWaterUnitVo> result = baseMapper.queryPage(jsonObject);
    if (!result.isEmpty()) {
      for (UseWaterUnitVo item : result) {
        //查询相关编号
        List<String> idList = useWaterUnitRefService
            .findIdList(item.getId(), nodeCode);
        if (!idList.isEmpty()) {
          //相关编号集合
          List<UseWaterUnitRefVo> useWaterUnitRefList = baseMapper
              .queryUnitRef(idList, nodeCode, jsonObject.getString("userId"),
                  item.getId());
          item.setUseWaterUnitRefList(useWaterUnitRefList);
          //相关编号，用逗号隔开
          String useWaterUnitIdRef = "";
          if (!useWaterUnitRefList.isEmpty()) {
            for (UseWaterUnitRefVo useWaterUnitRefVo : useWaterUnitRefList) {
              useWaterUnitIdRef += useWaterUnitRefVo.getUnitCode() + ",";
            }
          }
          if(useWaterUnitIdRef.length()>0){
            item.setUseWaterUnitIdRef(useWaterUnitIdRef.substring(0, useWaterUnitIdRef.length() - 1));
          }
        }
        //查询所属区域
        item.setAreaCountryName(
            dictUtils.getDictItemName("area_country_code", item.getAreaCountry(), nodeCode));
      }
    }
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //查询总数据条数
    long total = baseMapper.queryListTotal(jsonObject);
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  public UseWaterUnit selectById(JSONObject jsonObject, User user) {
    /**先根据id查询用水单位信息*/
    /**查询水表信息*/
    /**查询银行信息*/
    /**查询联系人信息*/
    /**查询责任书信息*/
    /**查询用水定额信息*/
    /**查询单位名称修改日志*/
    jsonObject.put("dictCode", AREA_COUNTRY_CODE);
    jsonObject.put("nodeCode", user.getNodeCode());
    jsonObject.put("userId", user.getId());
    UseWaterUnit useWaterUnit = baseMapper.selectById(jsonObject);
    /**查询相关编号信息*/
    if (null != useWaterUnit) {
      List<String> idList = useWaterUnitRefService
          .findIdList(useWaterUnit.getId(), user.getNodeCode());
      if (!idList.isEmpty()) {
        useWaterUnit.setUseWaterUnitRefList(
            baseMapper.queryUnitRef(idList, user.getNodeCode(), user.getId(),
                jsonObject.getString("id")));
      }
    }
    return useWaterUnit;

  }

  @Override
  public List<Map<String, Object>> findUnitCode(JSONObject yo) {

    return null;
  }

  @Override
  public ApiResponse createunitCode(User user, String unitCode, String id) {
    ApiResponse apiResponse = new ApiResponse();
    /**验证当前用户是否有新增当前批次的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), unitCode, user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该类型权限");
      return apiResponse;
    }
    /**排序号：查询当前节点编码、当前批次,节点编码后三位最大值*/
    String maxCount = baseMapper.maxUnitCode(unitCode, id, user.getNodeCode());
    maxCount = craeatRank(maxCount);
    apiResponse.setData(maxCount);
    return apiResponse;
  }

  /**
   * 生成3位序号
   */
  private String craeatRank(String maxCount) {
    //生成新的排序号，最大值加1
    int count = 0;
    if (StringUtils.isNotBlank(maxCount)) {
      count = Integer.parseInt(maxCount) + 1;
    } else {
      maxCount = "001";
    }

    if (count > 0) {
      maxCount = "00" + String.valueOf(count);
    }
    if (count > 9) {
      maxCount = "0" + String.valueOf(count);
    }
    if (count > 99) {
      maxCount = String.valueOf(count);
    }
    return maxCount;
  }

  @Override
  public String createAreaCode(String nodeCode) {
    /**区域码，取当前节点编码后2位*/
    String areaCode = nodeCode.substring(nodeCode.length() - 2);
    return areaCode;
  }

  @Override
  public List<Map<String, Object>> addUnitCodeList(User user) {
    //查询当前节点编码下所有可操作批次的所有单位的单位编码、单位名称
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("deleted", "0");
    wrapper.eq("node_code", user.getNodeCode());
    List<String> param = useWaterUnitRoleService
        .selectUseWaterUnitRole(user.getId(), user.getNodeCode());
    wrapper.in("unit_code_type", param);
    wrapper.setSqlSelect("id,unit_code as unitCode,unit_name as unitName");
    return this.selectList(wrapper);
  }

  @Override
  public UseWaterUnit selectByUnitCode(String unitCode, User user) {
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("deleted", "0");
    wrapper.eq("node_code", user.getNodeCode());
    wrapper.eq("unit_code", unitCode);
    UseWaterUnit result = this.selectOne(wrapper);
    return result;
  }

  @Override
  public void exportAccountAudit(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<Map<String, Object>> list = baseMapper
        .exportAccountAudit(jsonObject.getString("nodeCode"));
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "计划用水户账户审核表.xlsx";
    String templateName = "template/accountAudit.xlsx";
    commonService.export(fileName, templateName, request, response, data);

  }

  @Override
  public void exportForm(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<Map<String, Object>> list = baseMapper
        .exportForm(jsonObject.getString("nodeCode"));
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "计划用水户开通格式.xlsx";
    String templateName = "template/form.xlsx";
    commonService.export(fileName, templateName, request, response, data);
  }

  @Override
  public void exportRevoca(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    String nodeCode = jsonObject.getString("nodeCode");
    //开始时间
    String startTime = jsonObject.getString("startTime");
    //结束时间
    String endTime = jsonObject.getString("endTime");
    //开户行行名
    String sBankName = dictUtils.getDictItemName("receive_user_info", "1", nodeCode);
    jsonObject.put("sBankName", sBankName);
    //开户行号
    String sBankNum = dictUtils.getDictItemName("receive_user_info", "2", nodeCode);
    jsonObject.put("sBankNum", sBankNum);
    //户名
    String sUnitName = dictUtils.getDictItemName("receive_user_info", "3", nodeCode);
    jsonObject.put("sUnitName", sUnitName);
    //账号
    String sBankAccount = dictUtils.getDictItemName("receive_user_info", "4", nodeCode);
    jsonObject.put("sBankAccount", sBankAccount);
    if (StringUtils.isNotBlank(startTime)) {
      jsonObject.put("startTime", startTime + " 00:00:00");
    }
    if (StringUtils.isNotBlank(endTime)) {
      jsonObject.put("endTime", endTime + " 23:59:59");
    }
    List<Map<String, Object>> list = baseMapper
        .exportRevoca(jsonObject);
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "计划用水户撤销格式.xlsx";
    String templateName = "template/Revoca.xlsx";
    commonService.export(fileName, templateName, request, response, data);
  }

  @Override
  public void exportQueryData(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<Map> map = jsonObject.getJSONArray("data")
        .toJavaList(Map.class);
    if (!map.isEmpty()) {
      for (Map item : map) {
        //是否是节水单位
        if ("1".equals(item.get("saveUnitType"))) {
          item.put("saveUnitType","是");
        } else {
          item.put("saveUnitType","否");
        }
        //是否签约
        if ("1".equals(item.get("signed"))) {
          item.put("signed","是");
        } else {
          item.put("signed","否");
        }
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
      data.put("excelData", map);
      data.put("nowDate", new Date());
      SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
      data.put("dateFormat", dateFmt);
      String fileName = "用水户界面查询结果.xlsx";
      String templateName = "template/useWaterUnitData.xlsx";
      commonService.export(fileName, templateName, request, response, data);

  }

  @Override
  public void exportMoreAndLess(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<Map<String, Object>> list = baseMapper
        .exportMoreAndLess(jsonObject.getString("nodeCode"));
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "用水单位增减情况表.xlsx";
    String templateName = "template/useWaterUnitMoreAndLess.xlsx";
    commonService.export(fileName, templateName, request, response, data);
  }

  /**
   * 验证单位编号是否重复
   */
  private Boolean ValidateUnit(String unitCode, String nodeCode, String id) {
    EntityWrapper entityWrapper = new EntityWrapper();
    entityWrapper.eq("node_code", nodeCode);
    entityWrapper.eq("unit_code", unitCode);
    entityWrapper.eq("deleted", "0");
    if (StringUtils.isNotBlank(id)) {
      entityWrapper.notIn("id", id);
    }
    return this.selectCount(entityWrapper) > 0;
  }

  private boolean isEmptyId(String useWaterUnitIdRef, List<String> param) {
    for (String item : param) {
      if (item.equals(useWaterUnitIdRef)) {
        return true;
      }
    }
    return false;
  }

  @SneakyThrows
  public String updateSql(List<String> param, Object object) {
    Class<?> clazz = object.getClass();
    StringBuilder builder = new StringBuilder();
    String sql;
    for (String item : param) {
      builder.append(item);
      builder.append("=");
      builder.append("'");
      String fieldName = DANWEI_MAPPER.get(item);
      //通过字符串的变化拼接处getName，即获取name的get方法
      String methodName = "get" +
          fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Method method = clazz.getMethod(methodName);
      Object result = method.invoke(object);
      builder.append(result);
      builder.append("'");
      builder.append(",");
    }
    sql = builder.toString();
    sql = sql.substring(0, sql.length() - 1);
    return sql;
  }

}
