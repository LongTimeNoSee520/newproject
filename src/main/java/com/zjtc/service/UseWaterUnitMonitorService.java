package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterUnitMonitor;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;

/**
 * @author lianghao
 * @date 2021/01/15
 */
public interface UseWaterUnitMonitorService extends IService<UseWaterUnitMonitor> {

  /**
   * 分页查询
   * @param user，jsonObject
   * @return
   */
  Map<String, Object> queryPage(User user, JSONObject jsonObject);

  /**
   * 新增
   * @param user，monitor
   * @return
   */
  void add(User user, UseWaterUnitMonitor monitor);

  /**
   * 逻辑删除
   * @param ids
   * @return
   */
  void delete(List<String> ids);

  /**
   * 初始化下一年数据
   * @param user,monitorType
   * @return
   */
  void initNextYear(User user, String monitorType);
}
