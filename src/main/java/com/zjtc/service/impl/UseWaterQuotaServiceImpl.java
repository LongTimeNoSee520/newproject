package com.zjtc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.UseWaterQuotaMapper;
import com.zjtc.model.UseWaterQuota;
import com.zjtc.service.UseWaterQuotaService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/24
 */

@Service
public class UseWaterQuotaServiceImpl extends ServiceImpl<UseWaterQuotaMapper, UseWaterQuota> implements
    UseWaterQuotaService {


  @Override
  public List<UseWaterQuota> queryByUnitId(String useWaterUnitId) {
    return this.baseMapper.queryByUnitId(useWaterUnitId);
  }

  @Override
  public boolean delete(List<String> ids) {
    if (ids.isEmpty()){
      return false;
    }else {
    return this.removeByIds(ids);
    }
  }

  @Override
  public boolean deleteQuotas(String useWaterUnitId) {
    List<UseWaterQuota> useWaterQuotas = this.queryByUnitId(useWaterUnitId);
    List<String> ids = new ArrayList<>();
    for (UseWaterQuota quota:useWaterQuotas){
      ids.add(quota.getId());
    }
    return this.delete(ids);
  }

  @Override
  public boolean deleteQuotas(List<String> useWaterUnitIds) {
    if (useWaterUnitIds.isEmpty()){
      return false;
    }else {
      for (String id :useWaterUnitIds){
        this.deleteQuotas(id);
      }
      return true;
    }
  }

  @Override
  public boolean add(List<UseWaterQuota> useWaterQuotaList, String useWaterUnitId,
      String nodeCode) {
    if (useWaterQuotaList.isEmpty()){
      return false;
    }else {
      for (UseWaterQuota useWaterQuota : useWaterQuotaList) {
        useWaterQuota.setCreateTime(new Date());
        useWaterQuota.setNodeCode(nodeCode);
        useWaterQuota.setUseWaterUnitId(useWaterUnitId);
      }
      return this.saveBatch(useWaterQuotaList);
    }
  }
}
