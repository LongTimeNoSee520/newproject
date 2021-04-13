package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.waterBiz.BusinessWorkAnalyseMapper;
import com.zjtc.mapper.waterBiz.IndustryUseWaterMapper;
import com.zjtc.mapper.waterBiz.UseWaterUnitMonitorMapper;
import com.zjtc.mapper.waterBiz.WaterConditionAnalyseMapper;
import com.zjtc.mapper.waterCountry.CountyIndustryUseWaterMapper;
import com.zjtc.mapper.waterCountry.CountyUseWaterUnitMapper;
import com.zjtc.mapper.waterCountry.ImportantMonitorMapper;
import com.zjtc.service.BigScreenService;
import com.zjtc.service.UseWaterUnitService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

  @Autowired
  private WaterConditionAnalyseMapper waterConditionAnalyseMapper;

  @Autowired
  private BusinessWorkAnalyseMapper businessWorkAnalyseMapper;

  @Override
  public List<Map<String, Object>> selectUnitMap(JSONObject jsonObject) {
    List<Map<String, Object>> list = new ArrayList<>();
    String nodeCode = jsonObject.getString("nodeCode");
    if (StringUtils.isBlank(nodeCode)) {
      //默认nodeCode
      nodeCode = cityNodeCode;
      jsonObject.put("nodeCode",nodeCode);
    }
    //截止到当前年
    Integer year=jsonObject.getInteger("year");
    if(null !=year){
      year+=1;
      jsonObject.put("year",year+"-01-01 00:00:00");
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
  public List<Map<String, Object>> selectLeftData(JSONObject jsonObject) {
    List<Map<String, Object>> list = new ArrayList<>();
    String nodeCode = jsonObject.getString("nodeCode");
    if (StringUtils.isBlank(nodeCode)) {
      //默认nodeCode
      nodeCode = cityNodeCode;
      jsonObject.put("nodeCode",nodeCode);
    }
    //截止到当前年
    Integer year=jsonObject.getInteger("year");
    if(null !=year){
      year+=1;
      jsonObject.put("createTime",year+"-01-01 00:00:00");
    }
    //节水中心
    if (cityNodeCode.equals(nodeCode)) {
      list = useWaterUnitService.selectLeftData(jsonObject);
    } else {
      //区县
      list = countyUseWaterUnitMapper.selectLeftData(jsonObject);
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

  @Override
  public ApiResponse selectWaterUseAnalyse(String nodeCode, Integer year) {
    ApiResponse response = new ApiResponse();
    List<Map<String, Object>> list = new ArrayList<>();
    Map<String, Object> realityMap = new HashMap<>();
    Map<String, Object> planMap = new HashMap<>();
    //    实际用水
    List<Double>  aDouble1 = waterConditionAnalyseMapper.realityWater(nodeCode, year);
    for (Double result : aDouble1){
      if (null == result){
        result = 0D;
      }
    }
    realityMap.put("name","实际用水");
    realityMap.put("data",aDouble1);

    //    计划用水
    List<Double>  aDouble = waterConditionAnalyseMapper.planWater(nodeCode, year);
    for (Double result : aDouble){
      if ( null == result){
        result = 0D;
      }
    }
    planMap.put("name","计划用水");
    planMap.put("data",aDouble);

    list.add(realityMap);
    list.add(planMap);
    response.setData(list);
    return response;
  }


  @Override
  public ApiResponse businessApply(String nodeCode, Integer year) {
    ApiResponse response = new ApiResponse();
//    业务申请
    Integer aDouble = businessWorkAnalyseMapper.businessApply(nodeCode, year);
    response.setData(aDouble);
    return response;
  }

  @Override
  public ApiResponse businessTransaction(String nodeCode, Integer year) {
    ApiResponse response = new ApiResponse();
    List<Integer> list = new LinkedList<>();
    Map<String, Object> map = new LinkedHashMap<>();
    Integer aDouble = businessWorkAnalyseMapper.businessApply(nodeCode, year);
    Integer businessTransaction = businessWorkAnalyseMapper.businessTransaction(nodeCode, year);
    Integer businessSceneSolve = businessWorkAnalyseMapper.businessSceneSolve(nodeCode, year);
    Integer businessPublicSolve =businessWorkAnalyseMapper.businessPublicSolve(nodeCode, year);
    Integer businessWXSolve =businessWorkAnalyseMapper.businessWXSolve(nodeCode, year);
    list.add(businessSceneSolve);
    list.add(businessPublicSolve);
    list.add(businessWXSolve);

//    业务申请
    map.put("apply",aDouble);
//    业务办理
    map.put("solve",businessTransaction);
//    业务办理各数据来源
    map.put("source",list);
    response.setData(map);
    return response;
  }

  @Override
  public ApiResponse dataSources(String nodeCode, Integer year) {
    ApiResponse response = new ApiResponse();
    List<Integer> scene = new LinkedList<>();
    List<Integer> publics = new LinkedList<>();
    List<Integer> wx = new LinkedList<>();
    Map<String, Object> map = new LinkedHashMap<>();

//    申请
    Integer businessSceneApply = businessWorkAnalyseMapper.businessSceneApply(nodeCode, year);
    Integer businessPublicApply =businessWorkAnalyseMapper.businessPublicApply(nodeCode, year);
    Integer businessWXApply =businessWorkAnalyseMapper.businessWXApply(nodeCode, year);

//    解决
    Integer businessSceneSolve = businessWorkAnalyseMapper.businessSceneSolve(nodeCode, year);
    Integer businessPublicSolve =businessWorkAnalyseMapper.businessPublicSolve(nodeCode, year);
    Integer businessWXSolve =businessWorkAnalyseMapper.businessWXSolve(nodeCode, year);

    scene.add(businessSceneApply);
    scene.add(businessSceneSolve);

    publics.add(businessPublicApply);
    publics.add(businessPublicSolve);

    wx.add(businessWXApply);
    wx.add(businessWXSolve);

    map.put("scene",scene);
    map.put("publics",publics);
    map.put("wx",wx);
    response.setData(map);
    return response;
  }
}
