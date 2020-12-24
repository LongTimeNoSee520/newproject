package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.UseWaterUnitRef;
import java.util.List;
import java.util.Map;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
public interface UseWaterUnitRefService extends IService<UseWaterUnitRef> {

  /**
   * 根据当前单位id查询出所有关联单位id
   * @param id
   * @return
   */
  List<String> findIdList(String id);
  /**
   * 根据当前单位id查询相关编号信息
   */
  List<Map<String,Object>> findAll(String id);
}

