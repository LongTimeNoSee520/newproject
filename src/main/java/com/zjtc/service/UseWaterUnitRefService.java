package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.UseWaterUnitRef;
import java.util.List;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
public interface UseWaterUnitRefService extends IService<UseWaterUnitRef> {

  /**
   * 根据当前单位id查询出所有关联单位id
   * @param id
   * @param nodeCode
   * @return
   */
  List<String> findIdList(String id,String nodeCode);


  /**
   * 新增
   * @param useWaterUnitId 单位id 取当前关联的单位id
   * @param useWaterUnitIdRef 关联单位id，取当前添加的单位id
   * @param nodeCode 节点编码
   * @return
   */
  boolean save(String useWaterUnitId,String useWaterUnitIdRef,String nodeCode);

  /**根据单位id删除相关单位管理表数据
   *
   * @param  ids 单位id集合

   * @return
   */
  boolean deleteBatch(List<String> ids);

}

