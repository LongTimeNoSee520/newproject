package com.zjtc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.waterBiz.UseWaterUnitMeterMapper;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.service.UseWaterUnitMeterService;
import com.zjtc.service.WaterMonthUseDataService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 用水单位水表
 */
@Service
public class UseWaterUnitMeterServiceImpl extends
    ServiceImpl<UseWaterUnitMeterMapper, UseWaterUnitMeter> implements
    UseWaterUnitMeterService {

  @Autowired
  private WaterMonthUseDataService waterMonthUseDataService;

//  @Autowired
//  private WaterUseDataService waterUseDataService;


  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean insertUseWaterUnitMeter(List<UseWaterUnitMeter> useWaterUnitMeter,
      String useWaterUnitId, String nodeCode) {
    if (null == useWaterUnitMeter || StringUtils.isBlank(useWaterUnitId) || StringUtils
        .isBlank(nodeCode)) {
      return false;
    }
//    水使用量月数据
    WaterMonthUseData waterMonthUseData = new WaterMonthUseData();
    List<WaterMonthUseData> waterMonthUseDataList = new ArrayList<>();
    boolean b1 = false;
    //    遍历水表信息
    for (UseWaterUnitMeter useWaterUnitMeter1 : useWaterUnitMeter) {
//      单位id
      useWaterUnitMeter1.setUseWaterUnitId(useWaterUnitId);
      useWaterUnitMeter1.setNodeCode(nodeCode);
      useWaterUnitMeter1.setId("");
//      水表档案号
      String waterMeterCode = useWaterUnitMeter1.getWaterMeterCode();
      if (StringUtils.isBlank(waterMeterCode)) {
        return false;
      }
      try {
//      通过水表档案号查询对应数据的id
        String id = this.baseMapper.selectWaterMeterCodeMyId(waterMeterCode);
        if (StringUtils.isBlank(id)) {
          continue;
        }
        waterMonthUseData.setId(id);
////      同时更新月水使用量月数据数据
        waterMonthUseData.setNodeCode(nodeCode);
////      单位id
        waterMonthUseData.setUseWaterUnitId(useWaterUnitId);
      } catch (Exception e) {
        log.error("回填发票号为waterMeterCode异常");
      }
      waterMonthUseDataList.add(waterMonthUseData);
//      更新月使用量数据
      b1 = waterMonthUseDataService.updateBatchById(waterMonthUseDataList);
      if (!b1) {
        return false;
      }
    }
    boolean b = this.saveBatch(useWaterUnitMeter);
    return b && b1;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean deletedUseWaterUnitMeter(String id) {
    List<UseWaterUnitMeter> meters = this.baseMapper.selectUseWaterUnitMeter(id);
    List<String> ids = new ArrayList<>();
    List<String> monthUseDatas=new ArrayList<>();
    if(!meters.isEmpty()){
      for (UseWaterUnitMeter useWaterUnitMeter : meters) {
        ids.add(useWaterUnitMeter.getId());
        monthUseDatas.add(useWaterUnitMeter.getWaterMeterCode());
      }
    }
    if (ids.isEmpty()) {
      return false;
    }
//    删除关联表的数据
    int integer = this.baseMapper.deleteBatchIds(ids);
//    同时清空水使用量月数据表对应的部门id
    boolean b=false;
    if(!monthUseDatas.isEmpty()){
      QueryWrapper wrapper=new QueryWrapper();
      wrapper.in("water_meter_code",monthUseDatas);
      WaterMonthUseData param=new WaterMonthUseData();
      param.setUseWaterUnitId("");
      b=waterMonthUseDataService.update(param,wrapper);
    }
    //   boolean b = waterMonthUseDataService.deletedUnit(ids);
////    同时清空水使用量数据表对应的部门id
//    boolean b1 = waterUseDataService.deletedUnit(ids);
    return integer > 0 && b;
  }

  @Override
  public boolean deletedUseWaterUnitMeter(List<String> ids) {
    if (ids.isEmpty()) {
      return false;
    }
    for (String id : ids) {
      this.deletedUseWaterUnitMeter(id);
    }
    return true;


  }


  @Override
  public List<UseWaterUnitMeter> selectUseWaterUnitMeter(String useWaterUnitId, String nodeCode) {
    QueryWrapper<UseWaterUnitMeter> wrapper = new QueryWrapper<>();
    wrapper.eq("use_water_unit_id", useWaterUnitId);
    wrapper.eq("node_code", nodeCode);
    return this.baseMapper.selectList(wrapper);
  }

  @Override
  public ApiResponse selectUseWaterUnitMeterAll(List<String> waterMeterCodes,
      String nodeCode,String useWaterUnitId) {
    ApiResponse response = new ApiResponse();
    String unitName = null;
    if (waterMeterCodes.isEmpty()) {
      response.recordError("系统异常");
      return response;
    }
    for (String waterMeterCode : waterMeterCodes) {
      unitName = this.baseMapper.selectWaterMeterCodeWhetherOccupy(waterMeterCode);
      if (!StringUtils.isBlank(unitName)) {
        response.recordError(unitName + "正在使用档案号为" + waterMeterCode + "的水表");
        return response;
      }
    }
    List<WaterMonthUseData> waterMonthUseDataList = waterMonthUseDataService
        .selectWaterUseData(waterMeterCodes,useWaterUnitId);
    response.setData(waterMonthUseDataList);
    return response;
  }

  @Override
  public Map<String, String> getMeterMap(String nodeCode) {
    Map<String, String> result = new HashMap<>();
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("node_code", nodeCode);
    List<UseWaterUnitMeter> list = this.list(wrapper);
    if (!list.isEmpty()) {
      for (UseWaterUnitMeter item : list) {
        result.put(item.getWaterMeterCode(), item.getUseWaterUnitId());
      }
      return result;
    }
    return result;
  }
}
