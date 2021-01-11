package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterBasePlanMapper;
import com.zjtc.model.UseWaterBasePlan;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterBasePlanService;
import com.zjtc.service.UseWaterUnitRoleService;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lianghao
 * @date 2020/12/24
 */

@Service
public class UseWaterBasePlanServiceImpl extends
    ServiceImpl<UseWaterBasePlanMapper, UseWaterBasePlan> implements
    UseWaterBasePlanService {

  @Autowired
  private UseWaterUnitRoleService useWaterUnitRoleService;

  @Override
  @Transactional(rollbackFor = Exception.class)//多个表中修改数据时，一个出错全部回滚
  public ApiResponse add(User user, UseWaterBasePlan useWaterBasePlan) {
    ApiResponse response = new ApiResponse();
    /**查询当前登录人员是否有操作该类型的权限*/
    boolean result = useWaterUnitRoleService
        .checkUserRight(user.getId(), useWaterBasePlan.getUnitCode(), user.getNodeCode());
    if (!result) {
      response.recordError("当前登录用户没有操作当前类型的权限");
      return response;
    }
    /**查询当前单位编码在当前nodeCode下、当前年份下是否已经有基建计划*/
    int num = this.baseMapper.queryExistNum(useWaterBasePlan.getUnitCode(), user.getNodeCode(),
        useWaterBasePlan.getPlanYear());
    if (num > 0) {
      response.recordError("当前单位在该年份已有计划，无法再新增");
      return response;
    }
    useWaterBasePlan.setCreateTime(new Date());
    useWaterBasePlan.setDeleted("0");
    useWaterBasePlan.setNodeCode(user.getNodeCode());
    this.insert(useWaterBasePlan);
    /**TODO 调整该用水单位该年加价费算法*/

    return response;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)//多个表中修改数据时，一个出错全部回滚
  public ApiResponse edit(User user, UseWaterBasePlan useWaterBasePlan) {
    ApiResponse response = new ApiResponse();
    /**查询当前登录人员是否有操作该类型的权限*/
    boolean result = useWaterUnitRoleService
        .checkUserRight(user.getId(), useWaterBasePlan.getUnitCode(), user.getNodeCode());
    if (!result) {
      response.recordError("当前登录用户没有操作当前类型的权限");
      return response;
    }
    /**因为有可能修改了单位编号，所以需要查询当前单位编号是否已经有编制(排除自己),
     * 保证在同一单位编号下每年只有一个基建计划*/
    int others = this.baseMapper.queryOthers(useWaterBasePlan);
    if (others > 0 ){
      response.recordError("当前单位编号在该年份已有基建计划");
      return response;
    }else {
      this.baseMapper.updateById(useWaterBasePlan);
    }

    /**TODO 调整该用水单位该年加价费算法*/

    return response;
  }

  @Override
  public List<Integer> queryYear(User user) {
    return this.baseMapper.queryYear(user.getNodeCode(), user.getId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)//多个表中修改数据时，一个出错全部回滚
  public boolean delete(List<String> ids) {
    return this.baseMapper.delete(ids);
    /**TODO 调整该用水单位该年加价费算法*/
  }

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {
    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    String nodeCode = user.getNodeCode();
    String userId = user.getId();
    String unitCode = jsonObject.getString("unitCode");
    String unitName = jsonObject.getString("unitName");
    Integer planYear = jsonObject.getInteger("planYear");
    Map<String, Object> map = new HashMap();
    map.put("current", current);
    map.put("size", size);
    map.put("nodeCode", nodeCode);
    map.put("userId", userId);
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (null != planYear) {
      map.put("planYear", planYear);
    }

    /**查出满足条件的共有多少条*/
    int num = this.baseMapper.queryNum(map);
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("total", num);//满足条件的总条数
    result.put("size", size);//每页条数
    result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
    result.put("current", current);//当前页

    /**查出满足条件的数据*/
    List<UseWaterBasePlan> useWaterBasePlanList = this.baseMapper.queryPage(map);
    result.put("records", useWaterBasePlanList);
    return result;
  }

}
