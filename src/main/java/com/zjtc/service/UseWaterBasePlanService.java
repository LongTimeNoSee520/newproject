package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterBasePlan;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;

/**
 * @author lianghao
 * @date 2020/12/24
 */

public interface UseWaterBasePlanService extends IService<UseWaterBasePlan> {


  /**新增*/
  ApiResponse add(User user, UseWaterBasePlan useWaterBasePlan);

  /**修改*/
  ApiResponse edit(User user,UseWaterBasePlan useWaterBasePlan);

  /**查询可选年份列表*/
  List<Integer> queryYear(User user);

  /**删除*/
  boolean delete(List<String> ids);

  /**分页查询*/
  Map<String,Object> queryPage(User user, JSONObject jsonObject);
}
