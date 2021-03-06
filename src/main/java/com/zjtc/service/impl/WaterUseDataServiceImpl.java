package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.WaterUseDataMapper;
import com.zjtc.model.WaterUseData;
import com.zjtc.service.WaterUseDataService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Service
@Slf4j
public class WaterUseDataServiceImpl extends
    ServiceImpl<WaterUseDataMapper, WaterUseData> implements
    WaterUseDataService {


  @Override
  public boolean deletedUnit(String id) {
    if (StringUtils.isBlank(id)) {
      return false;
    }
    List<WaterUseData> waterUseData = this.baseMapper.selectWaterUseDataId(id);
    List<String> list = new ArrayList<>();
    for (WaterUseData waterUseData1 : waterUseData) {
      list.add(waterUseData1.getId());
    }
    if (list.isEmpty()){
      return false;
    }
    return this.baseMapper.deletedUnit(list);
  }

  @Override
  public boolean deletedUnit(List<String> ids) {
    if (ids.isEmpty()) {
      return false;
    }
    for (String id : ids) {
      this.deletedUnit(id);
    }
    return true;
  }

  @Override
  public List<Integer> queryYear(String nodeCode) {
    return  this.baseMapper.queryYear(nodeCode);
  }


  @Override
  public List<WaterUseData> selectWaterUseData(List<String> waterMeterCode) {
    return this.baseMapper.selectWaterUseData(waterMeterCode);
  }
}
