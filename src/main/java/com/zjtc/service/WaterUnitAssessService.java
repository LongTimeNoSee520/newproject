package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/18
 */
public interface WaterUnitAssessService {

  /**
   * 分页
   * @param jsonObject 参数
   * @param nodeCode 节点编码
   * @param loginId 登录者id
   * @return 响应结果
   */
  ApiResponse queryPage(JSONObject jsonObject, String nodeCode,String loginId);
}
