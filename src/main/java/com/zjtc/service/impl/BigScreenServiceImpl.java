package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.mapper.waterCountry.CountyUseWaterUnitMapper;
import com.zjtc.service.BigScreenService;
import com.zjtc.service.UseWaterUnitService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2021/4/9
 * @description
 */
@Service
public class BigScreenServiceImpl implements BigScreenService {

  @Autowired
  private CountyUseWaterUnitMapper countyUseWaterUnitMapper;

  @Autowired
  private UseWaterUnitService useWaterUnitService;
  //节水中心nodeCode
  @Value("${city.nodeCode}")
  private String cityNodeCode;

  @Override
  public List<Map<String, Object>> selectUnitMap(JSONObject jsonObject) {
    List<Map<String, Object>> list = new ArrayList<>();
    String nodeCode = jsonObject.getString("nodeCode");
    if (StringUtils.isBlank(nodeCode)) {
      //默认nodeCode
      nodeCode = cityNodeCode;
      jsonObject.put("nodeCode",nodeCode);
    }
    //节水中心
    if (cityNodeCode.equals(nodeCode)) {
      list = useWaterUnitService.selectUnitMap(jsonObject);
    } else {
      //区县
      list = countyUseWaterUnitMapper.selectUnitMap(jsonObject);
    }
    return list;
  }

  @Override
  public List<Map<String, Object>> selectUnitById(JSONObject jsonObject) {
    List<Map<String, Object>> list = new ArrayList<>();
    String nodeCode = jsonObject.getString("nodeCode");
    if (StringUtils.isBlank(nodeCode)) {
      //默认nodeCode
      nodeCode = cityNodeCode;
      jsonObject.put("nodeCode",nodeCode);
    }
    //节水中心
    if (cityNodeCode.equals(nodeCode)) {
      list = useWaterUnitService.selectUnitById(jsonObject);
    } else {
      //区县
      list = countyUseWaterUnitMapper.selectUnitById(jsonObject);
    }
    return list;
  }

}
