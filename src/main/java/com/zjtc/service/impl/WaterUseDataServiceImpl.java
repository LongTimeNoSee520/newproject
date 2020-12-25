package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.WaterUseDataMapper;
import com.zjtc.model.WaterUseData;
import com.zjtc.service.WaterUseDataService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
  public boolean deletedUnit(List<String> id) {
    EntityWrapper<WaterUseData> entityWrapper = new EntityWrapper<>();
    entityWrapper.in("use_water_unit_id", id);
    List<WaterUseData> waterUseData = this.baseMapper.selectList(entityWrapper);
    List<WaterUseData> list = new ArrayList<>();
    WaterUseData waterUseData2 = new WaterUseData();
    boolean b = false;
    try {
      for (WaterUseData waterUseData1 : waterUseData) {
        waterUseData2.setId(waterUseData1.getId());
        waterUseData2.setUseWaterUnitId(null);
        list.add(waterUseData1);
        b = this.updateBatchById(list);
      }
    } catch (Exception e) {
      log.error("删除单位时删除关联的水使用量数据的部门id出错");
      e.printStackTrace();
    }
    if (b) {
      return true;
    } else {
      return false;
    }
  }
}
