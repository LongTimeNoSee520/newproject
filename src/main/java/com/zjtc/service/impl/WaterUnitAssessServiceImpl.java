package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.WaterUnitAssessMapper;
import com.zjtc.model.vo.WaterUnitAssessVO;
import com.zjtc.service.WaterUnitAssessService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/18
 */
@Service
public class WaterUnitAssessServiceImpl implements WaterUnitAssessService {

  @Autowired
  private WaterUnitAssessMapper waterUnitAssessMapper;

  @Override
  public ApiResponse queryPage(JSONObject jsonObject, String nodeCode, String loginId) {
    ApiResponse response = new ApiResponse();
    Map<String, Object> map = new LinkedHashMap<>(10);
    if (StringUtils.isBlank(nodeCode) || StringUtils.isBlank(loginId)) {
      response.recordError("系统异常");
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
    Integer beginYear = null;
    if (null != jsonObject.getInteger("beginYear")) {
      beginYear = jsonObject.getInteger("beginYear");
    }
//    结束年份
    Integer endYear = null;
    if (null != jsonObject.getInteger("endYear")) {
      endYear = jsonObject.getInteger("endYear");
    }
//    总条数
    Integer total = waterUnitAssessMapper
        .selectCount(unitName, beginYear, endYear, nodeCode,  loginId);
//    总页数
    double pages = Math.ceil((double) total / pageSize);
//    数据集
    List<WaterUnitAssessVO> waterUnitAssessVOS = waterUnitAssessMapper
        .queryList(currPage, pageSize,unitName, beginYear, endYear, nodeCode,  loginId);
    map.put("total", total);
    map.put("size", pageSize);
    map.put("pages", (int) (pages));
    map.put("current", currPage);
    map.put("records", waterUnitAssessVOS);
    response.setCode(200);
    response.setData(map);
    return response;
  }
}
