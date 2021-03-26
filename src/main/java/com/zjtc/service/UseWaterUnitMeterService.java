package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterUnitMeter;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 * 水表信息
 */
public interface UseWaterUnitMeterService extends IService<UseWaterUnitMeter> {

  /**
   * 添加水表
   * @param useWaterUnitMeter 水表信息实体
   * @return 添加是否成功 ,false的原因为可能档案号为空或者已被使用
   */
 boolean insertUseWaterUnitMeter(List<UseWaterUnitMeter> useWaterUnitMeter,String useWaterUnitId,String nodeCode);

  /**
   * 删除部门时删除关联的水表
   * @param id 部门id
   * @return 是否删除成功
   */
  boolean deletedUseWaterUnitMeter(String id);

  /**
   * 删除部门时删除关联的水表
   * @param id 部门id
   * @return 是否删除成功
   */
 boolean deletedUseWaterUnitMeter(List<String> id);

  /**
   * 查询水表信息
   * @param useWaterUnitId 单位id
   * @param nodeCode 区域编码
   * @return 水表信息集
   */
 List<UseWaterUnitMeter> selectUseWaterUnitMeter(String useWaterUnitId,String nodeCode);

  /**
   * 回填水表信息
   * @param waterMeterCodes 水表档案号
   * @param nodeCode 区域编码
   * @return 水表信息集
   */
  ApiResponse selectUseWaterUnitMeterAll(List<String> waterMeterCodes,String nodeCode);

 /**
  * 查询所有档案号对应的单位id
  */
  Map<String ,String> getMeterMap(String nodeCode);
}
