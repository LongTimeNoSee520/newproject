package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.QuotaInfo;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;

/**
 * @author lianghao
 * @date 2020/12/23
 */

public interface QuotaInfoService extends IService<QuotaInfo> {


  /**
   * 定额信息新增
   */
  boolean add(User user, QuotaInfo quotaInfo);

  /**
   * 定额信息修改
   */
  boolean edit(QuotaInfo quotaInfo);

  /**
   * 定额信息删除
   */
  boolean delete(List<String> ids);

  /**
   * 定额信息树关键词查询
   */
  List<QuotaInfo> queryTree(String keyword);

  /**
   * 一级行业信息查询
   * */
  List<Map<String,Object>> queryIndustry(User user,JSONObject jsonObject);
}
