package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.WaterMonthUseDataMapper;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.service.WaterMonthUseDataService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
    EntityWrapper<WaterMonthUseData> entityWrapper = new EntityWrapper<>();
    int byId = 0;
    entityWrapper.eq("use_water_unit_id", id);
    List<WaterMonthUseData> waterMonthUseData = this.baseMapper.selectList(entityWrapper);
    try {
      for (WaterMonthUseData waterMonthUseData1 : waterMonthUseData){
        byId = this.baseMapper.deleteById(waterMonthUseData1.getId());
      }
    } catch (Exception e) {
      log.error("删除单位时删除关联的水使用量月数据的部门id出错");
      e.printStackTrace();
    }
    if (byId > 0) {
      return true;
    } else {
      return false;
    }
  }
}
