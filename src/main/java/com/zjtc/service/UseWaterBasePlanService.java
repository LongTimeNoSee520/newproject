package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterBasePlan;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
  boolean delete(List<String> ids, User user);

  /**分页查询*/
  Map<String,Object> queryPage(User user, JSONObject jsonObject);

  /**
   * 导出按条件查询后的基建计划数据
   * @param user
   * @param jsonObject
   */
  ApiResponse export(User user, JSONObject jsonObject,HttpServletRequest request,
      HttpServletResponse response);

  /**查询当前单位编码在当前nodeCode下、当前年份下是否已经有基建计划*/
  ApiResponse checkExisted(User user,String unitCode, Integer planYear);
}
