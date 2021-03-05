package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Dict;
import java.util.Map;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
public interface DictService extends IService<Dict> {

  /**
   * 分页查询
   */
  Map<String,Object> queryPage(JSONObject jsonObject);
}
