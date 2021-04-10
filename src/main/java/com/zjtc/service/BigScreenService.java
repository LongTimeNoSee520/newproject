package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * @author yuyantian
 * @date 2021/4/9
 * @description
 */
public interface BigScreenService  {

  /**
   * 查询中间地图区域标点
   * @param jsonObject
   * @return
   */
  List<Map<String,Object>> selectUnitMap(JSONObject jsonObject);

  /**
   * 查询取水单位信息
   * @param jsonObject
   * @return
   */
  List<Map<String,Object>> selectUnitById(JSONObject jsonObject);

  /**统计各行业节约用水情况*/
  List<Map<String,Object>> queryList(JSONObject jsonObject);
  /**各行业用水排名数据*/
  Map<String,Object> queryRankData(JSONObject jsonObject);
  /**重点用水单位监控数据*/
  Map<String,Object> importantMonitorData(JSONObject jsonObject);
}
