package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.WaterMonthUseData;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 * 水使用量月数据
 */
public interface WaterMonthUseDataService extends IService<WaterMonthUseData> {


  /**
   * 删除单位时将水使用量月数据表相关的单位id对应的数据清空
   * @param id 部门id
   * @return 删除是否成功
   */
  boolean deletedUnit(String id);
}
