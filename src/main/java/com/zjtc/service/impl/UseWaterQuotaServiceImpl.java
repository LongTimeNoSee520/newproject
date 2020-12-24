package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterQuotaMapper;
import com.zjtc.model.UseWaterQuota;

import com.zjtc.service.UseWaterQuotaService;
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
    return this.deleteBatchIds(ids);
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
      return this.insertBatch(useWaterQuotaList);
    }
  }
}
