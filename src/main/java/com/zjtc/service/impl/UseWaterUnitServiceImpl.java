package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterUnitMapper;
import com.zjtc.model.Bank;
import com.zjtc.model.File;
import com.zjtc.model.UseWaterQuota;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.UseWaterUnitRef;
import com.zjtc.model.User;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.model.vo.RefEditData;
import com.zjtc.model.vo.UseWaterUnitRefVo;
import com.zjtc.service.BankService;
import com.zjtc.service.ContactsService;
import com.zjtc.service.FileService;
import com.zjtc.service.UseWaterQuotaService;
import com.zjtc.service.UseWaterUnitMeterService;
import com.zjtc.service.UseWaterUnitModifyService;
import com.zjtc.service.UseWaterUnitRefService;
import com.zjtc.service.UseWaterUnitRoleService;
import com.zjtc.service.UseWaterUnitService;
import com.zjtc.service.WaterMonthUseDataService;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.html.parser.Entity;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    DANWEI_MAPPER.put("unitAddress", "unitAddress");
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
  private WaterMonthUseDataService waterMonthUseDataService;
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
    /**验证当前用户是否有操作当前批次的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), entity.getUnitCode(), user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该批次权限");
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
      //绑定月使用水量表单位水表
      for (UseWaterUnitMeter item : entity.getMeterList()) {
        Wrapper entity1 = new EntityWrapper<>();
        entity1.eq("node_code", user.getNodeCode());
        entity1.eq("water_meter_code", item.getWaterMeterCode());
        List<WaterMonthUseData> waterMonthUseData = waterMonthUseDataService.selectList(entity1);
        waterMonthUseData.get(0).setUseWaterUnitId(entity.getId());
      }
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
    /*********************************************************/
    /**一：修改数据*/
    ApiResponse apiResponse = new ApiResponse();
    //当前序号，取unitCode 7-9位
    String rank = entity.getUnitCode().substring(7, 9);
    /**验证当前用户是否有操作当前批次的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), entity.getUnitCode(), user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该批次权限");
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
      return apiResponse;
    }
    /**修改用水单位信息*/
    //批次
    entity.setUnitCodeGroup(entity.getUnitCode().substring(2, 4));
    //类型
    entity.setUnitCodeType(entity.getUnitCode().substring(4, 6));
    this.updateById(entity);
    //修改水表数据
    useWaterUnitMeterService.deletedUseWaterUnitMeter(entity.getId());
    if (!entity.getMeterList().isEmpty()) {
      useWaterUnitMeterService
          .insertUseWaterUnitMeter(entity.getMeterList(), entity.getId(), user.getNodeCode());
    }
    /**修改银行数据*/
    bankService.deletedBank(entity.getId());
    if (!entity.getBankList().isEmpty()) {
      bankService.insertBank(entity.getBankList(), entity.getId(), user.getNodeCode());
    }
    /**修改联系人数据*/
    contactsService.deleteContacts(entity.getId());
    if (!entity.getContactsList().isEmpty()) {
      contactsService
          .add(entity.getContactsList(), entity.getId(), entity.getUnitCode(),
              user.getNodeCode());
    }
    /**修改责任书数据,如果传入的附件删除状态为:1,删除附件,为0，绑定业务id*/
    if (!entity.getSysFile().isEmpty()) {
      fileService.updateBusinessId(entity.getId(), entity.getSysFile());
    }
    /**修改用水定额数据*/
    useWaterQuotaService.deleteQuotas(entity.getId());
    if (!entity.getQuotaFile().isEmpty()) {
      useWaterQuotaService
          .add(entity.getQuotaFile(), entity.getId(), user.getNodeCode());
    }
    /**操作相关编号信息数据 */
    /**1.选择主户*/
    String userWaterUnitId = entity.getUseWaterUnitIdRef(); //选择的主户
    /**1.1如果选择主户，是相关联的数据，只修改主户字段*/
    List<String> redIds = useWaterUnitRefService.findIdList(entity.getId(), user.getNodeCode());
    if (isEmptyId(userWaterUnitId, redIds)) {
      List<UseWaterUnit> useWaterUnitsList = new ArrayList<>();
      for (String id : redIds) {
        UseWaterUnit param = new UseWaterUnit();
        param.setId(id);
        //是否是主户字段为否
        param.setImain("0");
        useWaterUnitsList.add(param);
      }
      //批量修改住户字段
      this.updateBatchById(useWaterUnitsList);

    } else {
      /**1.2.如果是之前没有关联关系的数据，加入关联关系,这是选择关联的账户，为下级单位*/
      useWaterUnitRefService
          .save(entity.getId(), userWaterUnitId, user.getNodeCode());
    }
    //新增主户
    UseWaterUnit useWaterUnitAdd = new UseWaterUnit();
    useWaterUnitAdd.setId(userWaterUnitId);
    useWaterUnitAdd.setImain("1");
    this.updateById(useWaterUnitAdd);
    /**2.删除关联关表数据*/
    if (!entity.getUseWaterUnitRefList().isEmpty()) {
      //1.原则就是传入的关联单位id,当前节点的子节点跟当前节点的父节点关联
      //2.如果没有子节点或没有父节点，只删除
      List<UseWaterUnitRefVo> useWaterUnitRefs = entity.getUseWaterUnitRefList();
      for (UseWaterUnitRefVo item : useWaterUnitRefs) {
        //查询当前节点的下级节点
        Wrapper sonWrapper = new EntityWrapper();
        sonWrapper.eq("node_code", user.getNodeCode());
        sonWrapper.eq("use_water_unit_id", item.getId());
        List<UseWaterUnitRef> sonList = useWaterUnitRefService.selectList(sonWrapper);
        //查询当前节点的上级节点
        Wrapper parWrapper = new EntityWrapper();
        parWrapper.eq("node_code", user.getNodeCode());
        parWrapper.eq("use_water_unit_id_ref", item.getId());
        List<UseWaterUnitRef> parList = useWaterUnitRefService.selectList(parWrapper);
        if (!parList.isEmpty() && !sonList.isEmpty()) {
          //当前节点的子节点跟当前节点的父节点关联
          for (UseWaterUnitRef ref : sonList) {
            ref.setUseWaterUnitId(parList.get(0).getUseWaterUnitId());
          }
          //删除当前节点与父节点的关联
          useWaterUnitRefService.deleteById(parList.get(0).getId());
          //当前节点的子节点与当前节点的父节点重新建立关联
          useWaterUnitRefService.updateBatchById(sonList);
        } else if (parList.isEmpty()) {
          useWaterUnitRefService.deleteById(sonList.get(0).getId());
        } else if (sonList.isEmpty()) {
          useWaterUnitRefService.deleteById(parList.get(0).getId());
        }
      }
    }
    /**判断名称是否被修改，新增单位名称修改日志信息数据*/
    useWaterUnitModifyService
        .insertUnitName(entity.getId(), user.getNodeCode(), entity.getUnitName(),
            user.getUsername(), user.getId());
    /************************************************************************/
    /*****************************关联修改************************************/
    /************************************************************************/
    /**二：关联修改信息*/
    if (null != entity.getRefEditData()) {
      List<String> idsList = entity.getRefEditData().getRefIds();
      if (!idsList.isEmpty()) {
        RefEditData refEditData = entity.getRefEditData();
        /**基本信息同步*/
        List<String> useWaterUnitColumn = refEditData.getUseWaterUnitColumn();
        if (!useWaterUnitColumn.isEmpty()) {
          String sql1 = updateSql(useWaterUnitColumn, entity);
          baseMapper.updateUseWaterUnit(idsList, sql1);
        }
        /**水表信息同步*/
        if ("true".equals(refEditData.getMeterColumn())) {
          useWaterUnitMeterService.deletedUseWaterUnitMeter(idsList);
          for (String item : idsList) {
            useWaterUnitMeterService
                .insertUseWaterUnitMeter(entity.getMeterList(), item, user.getNodeCode());
          }
        }
        /**银行信息同步*/
        if ("true".equals(refEditData.getBankColumn())) {
          bankService.deletedBank(idsList);
          for (String item : idsList) {
            bankService.insertBank(entity.getBankList(), item, user.getNodeCode());
          }
        }
        /**联系人信息同步*/
        if ("true".equals(refEditData.getContactsColumn())) {
          contactsService.deleteContacts(idsList);
          for (String item : idsList) {
            contactsService
                .add(entity.getContactsList(), item, entity.getUnitCode(),
                    user.getNodeCode());
          }
        }
        /**责任书信息同步*/
        if ("true".equals(refEditData.getFileColumn())) {
          List<File> file = entity.getSysFile();
          for (String item : idsList) {
            for (File items : file) {
              items.setId("");
              items.setBusinessId(item);
            }
          }
          fileService.insertBatch(file);
        }
        /**用水定额信息同步*/
        if ("true".equals(refEditData.getQuotaFileColumn())) {
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
    //清除绑定月使用水量表单位水表
    Wrapper wrapper1 = new EntityWrapper();
    wrapper1.in("id", ids);
    WaterMonthUseData waterMonthUseData = new WaterMonthUseData();
    waterMonthUseData.setUseWaterUnitId("");
    waterMonthUseDataService.update(waterMonthUseData, wrapper);
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
      //查询当前节点的上级节点
      Wrapper parWrapper = new EntityWrapper();
      parWrapper.eq("node_code", user.getNodeCode());
      parWrapper.eq("use_water_unit_id_ref", id);
      List<UseWaterUnitRef> parList = useWaterUnitRefService.selectList(parWrapper);
      if (!parList.isEmpty() && !sonList.isEmpty()) {
        //当前节点的子节点跟当前节点的父节点关联
        for (UseWaterUnitRef ref : sonList) {
          ref.setUseWaterUnitId(parList.get(0).getUseWaterUnitId());
        }
        //删除当前节点与父节点的关联
        useWaterUnitRefService.deleteById(parList.get(0).getId());
        //当前节点的子节点与当前节点的父节点重新建立关联
        useWaterUnitRefService.updateBatchById(sonList);
      } else if (parList.isEmpty()) {
        useWaterUnitRefService.deleteById(sonList.get(0).getId());
      } else if (sonList.isEmpty()) {
        useWaterUnitRefService.deleteById(parList.get(0).getId());
      }
    }

    /**删除单位名称修改日志数据*/
    return true;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    jsonObject.put("dictCode", AREA_COUNTRY_CODE);
    List<Map<String, Object>> result = baseMapper.queryPage(jsonObject);
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
      apiResponse.recordError("当前用户没有操作该批次权限");
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
    wrapper.in("unit_code_group", param);
    wrapper.setSqlSelect("id,unit_code as unitCode,unit_name as unitName");
    return this.selectList(wrapper);
  }

  @Override
  public UseWaterUnit selectByUnitCode(String unitCode, User user) {
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("deleted", "0");
    wrapper.eq("node_code", user.getNodeCode());
    wrapper.eq("unit_code", unitCode);
    List<UseWaterUnit> result = this.selectList(wrapper);
    if (!result.isEmpty()) {
      return result.get(0);
    }
    return null;
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
    System.out.println(sql);
    return sql;
  }

}
