package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterUnit;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Service
public interface UseWaterUnitService extends IService<UseWaterUnit> {
  /**
   *新增
   */
  boolean save(UseWaterUnit useWaterUnit);
  /**
   * 修改
   */
  boolean update(UseWaterUnit useWaterUnit);
  /**
   * 删除
   */
  boolean delete(String id);
  /**
   *分页
   */
  List<Map<String,Object>> queryPage(JSONObject jsonObject);
  /**
   * 根据单位id查询详情
   */
  List<UseWaterUnit> queryAll(JSONObject jsonObject);

  /**
   * 新增用水单位时，查询相关编号
   */
  List<Map<String,Object>> findUnitCode(JSONObject jsonObject);
}
