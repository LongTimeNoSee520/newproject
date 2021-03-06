package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.CommonUtil;
import com.zjtc.base.util.JxlsUtils;
import com.zjtc.mapper.waterBiz.WaterUnitAssessMapper;
import com.zjtc.model.User;
import com.zjtc.model.vo.WaterUnitAssessVO;
import com.zjtc.service.WaterUnitAssessService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/18
 */
@Service
@Slf4j
public class WaterUnitAssessServiceImpl implements WaterUnitAssessService {

  @Autowired
  private WaterUnitAssessMapper waterUnitAssessMapper;

  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;

  @Value("${file.fileUploadPath}")
  private String fileUploadPath;

  @Override
  public ApiResponse queryPage(JSONObject jsonObject, String nodeCode, String loginId) {
    ApiResponse response = new ApiResponse();
    Map<String, Object> map = new LinkedHashMap<>(10);

    if (StringUtils.isBlank(nodeCode) || StringUtils.isBlank(loginId)) {
      response.recordError("系统异常");
      return response;
    }
    if (null == jsonObject.getInteger("current") || null == jsonObject.getInteger("size")) {
      response.recordError("分页查询条数和页数不能为空");
      return response;
    }
//    页数
    Integer currPage = jsonObject.getInteger("current");
//    条数
    Integer pageSize = jsonObject.getInteger("size");
//    单位名称
    String unitName = "";
    if (null != jsonObject.getString("unitName")) {
      unitName = jsonObject.getString("unitName");
    }
//    开始年份
    Integer accessYear = null;
    if (null != jsonObject.getInteger("accessYear")) {
      accessYear = jsonObject.getInteger("accessYear");
    }
//    总条数
    Integer total = waterUnitAssessMapper
        .selectCount(unitName, accessYear, nodeCode, loginId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<WaterUnitAssessVO> waterUnitAssessVOS = waterUnitAssessMapper
        .queryList(currPage, pageSize, unitName, accessYear, nodeCode, loginId);
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", waterUnitAssessVOS);
    response.setCode(200);
    response.setData(map);
    return response;
  }

  @Override
  public void export(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response, User user) {
//    单位名称
    String unitName = "";
    if (null != jsonObject.getString("unitName")) {
      unitName = jsonObject.getString("unitName");
    }
//    年份
    Integer accessYear = null;
    if (null != jsonObject.getInteger("accessYear")) {
      accessYear = jsonObject.getInteger("accessYear");
    }
////    结束年份
//    Integer endYear = null;
//    if (null != jsonObject.getInteger("endYear")) {
//      endYear = jsonObject.getInteger("endYear");
//    }
    Integer integer = this.waterUnitAssessMapper
        .selectCount(unitName, accessYear, user.getNodeCode(), user.getId());
    List<WaterUnitAssessVO> waterUnitAssess = this.waterUnitAssessMapper
        .queryList(1, integer + 1, unitName, accessYear, user.getNodeCode(), user.getId()
            );
    Map<String, Object> data = new HashMap<>(16);
    data.put("waterUnitAssess", waterUnitAssess);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    try {
      String fileName = "计划户用水单位考核表.xlsx";
      String saveFilePath =
          fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName;
      InputStream inputStream = getClass().getClassLoader()
          .getResourceAsStream("template/PlanUserExamInfo.xlsx");
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
      log.error("用水单位考核导出异常:" + e.getMessage());
    }
  }
}
