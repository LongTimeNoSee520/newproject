package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterPlanAdd;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
public interface UseWaterPlanAddService extends IService<UseWaterPlanAdd> {

  /**
   * 修改备注
   * */
  void updateRemarks(String id, String remarks);
}
