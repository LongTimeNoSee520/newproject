package com.zjtc.mapper.waterCountry;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yuyantian
 * @date 2021/4/9
 * @description
 */
@Mapper
public interface CountyUseWaterUnitMapper {
  /**
   * 查询中间地图区域标点
   * @param jsonObject
   * @return
   */
  List<Map<String,Object>> selectUnitMap(JSONObject jsonObject);
  /**
   *根据用水单位id查询用水户信息
   */
  Map<String, Object> selectUnitById(JSONObject jsonObject);

  /**数字大屏
   * 查询左侧管理户数、计划用水量、实际用水量
   *selectLeftData
   */
  List<Map<String, Object>> selectLeftData(JSONObject jsonObject);
}
