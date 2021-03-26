package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.WaterUseData;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 * 水使用量数据
 */
public interface WaterUseDataService extends IService<WaterUseData> {


  /**
   * 删除单位时将水使用量数据表相关的单位id对应的数据清空
   * @param id 部门id
   * @return 删除是否成功
   */
  boolean deletedUnit(String id);

  /**
   * 删除单位时将水使用量数据表相关的单位id对应的数据清空
   * @param ids 部门id
   * @return 删除是否成功
   */
  boolean deletedUnit(List<String> ids);

  /**
   * 查询当前nodeCode下可以选择的年份
   * @param nodeCode 节点编码
   * @return 结果集
   */
  List<Integer> queryYear( String nodeCode);

  /**
   * 根据水表档案号回填水表信息
   * @param waterMeterCode 水表档案号集
   * @return 水表使用量信息
   */
  List<WaterUseData> selectWaterUseData(List<String> waterMeterCode);
}
