package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterUnitMeter;
import java.util.List;
import org.springframework.context.annotation.Bean;

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
 boolean insertUseWaterUnitMeter(UseWaterUnitMeter useWaterUnitMeter);

  /**
   * 删除水表
   * @param id 水表id集
   * @return 是否删除成功
   */
 boolean deletedUseWaterUnitMeter(List<String> id);

  /**
   * 查询水表信息
   * @param useWaterUnitId 单位id
   * @param nodeCode 区域编码
   * @return 水表信息集
   */
  @Bean
 List<UseWaterUnitMeter> selectUseWaterUnitMeter(String useWaterUnitId,String nodeCode);


}
