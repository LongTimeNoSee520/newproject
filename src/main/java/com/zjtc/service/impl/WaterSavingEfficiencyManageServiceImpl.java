package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.FileUtil;
import com.zjtc.base.util.PoiUtil;
import com.zjtc.mapper.waterBiz.WaterSavingEfficiencyManageMapper;
import com.zjtc.model.User;
import com.zjtc.model.WaterSavingEfficiencyManage;
import com.zjtc.service.CommonService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.WaterSavingEfficiencyManageService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuyantian
 * @date 2021/3/25
 * @description
 */
@Service
public class WaterSavingEfficiencyManageServiceImpl extends
    ServiceImpl<WaterSavingEfficiencyManageMapper, WaterSavingEfficiencyManage> implements
    WaterSavingEfficiencyManageService {

  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;
  @Autowired
  private FileUtil fileUtil;
  @Autowired
  private CommonService commonService;
  @Autowired
  private SystemLogService systemLogService;
  @Autowired
  private PoiUtil poiUtil;

  @Override
  public boolean addOrUpdate(JSONObject jsonObject, User user) {
    String id=jsonObject.getString("id");
    List<WaterSavingEfficiencyManage> list = jsonObject.getJSONArray("manages")
        .toJavaList(WaterSavingEfficiencyManage.class);
    List<WaterSavingEfficiencyManage> updateList = new ArrayList<>();
    List<WaterSavingEfficiencyManage> deleteList = new ArrayList<>();
    boolean result=false;
    if (!list.isEmpty()) {
      for (WaterSavingEfficiencyManage item : list) {
        if ("1".equals(item.getDeleted())) {
          deleteList.add(item);
        } else if ("0".equals(item.getDeleted())) {
          item.setWaterBalanceTestId(id);
          updateList.add(item);
        }
      }
      if(!updateList.isEmpty()){
        //新增或修改
        result=saveOrUpdateBatch(updateList);
      }
      if(!deleteList.isEmpty()){
        //逻辑删除
        result= updateBatchById(deleteList);
      }
    }
    systemLogService.logInsert(user,"节水效率评估管理","编辑",null);
    return result;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    //查询总数据条数
    long total = baseMapper.queryListTotal(jsonObject);
    if (total <= 0) {
      return page;
    }
    List<Map<String, Object>> result = baseMapper.queryPage(jsonObject);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  public ApiResponse importExcel(MultipartFile file, User user, String bussiesId) throws Exception {
    ApiResponse response = new ApiResponse();
    List<WaterSavingEfficiencyManage> entity = new ArrayList<>();
    List<String[]> list = poiUtil.readExcel(file, 2);
    if (!list.isEmpty()) {
      for (String[] item : list) {
        WaterSavingEfficiencyManage manage = new WaterSavingEfficiencyManage();
        manage.setNodeCode(user.getNodeCode());
        manage.setDeleted("0");
        manage.setWaterBalanceTestId(bussiesId);
        manage.setType(item[1]);
        manage.setEvaluationIndex(item[2]);
        manage.setCalculationFormula(item[3]);
        manage.setActualValue(Double.parseDouble(item[4]));
        manage.setPCQuota(Double.parseDouble(item[5]));
        manage.setIndustryQuota(Double.parseDouble(item[6]));
        manage.setInterAdvancedValue(Double.parseDouble(item[7]));
        manage.setLevelAnalysis(item[8]);
        entity.add(manage);
      }
      this.saveBatch(entity);
    } else {
      response.recordError("无导入数据，请核对！");
    }
    systemLogService.logInsert(user,"节水效率评估管理","导入",null);
    return response;
  }


  @Override
  public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
    final String fileName = "节水效率指标评估模板.xlsx";
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
}
