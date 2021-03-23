package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.WaterSavingUnitQuotaMapper;
import com.zjtc.model.WaterSavingUnitQuota;
import com.zjtc.service.WaterSavingUnitQuotaService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * WaterSavingUnitQuota的服务接口的实现类
 *
 * @author
 */
@Service
public class WaterSavingUnitQuotaServiceImpl extends
    ServiceImpl<WaterSavingUnitQuotaMapper, WaterSavingUnitQuota> implements
    WaterSavingUnitQuotaService {

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    WaterSavingUnitQuota entity = jsonObject.toJavaObject(WaterSavingUnitQuota.class);
    boolean result = this.insert(entity);
    return result;
  }

  @Override
  public boolean updateModel(JSONObject jsonObject) {
    WaterSavingUnitQuota entity = jsonObject.toJavaObject(WaterSavingUnitQuota.class);
    boolean result = this.updateById(entity);
    return result;
  }

  @Override
  public boolean deleteModel(JSONObject jsonObject) {
    WaterSavingUnitQuota entity = jsonObject.toJavaObject(WaterSavingUnitQuota.class);
    boolean result = this.deleteById(entity);
    return result;
  }

  @Override
  public Page<WaterSavingUnitQuota> queryPage(JSONObject jsonObject) {
    WaterSavingUnitQuota entity = jsonObject.toJavaObject(WaterSavingUnitQuota.class);
    Page<WaterSavingUnitQuota> page = new Page<WaterSavingUnitQuota>(
        jsonObject.getInteger("current"), jsonObject.getInteger("size"));
    page.setSearchCount(true);
    page.setOptimizeCountSql(true);
    EntityWrapper<WaterSavingUnitQuota> eWrapper = new EntityWrapper<WaterSavingUnitQuota>(entity);
    Page<WaterSavingUnitQuota> result = this.selectPage(page, eWrapper);
    return result;
  }

  @Override
  public boolean updateOrDelete(List<WaterSavingUnitQuota> list, String savingId) {
    List<WaterSavingUnitQuota> updateList = new ArrayList<>();
    List<WaterSavingUnitQuota> deleteList = new ArrayList<>();
    boolean result=false;
    if (!list.isEmpty()) {
      for (WaterSavingUnitQuota item : list) {
        if ("1".equals(item.getDeleted())) {
          deleteList.add(item);
        } else if ("0".equals(item.getDeleted())) {
          item.setWaterSavingUnitId(savingId);
          updateList.add(item);
        }
      }
      if(!updateList.isEmpty()){
        //新增或修改
        result=insertOrUpdateBatch(updateList);
      }
      if(!deleteList.isEmpty()){
        //逻辑删除
        result= updateBatchById(deleteList);
      }
    }
    return result;
  }

}