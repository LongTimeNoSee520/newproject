package com.zjtc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.UseWaterUnitRefMapper;
import com.zjtc.model.UseWaterUnitRef;
import com.zjtc.service.UseWaterUnitRefService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    QueryWrapper entityQueryWrapper = new QueryWrapper();
    entityQueryWrapper.eq("use_water_unit_id", rootId);
    List<UseWaterUnitRef> useWaterUnitRefs = this.list(entityQueryWrapper);
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
    return this.save(useWaterUnitRef);
  }

  @Override
  public boolean deleteBatch(List<String> ids) {
    return this.removeByIds(ids);
  }

  private void recursion(List<String> param) {
    //查询当前单位id得所有关联单位id
    List<String> refList = new ArrayList<>();
    QueryWrapper entityQueryWrapper = new QueryWrapper();
    for (String item : param) {
      sum.add(item);
      entityQueryWrapper.eq("use_water_unit_id", item);
      List<UseWaterUnitRef> result = this.list(entityQueryWrapper);
      if (result.size() > 0 ) {
        for(UseWaterUnitRef items:result)
        refList.add(items.getUseWaterUnitIdRef());
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
    QueryWrapper wrapper = new QueryWrapper();
    wrapper.eq("node_code", nodeCode);
    wrapper.eq("use_water_unit_id_ref", id);
    List<UseWaterUnitRef> result = this.list(wrapper);
    if (!result.isEmpty()) {
      findPrent(result.get(0).getUseWaterUnitId(), nodeCode);
    }else{
      rootId=id;
     return;
    }
  }

}
