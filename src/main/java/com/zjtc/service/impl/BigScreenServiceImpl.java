package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.mapper.waterBiz.IndustryUseWaterMapper;
import com.zjtc.mapper.waterBiz.UseWaterUnitMonitorMapper;
import com.zjtc.mapper.waterCountry.CountyIndustryUseWaterMapper;
import com.zjtc.mapper.waterCountry.CountyUseWaterUnitMapper;
import com.zjtc.mapper.waterCountry.ImportantMonitorMapper;
import com.zjtc.service.BigScreenService;
import com.zjtc.service.UseWaterUnitService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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
  private UseWaterUnitService useWaterUnitService;
  //节水中心nodeCode
  @Value("${city.nodeCode}")
  private String cityNodeCode;

  /**连接查询区县数据库数据*/
  @Autowired
  private CountyUseWaterUnitMapper countyUseWaterUnitMapper;
  @Autowired
  private CountyIndustryUseWaterMapper countyIndustryUseWaterMapper;
  @Autowired
  private ImportantMonitorMapper importantMonitorMapper;
  /**连接查询市数据库数据*/
  @Autowired
  private IndustryUseWaterMapper industryUseWaterMapper;
  @Autowired
  private UseWaterUnitMonitorMapper useWaterUnitMonitorMapper;
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
  @Override
  public List<Map<String, Object>> queryList(JSONObject jsonObject) {
    String  nodeCode =  jsonObject.getString("nodeCode");
    Integer year = jsonObject.getInteger("year");
    if (nodeCode.equals(cityNodeCode)){
      return industryUseWaterMapper.queryList(nodeCode,year);
    }else {
      return countyIndustryUseWaterMapper.queryList(nodeCode,year);
    }
  }

  @Override
  public Map<String, Object> queryRankData(JSONObject jsonObject) {
    String  nodeCode =  jsonObject.getString("nodeCode");
    Integer year = jsonObject.getInteger("year");
    String  id =  jsonObject.getString("id");//行业id
    Map<String,Object> result = new HashMap<>();
    List<Map<String,Object>> records =new ArrayList<>();
    if (nodeCode.equals(cityNodeCode)) {
      records = industryUseWaterMapper.queryRankData(nodeCode,year,id);
    }else {
      records= countyIndustryUseWaterMapper.queryRankData(nodeCode,year,id);
    }
    if (records.size() > 0){
      /**获取最大的用水量 向上百位取整*/
      int upNumber = this.upRoundHundred((double)records.get(0).get("useWaterNumber"));
      result.put("upNumber",upNumber);
      result.put("records",records);
    }
    return result;
  }

  @Override
  public Map<String, Object> importantMonitorData(JSONObject jsonObject) {
    String  nodeCode =  jsonObject.getString("nodeCode");
    Integer year = jsonObject.getInteger("year");
    Map<String,Object> result = new HashMap<>();
    if (nodeCode.equals(cityNodeCode)) {
      Map<String,Object> plan = useWaterUnitMonitorMapper.queryPlan(nodeCode,year);
      LinkedList<Double> real = useWaterUnitMonitorMapper.queryReal(nodeCode,year);
      LinkedList planList = this.mapToLinkedList(plan);
      result.put("plan",planList);
      result.put("real",real);
    }else {
      Map<String,Object> plan = importantMonitorMapper.queryPlan(nodeCode, year);
      LinkedList<Double> real = importantMonitorMapper.queryReal(nodeCode, year);
      LinkedList planList = this.mapToLinkedList(plan);
      result.put("plan",planList);
      result.put("real",real);
    }
    return result;
  }

  private LinkedList mapToLinkedList(Map<String, Object> map) {
    /**传入的map的key只能是包含为sql里的字段，所以sql查询的结果需包含以下key*/
    LinkedList list = new LinkedList();
    if (null != map) {
      list.add(0, null == map.get("one") ? 0 : map.get("one"));
      list.add(1, null == map.get("two") ? 0 : map.get("two"));
      list.add(2, null == map.get("three") ? 0 : map.get("three"));
      list.add(3, null == map.get("four") ? 0 : map.get("four"));
    }else {
      list.addAll(Arrays.asList(new Double[]{0d, 0d, 0d, 0d}));
    }
    return list;
  }

  /**
   * 向上百位取整
   * 34512 返回34600。
   * */
  private int upRoundHundred(double num){
    int upNumber;
    upNumber= (int) Math.ceil(num / 100);
    return upNumber*100;
  }
}
