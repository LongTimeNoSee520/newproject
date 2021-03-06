package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.DictUtils;
import com.zjtc.mapper.waterBiz.UseWaterUnitMapper;
import com.zjtc.model.Contacts;
import com.zjtc.model.File;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.UseWaterUnitRef;
import com.zjtc.model.User;
import com.zjtc.model.vo.AddressBook;
import com.zjtc.model.vo.RefEditData;
import com.zjtc.model.vo.UseWaterUnitRefVo;
import com.zjtc.model.vo.UseWaterUnitVo;
import com.zjtc.service.BankService;
import com.zjtc.service.CommonService;
import com.zjtc.service.ContactsService;
import com.zjtc.service.FileService;
import com.zjtc.service.SystemLogService;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
  @Autowired
  SystemLogService systemLogService;
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

  /**
   * 上下文
   */
  @Value("${file.preViewRealPath}")
  private String preViewRealPath;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse save(UseWaterUnit entity, User user) {
    ApiResponse apiResponse = new ApiResponse();
    //当前用户类型，取unitCode
    String unitType = entity.getUnitCode().substring(4, 6);
    /**验证当前用户是否有操作当前类型的权限*/
    /**如果已有当前类型,则无权限，反之，有权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), entity.getUnitCode(), user.getNodeCode());
    List<String> unitTypeList = selectAllType(user.getNodeCode());
    if (!flag && unitTypeList.contains(unitType)) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该类型权限,请联系管理员!");
      apiResponse.setCode(501);
      return apiResponse;
    } else {
      //新增该用户类型权限
      List<String> strArr = new ArrayList<>();
      strArr.add(unitType);
      useWaterUnitRoleService.addUseWaterUnitRole(user.getId(), user.getNodeCode(), strArr);
    }
    /**验证单位编号是否重复,先查询出当前节点编码*/
    if (ValidateUnit(entity.getUnitCode(), user.getNodeCode(), entity.getId())) {
      apiResponse.recordError("当前排序号重复");
      apiResponse.setCode(501);
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
    UseWaterUnit param = entity;
    this.save(entity);
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
      this.unitRefCheck(entity.getId(), entity.getUseWaterUnitIdRef(), user.getNodeCode());
    }
    systemLogService.logInsert(user, "用水单位管理", "新增", null);
    return apiResponse;

  }

  /**
   * 关联相关编号这里做一个约定 新增、修改是选择的相关编号、选择主户与该单位都是被关联关系 例：单位A,关联单位B   B被A关联 A(unit_id),---> B（unit_id_ref）
   * 为满足树型结构，在新增或修改时都需要去判断被关联的B,是否是根节点 始终关联 被关联的节点的根节点
   *
   * @param useWaterUnitId 用水单位id
   * @param newId 要关联的id
   */
  private void unitRefCheck(String useWaterUnitId, String newId, String nodeCode) {
    String rootId = useWaterUnitRefService.selectRootNode(newId, nodeCode);
    if(StringUtils.isNotBlank(rootId)){
      if (newId ==rootId) {
        useWaterUnitRefService
            .save(useWaterUnitId, newId, nodeCode);
      }else{
        useWaterUnitRefService
            .save(rootId, newId, nodeCode);
        useWaterUnitRefService
            .save(useWaterUnitId, rootId, nodeCode);
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse update(UseWaterUnit entity, User user) {
    ApiResponse apiResponse = new ApiResponse();
    String unitType = entity.getUnitCode().substring(4, 6);
    /**验证当前用户是否有操作当前类型的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), entity.getUnitCode(), user.getNodeCode());
    List<String> unitTypeList = selectAllType(user.getNodeCode());
    if (!flag && unitTypeList.contains(unitType)) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该类型权限,请联系管理员！");
      apiResponse.setCode(501);
      return apiResponse;
    } else {
      //新增该用户类型权限
      List<String> strArr = new ArrayList<>();
      strArr.add(unitType);
      useWaterUnitRoleService.addUseWaterUnitRole(user.getId(), user.getNodeCode(), strArr);
    }
    /**验证单位编号是否重复,先查询出当前节点编码*/
    if (ValidateUnit(entity.getUnitCode(), user.getNodeCode(), entity.getId())) {
      apiResponse.recordError("当前排序号重复");
      apiResponse.setCode(501);
      /**生成新的排序号*/
      String maxCount = baseMapper
          .maxUnitCode(entity.getUnitCode(), entity.getId(), user.getNodeCode());
      maxCount = craeatRank(maxCount);
      apiResponse.setData(maxCount);
      return apiResponse;
    }
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
    String userWaterUnitId = entity.getImainUnitId(); //选择的主户
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
        this.unitRefCheck(entity.getId(), userWaterUnitId, user.getNodeCode());
      }
      systemLogService.logInsert(user, "用水单位管理", "修改", null);
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
        QueryWrapper sonQueryWrapper = new QueryWrapper();
        sonQueryWrapper.eq("node_code", user.getNodeCode());
        sonQueryWrapper.eq("use_water_unit_id", item.getId());
        List<UseWaterUnitRef> sonList = useWaterUnitRefService.list(sonQueryWrapper);
        List<String> sonIds = new ArrayList<>();
        //查询当前节点的上级节点
        QueryWrapper parQueryWrapper = new QueryWrapper();
        parQueryWrapper.eq("node_code", user.getNodeCode());
        parQueryWrapper.eq("use_water_unit_id_ref", item.getId());
        List<UseWaterUnitRef> parList = useWaterUnitRefService.list(parQueryWrapper);
        if (!parList.isEmpty() && !sonList.isEmpty()) {
          //当前节点的子节点跟当前节点的父节点关联
          for (UseWaterUnitRef ref : sonList) {
            ref.setUseWaterUnitId(parList.get(0).getUseWaterUnitId());
            sonIds.add(ref.getId());
          }
          //删除当前节点与父节点的关联
          useWaterUnitRefService.removeById(parList.get(0).getId());
          //当前节点的子节点与当前节点的父节点重新建立关联
          useWaterUnitRefService.updateBatchById(sonList);
        }
        if (parList.isEmpty() && !sonList.isEmpty()) {
          //如果是删除的根节点，其他节点建立管理关系,取第一子节点为父节点
          String firstId = sonList.get(0).getId();
          //删除第一条子节点的数据
          useWaterUnitRefService.removeById(firstId);
          //修改父节点为第一个子节点
          for (UseWaterUnitRef ref : sonList) {
            ref.setUseWaterUnitId(firstId);
          }
          useWaterUnitRefService.updateBatchById(sonList);
        }
        if (sonList.isEmpty() && !parList.isEmpty()) {
          useWaterUnitRefService.removeById(parList.get(0).getId());
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
              items.setNodeCode(user.getNodeCode());
              items.setBusinessId(id);
            }
          }
          fileService.saveBatch(file);
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
    QueryWrapper wrapper = new QueryWrapper();
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
      QueryWrapper sonQueryWrapper = new QueryWrapper();
      sonQueryWrapper.eq("node_code", user.getNodeCode());
      sonQueryWrapper.eq("use_water_unit_id", id);
      List<UseWaterUnitRef> sonList = useWaterUnitRefService.list(sonQueryWrapper);
      List<String> sonIds = new ArrayList<>();
      //查询当前节点的上级节点
      QueryWrapper parQueryWrapper = new QueryWrapper();
      parQueryWrapper.eq("node_code", user.getNodeCode());
      parQueryWrapper.eq("use_water_unit_id_ref", id);
      List<UseWaterUnitRef> parList = useWaterUnitRefService.list(parQueryWrapper);
      if (!parList.isEmpty() && !sonList.isEmpty()) {
        //当前节点的子节点跟当前节点的父节点关联
        for (UseWaterUnitRef ref : sonList) {
          ref.setUseWaterUnitId(parList.get(0).getUseWaterUnitId());
          sonIds.add(ref.getId());
        }
        //删除当前节点与父节点的关联
        useWaterUnitRefService.removeById(parList.get(0).getId());
        //当前节点的子节点与当前节点的父节点重新建立关联
        useWaterUnitRefService.updateBatchById(sonList);
      }
      if (parList.isEmpty() && !sonList.isEmpty()) {
        useWaterUnitRefService.deleteBatch(sonIds);
      }
      if (sonList.isEmpty() && !parList.isEmpty()) {
        useWaterUnitRefService.removeById(parList.get(0).getId());
      }
    }
    systemLogService.logInsert(user, "用水单位管理", "删除", null);
    return true;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    String nodeCode = jsonObject.getString("nodeCode");
    Map<String, Object> page = new LinkedHashMap<>();
    long start = System.currentTimeMillis();
    List<UseWaterUnitVo> result = baseMapper.queryPage(jsonObject);
    long end = System.currentTimeMillis() - start;
    System.out.println("=================>end" + end);
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
          //相关编号，用逗号隔开
          String useWaterUnitIdRef = "";
          if (!useWaterUnitRefList.isEmpty()) {
            for (UseWaterUnitRefVo useWaterUnitRefVo : useWaterUnitRefList) {
              useWaterUnitIdRef += useWaterUnitRefVo.getUnitCode() + ",";
            }
          }
          if (useWaterUnitIdRef.length() > 0) {
            item.setUseWaterUnitIdRef(
                useWaterUnitIdRef.substring(0, useWaterUnitIdRef.length() - 1));
          }
        }
        //查询所属区域
        item.setAreaCountryName(
            dictUtils.getDictItemNameCountry(AREA_COUNTRY_CODE, item.getAreaCountry(), nodeCode));
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
    long end2 = System.currentTimeMillis() - start;
    System.out.println("=================>end2:" + end2);
    return page;
  }

  @Override
  public UseWaterUnitVo selectById(JSONObject jsonObject, User user) {
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
    UseWaterUnitVo useWaterUnit = baseMapper.selectById(jsonObject);
    if (null != useWaterUnit) {
      /**查询相关编号信息*/
      List<String> idList = useWaterUnitRefService
          .findIdList(useWaterUnit.getId(), user.getNodeCode());
      if (!idList.isEmpty()) {
        //相关编号集合
        List<UseWaterUnitRefVo> useWaterUnitRefList = baseMapper
            .queryUnitRef(idList, user.getNodeCode(), jsonObject.getString("userId"),
                useWaterUnit.getId());
        useWaterUnit.setUseWaterUnitRefList(useWaterUnitRefList);
           for (UseWaterUnitRefVo useWaterUnitRefVo : useWaterUnitRefList) {
            if ("1".equals(useWaterUnitRefVo.getImain())) {
              useWaterUnit.setImainUnitId(useWaterUnitRefVo.getId());
            }
        }
      }
      //附件
      if (!useWaterUnit.getSysFile().isEmpty()) {
        for (File file : useWaterUnit.getSysFile()) {
          file.setUrl(preViewRealPath + file.getFilePath());
        }
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
    String unitType = unitCode.substring(4, 6);
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), unitCode, user.getNodeCode());
    List<String> unitTypeList = selectAllType(user.getNodeCode());
    if (!flag && unitTypeList.contains(unitType)) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该类型权限,请联系管理员!");
      apiResponse.setCode(501);
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
  public Map<String, Object> addUnitCodePage(JSONObject jsonObject, User user) {
    //查询当前节点编码下所有可操作类型的所有单位的单位编码、单位名称
    Map<String, Object> page = new LinkedHashMap<>();
    List<Map<String, Object>> result = baseMapper.addUnitCodePage(jsonObject);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //查询总数据条数
    long total = baseMapper.addUnitCodeTotal(jsonObject);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  public Map<String, Object> selectByUnitCode(String unitCode, User user) {
    return baseMapper.selectByUnitCode(unitCode, user.getNodeCode());
  }

  @Override
  public ApiResponse exportAccountAudit(User user, JSONObject jsonObject,
      HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    List<Map<String, Object>> list = baseMapper
        .exportAccountAudit(jsonObject.getString("nodeCode"));
    if (list.isEmpty()) {
      apiResponse.recordError("无导出数据！");
      apiResponse.setCode(501);
      return apiResponse;
    }
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "计划用水户账户审核表.xls";
    String templateName = "template/accountAudit.xls";
    commonService.export(fileName, templateName, request, response, data);
    systemLogService.logInsert(user, "用水单位管理", "导出账户审核表", null);
    return apiResponse;
  }

  @Override
  public ApiResponse exportForm(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    List<Map<String, Object>> list = baseMapper
        .exportForm(jsonObject.getString("nodeCode"));
    if (list.isEmpty()) {
      apiResponse.recordError("无导出数据！");
      apiResponse.setCode(501);
      return apiResponse;
    }
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "计划用水户开通格式.xls";
    String templateName = "template/form.xls";
    commonService.export(fileName, templateName, request, response, data);
    systemLogService.logInsert(user, "用水单位管理", "导出开通格式", null);
    return apiResponse;
  }

  @Override
  public ApiResponse exportRevoca(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    String nodeCode = jsonObject.getString("nodeCode");
    //开始时间
    String startTime = jsonObject.getString("startTime");
    //结束时间
    String endTime = jsonObject.getString("endTime");
    //开户行行名
    String sBankName = dictUtils.getDictItemNameCountry("receive_user_info", "1", nodeCode);
    jsonObject.put("sBankName", sBankName);
    //开户行号
    String sBankNum = dictUtils.getDictItemNameCountry("receive_user_info", "2", nodeCode);
    jsonObject.put("sBankNum", sBankNum);
    //户名
    String sUnitName = dictUtils.getDictItemNameCountry("receive_user_info", "3", nodeCode);
    jsonObject.put("sUnitName", sUnitName);
    //账号
    String sBankAccount = dictUtils.getDictItemNameCountry("receive_user_info", "4", nodeCode);
    jsonObject.put("sBankAccount", sBankAccount);
    if (StringUtils.isNotBlank(startTime)) {
      jsonObject.put("startTime", startTime + " 00:00:00");
    }
    if (StringUtils.isNotBlank(endTime)) {
      jsonObject.put("endTime", endTime + " 23:59:59");
    }
    List<Map<String, Object>> list = baseMapper
        .exportRevoca(jsonObject);
    if (list.isEmpty()) {
      apiResponse.recordError("您所选择的时间区间无数据！");
      apiResponse.setCode(501);
      return apiResponse;
    }
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "计划用水户撤销格式.xls";
    String templateName = "template/Revoca.xls";
    commonService.export(fileName, templateName, request, response, data);
    //导出的撤销账户标志
    bankService.updateIsExport(list);
    systemLogService.logInsert(user, "用水单位管理", "导出撤销格式", null);
    return apiResponse;
  }

  @Override
  public ApiResponse exportQueryData(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    List<Map<String, Object>> map = baseMapper.exportQueryData(jsonObject);
    if (map.isEmpty()) {
      apiResponse.recordError("无导出数据！");
      apiResponse.setCode(501);
      return apiResponse;
    }
    for (Map item : map) {
      //查询电话号码
      List<Contacts> contactsList = contactsService.queryByUnitId(item.get("id").toString());
      if (!contactsList.isEmpty()) {
        for (int i = 0; i < contactsList.size(); i++) {
          item.put("contacts" + (i + 1), contactsList.get(i).getContacts());
          item.put("mobileNumber" + (i + 1), contactsList.get(i).getMobileNumber());
          item.put("phoneNumber" + (i + 1), contactsList.get(i).getPhoneNumber());
        }
      }
      String areaCountry =
          null == item.get("areaCountry") ? null : item.get("areaCountry").toString();
      if (StringUtils.isNotBlank(areaCountry)) {
        //查询所属区域
        item.put("areaCountryName",
            dictUtils.getDictItemNameCountry(AREA_COUNTRY_CODE,
                areaCountry,
                user.getNodeCode()));
      }
      //查询相关编号
      List<String> idList = useWaterUnitRefService
          .findIdList(item.get("id").toString(), user.getNodeCode());
      if (!idList.isEmpty()) {
        //相关编号集合
        List<UseWaterUnitRefVo> useWaterUnitRefList = baseMapper
            .queryUnitRef(idList, user.getNodeCode(), user.getId(),
                item.get("id").toString());
        //相关编号，用逗号隔开
        String useWaterUnitIdRef = "";
        if (!useWaterUnitRefList.isEmpty()) {
          for (UseWaterUnitRefVo useWaterUnitRefVo : useWaterUnitRefList) {
            useWaterUnitIdRef += useWaterUnitRefVo.getUnitCode() + ",";
          }
        }
        if (useWaterUnitIdRef.length() > 0) {
          useWaterUnitIdRef = useWaterUnitIdRef.substring(0, useWaterUnitIdRef.length() - 1);
        }
        item.put("useWaterUnitIdRef", useWaterUnitIdRef);
      }

    }
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", map);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "用水户界面查询结果.xls";
    String templateName = "template/useWaterUnitData.xls";
    commonService.export(fileName, templateName, request, response, data);
    systemLogService.logInsert(user, "用水单位管理", "导出查询结果", null);
    return apiResponse;
  }

  @Override
  public ApiResponse exportMoreAndLess(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    List<Map<String, Object>> list = baseMapper
        .exportMoreAndLess(jsonObject.getString("nodeCode"), user.getId());
    if (list.isEmpty()) {
      apiResponse.recordError("无导出数据！");
      apiResponse.setCode(501);
      return apiResponse;
    }
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "用水单位增减情况表.xls";
    String templateName = "template/useWaterUnitMoreAndLess.xls";
    commonService.export(fileName, templateName, request, response, data);
    systemLogService.logInsert(user, "用水单位管理", "导出用水单位增减情况", null);
    return apiResponse;
  }

  @Override
  public List<Map<String, Object>> selectCodeByName(String userId, String nodeCode, String unitName,
      Double actualAmount) {
    return baseMapper.selectCodeByName(userId, nodeCode, unitName, actualAmount);
  }

  @Override
  public List<String> selectAllType(String nodeCode) {
    return baseMapper.selectAllType(nodeCode);
  }

  @Override
  public List<Map<String, Object>> selectUnitMap(JSONObject jsonObject) {
    return baseMapper.selectUnitMap(jsonObject);
  }

  @Override
  public Map<String, Object> selectUnitById(JSONObject jsonObject) {
    return baseMapper.selectUnitById(jsonObject);
  }

  @Override
  public List<Map<String, Object>> selectLeftData(JSONObject jsonObject) {
    return baseMapper.selectLeftData(jsonObject);
  }

  /**
   * 验证单位编号是否重复
   */
  private Boolean ValidateUnit(String unitCode, String nodeCode, String id) {
    QueryWrapper entityQueryWrapper = new QueryWrapper();
    entityQueryWrapper.eq("node_code", nodeCode);
    entityQueryWrapper.eq("unit_code", unitCode);
    entityQueryWrapper.eq("deleted", "0");
    if (StringUtils.isNotBlank(id)) {
      entityQueryWrapper.notIn("id", id);
    }
    return this.count(entityQueryWrapper) > 0;
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

//  @Override
//  public List<OrgTreeVO> selectUnitCode(String nodeCode, String condition,String userId) {
////    查询全部类型,相当于是顶级部门
//    List<OrgTreeVO> treeVOFather = this.baseMapper.selectUnitCode(nodeCode, condition,userId);
//    for (OrgTreeVO orgTreeVO : treeVOFather) {
////      查询部门
//      String type = orgTreeVO.getId();
//      List<OrgTreeVO> treeVOSon = baseMapper.selectByTypeUnitAll(type, condition,userId,nodeCode);
//      treeVOFather.addAll(treeVOSon);
//    }
//    List<String> codeNodeList = new ArrayList<>(10);
//    //      人员
//    for (OrgTreeVO treeVO : treeVOFather) {
//      if (StringUtils.isNotBlank(treeVO.getNodeCode())) {
//        codeNodeList.add(treeVO.getNodeCode());
//      }
//    }
//    List<String> list = this.removeDuplicationByHashSet(codeNodeList);
//    List<OrgTreeVO> orgTreePerson = contactsService.selectContacts(list, condition,userId,nodeCode);
//    treeVOFather.addAll(orgTreePerson);
//    return treeVOFather;
//  }

  @Override
  public List<AddressBook> selectUnitCode(String nodeCode, String condition, String userId) {
//    查询全部类型,相当于是顶级部门 orgTreeVOS
    LinkedList<AddressBook> orgTreeVOS = useWaterUnitRoleService.selectUnitRoles(userId, nodeCode);
//    LinkedList<AddressBook> grandFathers = removeModelByHashSet(orgTreeVOS);
    if (null != orgTreeVOS && orgTreeVOS.size() > 0) {
      //查询用水单位数据
      List<AddressBook> fathers = baseMapper.selectByTypeUnitAll(nodeCode, orgTreeVOS);
//      if(null != fathers && fathers.size()>0){
      //查询联系人
      LinkedList<AddressBook> sons = contactsService.selectContacts(nodeCode, orgTreeVOS);
      orgTreeVOS.addAll(fathers);
      orgTreeVOS.addAll(sons);
//      }
    }
    return orgTreeVOS;
  }


  /**
   * list去重
   */
  public static LinkedList<AddressBook> removeModelByHashSet(LinkedList<AddressBook> list) {
    HashSet set = new HashSet(list);
    //把List集合所有元素清空
    list.clear();
    //把HashSet对象添加至List集合
    list.addAll(set);
    return list;
  }

  /**
   * list去重
   */
  public static List<String> removeDuplicationByHashSet(List<String> list) {
    HashSet set = new HashSet(list);
    //把List集合所有元素清空
    list.clear();
    //把HashSet对象添加至List集合
    list.addAll(set);
    return list;
  }

  @Override
  public List<Map<String, Object>> addUnitCodeList(User user) {
    //查询当前节点编码下所有可操作批次的所有单位的单位编码、单位名称
    return baseMapper.addUnitCodeList(user.getNodeCode(), user.getId());
  }

  @Override
  public void refreshSaveUnitType(String unitCode) {
    baseMapper.refreshSaveUnitType(unitCode, new Date());
  }

  @Override
  public void refreshWaterBalance(String unitCode, Date lastTestTime) {
    baseMapper.refreshWaterBalance(unitCode, lastTestTime);
  }
}
