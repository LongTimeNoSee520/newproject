package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.PayInfoPrintMapper;
import com.zjtc.model.PayInfoPrint;
import com.zjtc.model.User;
import com.zjtc.service.PayInfoPrintService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2021/5/17
 * @description
 */
@Service
public class PayInfoPrintServiceImpl extends
    ServiceImpl<PayInfoPrintMapper, PayInfoPrint> implements
    PayInfoPrintService {

  @Override
  public boolean deleteBatch(JSONObject jsonObject) {
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (!ids.isEmpty()) {
      return this.removeByIds(ids);
    }
    return false;
  }

  @Override
  public Map<String,Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();

    List<Map<String,Object>> result = baseMapper.queryPage(jsonObject);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //查询总数据条数
    long total = baseMapper.queryListTotal(jsonObject);
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  public PayInfoPrint selectPrintMess(String payId) {
    PayInfoPrint result = baseMapper.selectPrintMess(payId);
    return result;
  }

  @Override
  public String createPrintNum(String unitCode, String countYear, String countQuarter) {
    //生成规则：单位编号+年+季度+编号（3位）
    String printNum = unitCode + countYear + countQuarter + "001";
    return printNum;
  }

  @Override
  public boolean updatePrinted(List<String> list, User user) {
    return baseMapper.updatePrinted(list, user);
  }


}
