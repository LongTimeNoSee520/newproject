package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitRefMapper;
import com.zjtc.model.UseWaterUnitRef;
import com.zjtc.service.UseWaterUnitRefService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
  public List<String> findIdList(String id, String nodeCode) {
    /**1.先根据当前单位id，查询出当前的根节点*/
    findPrent(id, nodeCode);
    sum = new ArrayList<>();
    sum.add(rootId);
    //根据根节点遍历树
    //查询当前单位id得所有关联单位id
    Wrapper entityWrapper = new EntityWrapper();
    entityWrapper.eq("use_water_unit_id", rootId);
    List<UseWaterUnitRef> useWaterUnitRefs = this.selectList(entityWrapper);
    List<String> result =new ArrayList<>();
    if (!useWaterUnitRefs.isEmpty()) {
      for(UseWaterUnitRef item:useWaterUnitRefs ){
        result.add(item.getUseWaterUnitIdRef());
      }
      //递归查询出所有关联单位id
      recursion(result);
    }
    //去重
//    ArrayList<String> numbersList = new ArrayList<>(sum);
//    List<String> ids = numbersList.stream().distinct().collect(Collectors.toList());
    return sum;
  }

  @Override
  public boolean save(String useWaterUnitId, String useWaterUnitIdRef, String nodeCode) {
    UseWaterUnitRef useWaterUnitRef = new UseWaterUnitRef();
    useWaterUnitRef.setNodeCode(nodeCode);
    useWaterUnitRef.setCreateTime(new Date());
    useWaterUnitRef.setUseWaterUnitId(useWaterUnitId);
    useWaterUnitRef.setUseWaterUnitIdRef(useWaterUnitIdRef);
    return this.insert(useWaterUnitRef);
  }

  @Override
  public boolean deleteBatch(List<String> ids) {
    return false;
  }

  private void recursion(List<String> param) {
    //查询当前单位id得所有关联单位id
    List<String> refList = new ArrayList<>();
    Wrapper entityWrapper = new EntityWrapper();
    for (String item : param) {
      sum.add(item);
      entityWrapper.eq("use_water_unit_id", item);
      List<UseWaterUnitRef> result = this.selectList(entityWrapper);
      if (result.size() > 0 && StringUtils.isNotBlank(result.get(0).getUseWaterUnitIdRef())) {
        refList.add(result.get(0).getUseWaterUnitIdRef());
      }
    }
    if (refList.isEmpty()) {
      return;
    } else {
      recursion(refList);
    }
  }

  private String rootId=null;
  /**
   * 逆向递归查询当前单位id在编号关联数据中的根节点
   */
  private void findPrent(String id, String nodeCode) {
    String param=null;
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("node_code", nodeCode);
    wrapper.eq("use_water_unit_id_ref", id);
    List<UseWaterUnitRef> result = this.selectList(wrapper);
    if (!result.isEmpty()) {
      findPrent(result.get(0).getUseWaterUnitId(), nodeCode);
    }else{
      rootId=id;
     return;
    }
  }

}
