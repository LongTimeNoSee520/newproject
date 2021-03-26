package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.UseWaterQuota;
import java.util.List;


/**
 * @author lianghao
 * @date 2020/12/24
 */

public interface UseWaterQuotaService extends IService<UseWaterQuota> {
  /**
   * 通过用水单位id查询用水定额信息
   * */
  List<UseWaterQuota> queryByUnitId(String useWaterUnitId);

  /**
   * 批量删除
   * */
  boolean delete(List<String> ids);

  /**
   * 通过用水单位id批量删除
   * */
  boolean deleteQuotas(String useWaterUnitId);

  /**
   * 通过用水单位id列表批量删除
   * */
  boolean deleteQuotas(List<String> useWaterUnitIds);
  /**
   * 批量新增
   * */
  boolean add(List<UseWaterQuota> contactsList,String useWaterUnitId,String nodeCode);


}
