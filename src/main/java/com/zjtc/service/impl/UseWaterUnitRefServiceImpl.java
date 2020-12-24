package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitRefMapper;
import com.zjtc.model.UseWaterUnitRef;
import com.zjtc.service.UseWaterUnitRefService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Service
public class UseWaterUnitRefServiceImpl extends
    ServiceImpl<UseWaterUnitRefMapper, UseWaterUnitRef> implements
    UseWaterUnitRefService {

  private List<String> sum;

  @Override
  public List<String> findIdList(String id) {
    sum = new ArrayList<>();
    sum.add(id);
    //查询当前单位id得所有关联单位id
    Wrapper entityWrapper = new EntityWrapper();
    entityWrapper.eq("use_water_unit_id", id);
    entityWrapper.setSqlSelect("use_water_unit_id_ref");
    List<String> result = this.selectList(entityWrapper);
    if (!result.isEmpty()) {
      //递归查询出所有关联单位id
      recursion(result);
      }
    //去重
    ArrayList<String> numbersList = new ArrayList<>(sum);
    List<String> ids = numbersList.stream().distinct().collect(Collectors.toList());
    return ids;
  }

  @Override
  public List<Map<String, Object>> findAll(String id) {
    /**先查出所有的单位id*/
   List<String> ids= findIdList(id);
   //排己
    ids.remove(id);
    /**查询相关编号信息*/
    /**todo:水表档案号显示多个？*/
    return null;
  }

  private void recursion(List<String> param) {
    //查询当前单位id得所有关联单位id
    List<String> refList = new ArrayList<>();
    Wrapper entityWrapper = new EntityWrapper();
    for (String item : param) {
      sum.add(item);
      entityWrapper.eq("use_water_unit_id", item);
      entityWrapper.setSqlSelect("use_water_unit_id_ref");
      List<String> result = this.selectList(entityWrapper);
      if (result.size() > 0 && null !=refList) {
        refList.addAll(result);
      }
    }
    if (refList.isEmpty()) {
      return;
    } else {
      recursion(refList);
    }
  }

}
