package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterUnitMeter;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 * 水表信息
 */
public interface UseWaterUnitMeterService extends IService<UseWaterUnitMeter> {

  /**
   * 更新水表
   * @param useWaterUnitMeter 水表信息实体
   * @return 更新是否成功
   */
 boolean insertUseWaterUnitMeter(UseWaterUnitMeter useWaterUnitMeter);

}
