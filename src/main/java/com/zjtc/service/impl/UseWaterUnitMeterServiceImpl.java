package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterUnitMeterMapper;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.WaterUseData;
import com.zjtc.service.UseWaterUnitMeterService;
import com.zjtc.service.WaterMonthUseDataService;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.service.WaterUseDataService;
import java.util.ArrayList;
import java.util.List;
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

  @Autowired
  private WaterUseDataService waterUseDataService;


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
    //    判断水表档案号是否已被其他部门所关联
    for (UseWaterUnitMeter useWaterUnitMeter1 : useWaterUnitMeter) {
//      单位id
      useWaterUnitMeter1.setUseWaterUnitId(useWaterUnitId);
      useWaterUnitMeter1.setNodeCode(nodeCode);
//      水表档案号
      String waterMeterCode = useWaterUnitMeter1.getWaterMeterCode();
      if (StringUtils.isBlank(waterMeterCode)) {
        return false;
      }
//      同时新增水使用量月数据数据
      waterMonthUseData.setNodeCode(nodeCode);
//      单位id
      waterMonthUseData.setUseWaterUnitId(useWaterUnitId);
//      水表档案号
      waterMonthUseData.setWaterMeterCode(useWaterUnitMeter1.getWaterMeterCode());
      waterMonthUseDataList.add(waterMonthUseData);
    }
    boolean b = this.insertBatch(useWaterUnitMeter);
    boolean b1 = waterMonthUseDataService.insertBatch(waterMonthUseDataList);
    return b && b1;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean deletedUseWaterUnitMeter(String id) {
    List<UseWaterUnitMeter> meters = this.baseMapper.selectUseWaterUnitMeter(id);
    List<String> ids = new ArrayList<>();
    for (UseWaterUnitMeter useWaterUnitMeter : meters) {
      ids.add(useWaterUnitMeter.getId());
    }
    if (ids.isEmpty()) {
      return false;
    }
//    删除关联表的数据
    int integer = this.baseMapper.deleteBatchIds(ids);
//    同时清空水使用量月数据表对应的部门id
    boolean b = waterMonthUseDataService.deletedUnit(ids);
//    同时清空水使用量数据表对应的部门id
    boolean b1 = waterUseDataService.deletedUnit(ids);
    return integer > 0 && b && b1;
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
    EntityWrapper<UseWaterUnitMeter> wrapper = new EntityWrapper<>();
    wrapper.eq("use_water_unitId", useWaterUnitId);
    wrapper.eq("node_code", nodeCode);
    return this.baseMapper.selectList(wrapper);
  }

  @Override
  public ApiResponse selectUseWaterUnitMeterAll(List<String> waterMeterCodes,
      String nodeCode) {
    ApiResponse response = new ApiResponse();
    String unitName = null;
    if (waterMeterCodes.isEmpty()) {
      response.recordError("系统异常");
      return response;
    }
    for (String waterMeterCode : waterMeterCodes) {
      unitName = this.baseMapper.selectWaterMeterCodeWhetherOccupy(waterMeterCode);
      if (!StringUtils.isBlank(unitName)) {
        response.recordError(unitName + "正在使用档案号为"+waterMeterCode+"的水表");
        return response;
      }
    }
    List<WaterUseData> waterUseData = waterUseDataService.selectWaterUseData(waterMeterCodes);
    response.setData(waterUseData);
    return response;
  }
}
