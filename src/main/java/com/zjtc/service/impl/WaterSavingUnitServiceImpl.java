package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.FileUtil;
import com.zjtc.mapper.waterBiz.WaterSavingUnitMapper;
import com.zjtc.model.User;
import com.zjtc.model.WaterSavingUnit;
import com.zjtc.model.WaterSavingUnitBase;
import com.zjtc.model.WaterSavingUnitQuota;
import com.zjtc.model.vo.WaterSavingUnitBaseVo;
import com.zjtc.model.vo.WaterSavingUnitQuotaVo;
import com.zjtc.model.vo.WaterSavingUnitVo;
import com.zjtc.service.CommonService;
import com.zjtc.service.FileService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.UseWaterUnitService;
import com.zjtc.service.WaterSavingUnitBaseService;
import com.zjtc.service.WaterSavingUnitQuotaService;
import com.zjtc.service.WaterSavingUnitService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * WaterSavingUnit的服务接口的实现类
 *
 * @author
 */

@Service
public class WaterSavingUnitServiceImpl extends
    ServiceImpl<WaterSavingUnitMapper, WaterSavingUnit> implements
    WaterSavingUnitService {

  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;

  /**
   * 附件存储目录
   */
  @Value("${server.servlet-path}")
  private String contextPath;

  /**
   * 上下文
   */
  @Value("${file.preViewRealPath}")
  private String preViewRealPath;

  @Autowired
  private FileService fileService;
  @Autowired
  private FileUtil fileUtil;
  @Autowired
  private WaterSavingUnitQuotaService waterSavingUnitQuotaService;
  @Autowired
  private WaterSavingUnitBaseService waterSavingUnitBaseService;
  @Autowired
  private CommonService commonService;
  @Autowired
  private SystemLogService systemLogService;
  @Autowired
  private UseWaterUnitService useWaterUnitService;


  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse updateModel(JSONObject jsonObject, User user) {
    ApiResponse apiResponse = new ApiResponse();
    WaterSavingUnit entity = jsonObject.toJavaObject(WaterSavingUnit.class);
    //验证当前单位编号是否已经存在(排己)
    List<WaterSavingUnit> oldData = validateUnitCode(entity.getUnitCode(), user.getNodeCode(),
        entity.getId());
    if (!oldData.isEmpty()) {
      //删除之前的单位数据
      apiResponse.recordError("当前单位编号已存在");
      apiResponse.setCode(501);
      return apiResponse;
    }
    //修改主数据
    this.updateById(entity);
    //更新附件
    if (!entity.getSysFiles().isEmpty()) {
      fileService.updateBusinessId(entity.getId(), entity.getSysFiles());
    }
    if (!entity.getWaterSavingUnitQuotaList().isEmpty()) {
      //更新
      waterSavingUnitQuotaService
          .updateOrDelete(entity.getWaterSavingUnitQuotaList(), entity.getId());
    }
    if (!entity.getWaterSavingUnitBaseList().isEmpty()) {
      waterSavingUnitBaseService
          .updateOrDelete(entity.getWaterSavingUnitBaseList(), entity.getId());
    }
    systemLogService.logInsert(user,"节水型用水单位","修改",null);
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean deleteModel(String id, User user) {
    /**删除节水单位主数据*/
    WaterSavingUnit entity = new WaterSavingUnit();
    entity.setId(id);
    entity.setDeleted("1");
    boolean flag = this.updateById(entity);
    /**删除定量考核指标数据*/
    WaterSavingUnitQuota waterSavingUnitQuota = new WaterSavingUnitQuota();
    waterSavingUnitQuota.setDeleted("1");
    QueryWrapper wrapper1 = new QueryWrapper();
    wrapper1.eq("water_saving_unit_id", id);
    waterSavingUnitQuotaService.update(waterSavingUnitQuota, wrapper1);
    /**删除基础考核指标数据*/
    WaterSavingUnitBase waterSavingUnitBase = new WaterSavingUnitBase();
    waterSavingUnitBase.setDeleted("1");
    QueryWrapper wrapper2 = new QueryWrapper();
    wrapper2.eq("water_saving_unit_id", id);
    waterSavingUnitBaseService.update(waterSavingUnitBase, wrapper2);
    systemLogService.logInsert(user,"节水型用水单位","删除",null);
    return flag;
  }


  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    List<WaterSavingUnit> result = baseMapper.queryPage(jsonObject);
    if (!result.isEmpty()) {
      for (WaterSavingUnit waterSavingUnit : result) {
        if (!waterSavingUnit.getSysFiles().isEmpty()) {
          for (com.zjtc.model.File file : waterSavingUnit.getSysFiles()) {
            file.setUrl(preViewRealPath + file.getFilePath());
          }
        }
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

  /**
   * 导入
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse importExcel(MultipartFile file,
      User user)
      throws Exception {
    Map beans = new HashMap<String, List>();
    WaterSavingUnitVo certs = new WaterSavingUnitVo();
    List<WaterSavingUnitQuotaVo> waterSavingUnitQuotaVo = new ArrayList<>();
    List<WaterSavingUnitBaseVo> waterSavingUnitBaseVo = new ArrayList<>();
    beans.put("entity", certs);
    beans.put("info1", waterSavingUnitQuotaVo);
    beans.put("info2", waterSavingUnitBaseVo);
    String xmlConfig = "template/xml/waterSavingUnit.xml";
    Map result = commonService.importExcel(beans, file, xmlConfig, true);
    systemLogService.logInsert(user,"节水型用水单位","导入",null);
    return transform((WaterSavingUnitVo) result.get("entity"),
        (List<WaterSavingUnitQuotaVo>) result.get("info1"),
        (List<WaterSavingUnitBaseVo>) result.get("info2"), user);
  }

  /**
   * 将导入的对象转换成实体对象
   */
  private ApiResponse transform(WaterSavingUnitVo voList,
      List<WaterSavingUnitQuotaVo> waterSavingUnitQuotaVo,
      List<WaterSavingUnitBaseVo> waterSavingUnitBaseVo,
      User user) throws Exception {
    ApiResponse apiResponse = new ApiResponse();
    List<WaterSavingUnitQuota> quotaList = new ArrayList<>();
    List<WaterSavingUnitBase> baseList = new ArrayList<>();
    WaterSavingUnit result = new WaterSavingUnit();
    if (null != voList) {
      result.setNodeCode(user.getNodeCode());
      result.setUnitName(voList.getUnitName());
      result.setUnitCode(voList.getUnitCode());
      result.setAddress(voList.getAddress());
      result.setLegalRepresentative(voList.getLegalRepresentative());
      result.setCentralizedDepartment(voList.getCentralizedDepartment());
      result.setPhoneNumber(voList.getPhoneNumber());
      result.setZipCode(voList.getZipCode());
      result.setReviewTime(voList.getReviewTime());
      result.setReviewScore(voList.getReviewScore());
      result.setCreateTime(voList.getCreateTime());
      result.setCreateScore(voList.getCreateScore());
      result.setIndustrialAdded(voList.getIndustrialAdded());
      result.setTotalWaterQuantity(voList.getTotalWaterQuantity());
      result.setIndustrialAddedWater(voList.getIndustrialAddedWater());
      result.setReuseRate(voList.getReuseRate());
      result.setZbRate(voList.getZbRate());
      result.setLeakageRale(voList.getLeakageRale());
      result.setRemarks(voList.getRemarks());
      result.setDeleted("0");

      //验证该单位是否已经存在 存在：覆盖，不存在新增
      List<WaterSavingUnit> oldData = validateUnitCode(result.getUnitCode(), user.getNodeCode(),
          null);
      if (!oldData.isEmpty()) {
        //删除之前的单位数据
        for (WaterSavingUnit item : oldData) {
          deleteModel(item.getId(),user);
        }
      }
      this.save(result);
      //异步刷新用水单位信息 是否是节水单位
      if(StringUtils.isNotBlank(result.getUnitCode())){
        useWaterUnitService.refreshSaveUnitType(result.getUnitCode()) ;
      }
    } else {
      apiResponse.recordError("无导入数据，无法导入！");
      apiResponse.setCode(501);
    }
    if (!waterSavingUnitQuotaVo.isEmpty()) {
      for (WaterSavingUnitQuotaVo item : waterSavingUnitQuotaVo) {
        WaterSavingUnitQuota param = new WaterSavingUnitQuota();
        param.setWaterSavingUnitId(result.getId());
        param.setNodeCode(user.getNodeCode());
        param.setQuotaIndex(item.getQuotaIndex());
        param.setAssessAlgorithm(item.getAssessAlgorithm());
        param.setAssessStandard(item.getAssessStandard());
        param.setStandardLevel(item.getStandardLevel());
        param.setCompanyScore(item.getCompanyScore());
        param.setUnitScore(item.getUnitScore());
        param.setCheckScore(item.getCheckScore());
        param.setActualScore(item.getActualScore());
        param.setDeleted("0");
        quotaList.add(param);
      }
      //新增定量考核数据
      waterSavingUnitQuotaService.saveBatch(quotaList);
    }
    if (!waterSavingUnitBaseVo.isEmpty()) {
      for (WaterSavingUnitBaseVo item : waterSavingUnitBaseVo) {
        WaterSavingUnitBase param = new WaterSavingUnitBase();
        param.setWaterSavingUnitId(result.getId());
        param.setNodeCode(user.getNodeCode());
        param.setContents(item.getContents());
        param.setAssessMethod(item.getAssessMethod());
        param.setAssessStandard(item.getAssessStandard());
        param.setCompanyScore(item.getCompanyScore());
        param.setUnitScore(item.getUnitScore());
        param.setCheckScore(item.getCheckScore());
        param.setActualScore(item.getActualScore());
        param.setDeleted("0");
        baseList.add(param);
      }
      //新增定量考核数据
      waterSavingUnitBaseService.saveBatch(baseList);
    }
    return apiResponse;
  }

  /**
   * 下载模板
   */
  @Override
  public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
    final String fileName = "节水型单位管理导入模板.xls";
    String saveFilePath =
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName;
    String templatePath = "template/" + fileName;
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream(templatePath);
    File file = new File(saveFilePath);
    try {
      FileUtils.copyInputStreamToFile(inputStream, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    fileUtil.downloadTemplate(file, templatePath, fileName, request, response);
    file.delete();
  }

  private List<WaterSavingUnit> validateUnitCode(String unitCode, String nodeCode, String id) {
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("unit_code", unitCode);
    wrapper.eq("node_code", nodeCode);
    wrapper.eq("deleted", "0");
    if (StringUtils.isNotBlank(id)) {
      wrapper.notIn("id", id);
    }
    return this.list(wrapper);
  }

}