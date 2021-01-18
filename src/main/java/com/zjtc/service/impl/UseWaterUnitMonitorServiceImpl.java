package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitMonitorMapper;
import com.zjtc.model.UseWaterUnitMonitor;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitMonitorService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * @author lianghao
 * @date 2021/01/15
 */
@Service
public class UseWaterUnitMonitorServiceImpl extends
    ServiceImpl<UseWaterUnitMonitorMapper, UseWaterUnitMonitor> implements
    UseWaterUnitMonitorService {


  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {
    Map<String, Object> result = new HashMap<>();
    Integer size = jsonObject.getInteger("size");
    Integer current = jsonObject.getInteger("current");
    String nodeCode = user.getNodeCode();
    String userId = user.getId();
    String monitorType = jsonObject.getString("monitorType");
    String unitName = jsonObject.getString("unitName");
    String unitCode = jsonObject.getString("unitCode");
    String unitCodeType = jsonObject.getString("unitCodeType");//用户类型
    Integer year = jsonObject.getInteger("year");
    String industryId = jsonObject.getString("industryId");
    Map<String, Object> map = new HashMap<>();
    map.put("size", size);
    map.put("current", current);
    map.put("monitorType", monitorType);
    map.put("nodeCode", nodeCode);
    map.put("userId", userId);
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitCodeType)) {
      map.put("unitCodeType", unitCodeType);
    }
    if (null != year) {
      map.put("year", year);
    }
    if (StringUtils.isNotBlank(industryId)) {
      map.put("industryId", industryId);
    }
    /**查出满足条件的共有多少条*/
    int num = this.baseMapper.queryNum(map);
    result.put("total", num);//满足条件的总条数
    result.put("size", size);//每页条数
    result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
    result.put("current", current);//当前页

    List<Map<String, Object>> records = this.baseMapper.queryPage(map);
    result.put("records", records);
    return result;
  }

  @Override
  public void add(User user, UseWaterUnitMonitor monitor) {
    monitor.setCreatePersonId(user.getId());
    monitor.setCreateTime(new Date());
    monitor.setDeleted("0");//未删除状态
    monitor.setNodeCode(user.getNodeCode());
    this.baseMapper.insert(monitor);
  }

  @Override
  public void delete(List<String> ids) {
   this.baseMapper.updateDeleted(ids);
  }

  @Override
  public void initNextYear(User user, String monitorType) {
    String nodeCode = user.getNodeCode();
    String userId = user.getId();
    this.baseMapper.initNextYear(userId,nodeCode,monitorType);
  }


}