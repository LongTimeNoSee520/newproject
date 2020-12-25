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
  public boolean deletedUnit(List<String> id) {
    EntityWrapper<WaterMonthUseData> entityWrapper = new EntityWrapper<>();
    entityWrapper.in("use_water_unit_id", id);
    List<WaterMonthUseData> waterMonthUseData = this.baseMapper.selectList(entityWrapper);
    List<WaterMonthUseData> list = new ArrayList<>();
    int b = 0;
    try {
      for (WaterMonthUseData waterMonthUseData1 : waterMonthUseData) {
        waterMonthUseData1.setUseWaterUnitId(null);
        b = this.baseMapper.updateById(waterMonthUseData1);
      }
    } catch (Exception e) {
      log.error("删除单位时删除关联的水使用量月数据的部门id出错");
      e.printStackTrace();
    }
    if (b > 0) {
      return true;
    } else {
      return false;
    }
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
