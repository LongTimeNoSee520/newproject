package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitMapper;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.service.UseWaterUnitService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Service
public class UseWaterUnitServiceImpl extends ServiceImpl<UseWaterUnitMapper, UseWaterUnit> implements
    UseWaterUnitService {

  @Override
  public boolean save(UseWaterUnit useWaterUnit) {
    return false;
  }

  @Override
  public boolean update(UseWaterUnit useWaterUnit) {
    return false;
  }

  @Override
  public boolean delete(String id) {
    return false;
  }

  @Override
  public List<Map<String, Object>> queryPage(JSONObject jsonObject) {
    return null;
  }

  @Override
  public List<UseWaterUnit> queryAll(JSONObject jsonObject) {
    return null;
  }

  @Override
  public List<Map<String, Object>> findUnitCode(JSONObject jsonObject) {
    return null;
  }
}
