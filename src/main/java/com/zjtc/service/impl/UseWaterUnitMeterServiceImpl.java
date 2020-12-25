package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitMeterMapper;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.service.UseWaterUnitMeterService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 用水单位水表
 */
@Service
public class UseWaterUnitMeterServiceImpl extends
    ServiceImpl<UseWaterUnitMeterMapper, UseWaterUnitMeter> implements
    UseWaterUnitMeterService {

  @Override
  public boolean insertUseWaterUnitMeter(List<UseWaterUnitMeter> useWaterUnitMeter,
      String useWaterUnitId, String nodeCode) {
    if (null == useWaterUnitMeter || StringUtils.isBlank(useWaterUnitId) || StringUtils
        .isBlank(nodeCode)) {
      return false;
    }
    //    判断水表档案号是否已被其他部门所关联
    for (UseWaterUnitMeter useWaterUnitMeter1 : useWaterUnitMeter) {
      useWaterUnitMeter1.setUseWaterUnitId(useWaterUnitId);
      useWaterUnitMeter1.setNodeCode(nodeCode);
      String waterMeterCode = useWaterUnitMeter1.getWaterMeterCode();
      if (StringUtils.isBlank(waterMeterCode)) {
        return false;
      }
      int i = this.baseMapper.selectWaterMeterCodeWhetherOccupy(waterMeterCode);
//    大于0表示已经被使用
      if (i > 0) {
        return false;
      }
    }
    return this.insertBatch(useWaterUnitMeter);

  }

  @Override
  public boolean deletedUseWaterUnitMeter(String id) {
    List<UseWaterUnitMeter> meters = this.baseMapper.selectUseWaterUnitMeter(id);
    List<String> ids = new ArrayList<>();
    for (UseWaterUnitMeter useWaterUnitMeter : meters) {
      ids.add(useWaterUnitMeter.getId());
    }
    int integer = this.baseMapper.deleteBatchIds(ids);
    if (integer > 0){
      return true;
    }else{
      return false;
    }
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
}
