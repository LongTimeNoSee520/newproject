package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.WaterMonthUseDataMapper;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.service.WaterMonthUseDataService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 水使用量月数据
 */
@Service
@Slf4j
public class WaterMonthUseDataServiceImpl extends
    ServiceImpl<WaterMonthUseDataMapper, WaterMonthUseData> implements
    WaterMonthUseDataService {


  @Override
  public boolean deletedUnit(String id) {
    List<WaterMonthUseData> dataList = this.baseMapper.selectWaterMonthUseDataId(id);
    List<String> ids = new ArrayList<>();
    for (WaterMonthUseData waterMonthUseData : dataList){
      ids.add(waterMonthUseData.getId());
    }
    if (ids.isEmpty()){
      return false;
    }
    return this.baseMapper.updateWaterMonthUseData(ids);
  }

  @Override
  public boolean deletedUnit(List<String> ids) {
    if (ids.isEmpty()){
      return false;
    }
    for (String id : ids){
      this.deletedUnit(id);
    }
    return true;
  }

  @Override
  public List<WaterMonthUseData> selectWaterMonthUseData(String useWaterUnitId, String nodeCode) {
    if (StringUtils.isBlank(useWaterUnitId) || StringUtils.isBlank(nodeCode)) {
      log.error("查询当年水表信息传入参数错误,接口名为:selectWaterMonthUseData");
      return null;
    }
    return this.baseMapper.selectWaterMonthUseData(useWaterUnitId, nodeCode);
  }
}
