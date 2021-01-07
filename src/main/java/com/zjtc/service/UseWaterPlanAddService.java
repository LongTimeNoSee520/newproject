package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
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

  /**
   * 分页
   *
   * @param jsonObject 参数
   * @return 结果集
   */
  ApiResponse queryPage(JSONObject jsonObject, String nodeCode,String loginId);
}
