package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitMeterMapper;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.service.UseWaterUnitMeterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Service
public class UseWaterUnitMeterServiceImpl extends
    ServiceImpl<UseWaterUnitMeterMapper, UseWaterUnitMeter> implements
    UseWaterUnitMeterService {


  @Override
  public boolean insertUseWaterUnitMeter(UseWaterUnitMeter useWaterUnitMeter) {
    if (null != useWaterUnitMeter) {
      return false;
    }
//    判断水表档案号是否已被其他部门所关联
    String waterMeterCode = useWaterUnitMeter.getWaterMeterCode();
    if (StringUtils.isBlank(waterMeterCode)) {
      return false;
    }
    int i = this.baseMapper.selectWaterMeterCodeWhetherOccupy(waterMeterCode);
    if (i > 0) {
      return false;
    }
    Integer result = this.baseMapper.insert(useWaterUnitMeter);
    if (null != result) {
      return true;
    } else {
      return false;
    }
  }
}
