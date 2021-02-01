package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.util.TimeUtil;
import com.zjtc.mapper.WaterMonthUseDataMapper;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.model.WaterUseData;
import com.zjtc.service.CommonService;
import com.zjtc.service.WaterMonthUseDataService;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 水使用量月数据
 */
@Service
@Slf4j
public class WaterMonthUseDataServiceImpl extends
    ServiceImpl<WaterMonthUseDataMapper, WaterMonthUseData> implements
    WaterMonthUseDataService {
  @Autowired
  private CommonService commonService;


  @Override
  public boolean deletedUnit(String id) {
    List<WaterMonthUseData> dataList = this.baseMapper.selectWaterMonthUseDataId(id);
    List<String> ids = new ArrayList<>();
    for (WaterMonthUseData waterMonthUseData : dataList){
      ids.add(waterMonthUseData.getId());
    }
    if (ids.isEmpty()){
      return false;
    }
    return this.baseMapper.updateWaterMonthUseData(ids);
  }

  @Override
  public boolean deletedUnit(List<String> ids) {
    if (ids.isEmpty()){
      return false;
    }
    for (String id : ids){
      this.deletedUnit(id);
    }
    return true;
  }

  @Override
  public List<WaterMonthUseData> selectWaterMonthUseData(String useWaterUnitId, String nodeCode) {
    if (StringUtils.isBlank(useWaterUnitId) || StringUtils.isBlank(nodeCode)) {
      log.error("查询当年水表信息传入参数错误,接口名为:selectWaterMonthUseData");
      return null;
    }
    return this.baseMapper.selectWaterMonthUseData(useWaterUnitId, nodeCode);
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();

    if(StringUtils.isBlank(jsonObject.getString("useYear"))){
      Calendar calendar = Calendar.getInstance();
      jsonObject.put("useYear",calendar.get(Calendar.YEAR));
    }
    List<WaterMonthUseData> result = baseMapper.queryPage(jsonObject);
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
  public List<WaterMonthUseData> selectWaterUseData(List<String> waterMeterCode) {
    EntityWrapper<WaterMonthUseData> wrapper = new EntityWrapper<>();
    wrapper.in("water_meter_code",waterMeterCode);
    return this.selectList(wrapper);
  }

  @Override
  public void export(JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    List<WaterMonthUseData> list = baseMapper.export(jsonObject);
    Map<String, Object> data = new HashMap<>();
    data.put("excelData", list);
    data.put("nowDate", new Date());
    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年MM月dd日");
    data.put("dateFormat", dateFmt);
    String fileName = "用水户水量查询结果.xlsx";
    String templateName = "template/waterMonthUseData.xlsx";
    commonService.export(fileName, templateName, request, response, data);
  }
}
