package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.base.util.RedisUtil;
import com.zjtc.mapper.waterSys.DictMapper;
import com.zjtc.model.Dict;
import com.zjtc.service.DictService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

  @Autowired
  private RedisUtil redisUtil;
  @Autowired
  private JWTUtil jwtUtil;

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    List<Map<String, Object>> result = baseMapper.queryPage(jsonObject);
    page.put("records", result);
    return page;
  }

}
