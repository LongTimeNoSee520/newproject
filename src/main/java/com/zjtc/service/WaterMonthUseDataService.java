package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.WaterMonthUseData;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 * 水使用量月数据
 */
public interface WaterMonthUseDataService extends IService<WaterMonthUseData> {


  /**
   * 删除单位时将水使用量月数据表相关的单位id对应的数据清空
   * @param id 部门id
   * @return 删除是否成功
   */
  boolean deletedUnit(String id);


  /**
   * 删除单位时将水使用量月数据表相关的单位id对应的数据清空
   * @param id 部门id
   * @return 删除是否成功
   */
  boolean deletedUnit(List<String> id);


  /**
   * 查询当年水表信息
   * @param useWaterUnitId 单位id
   * @param nodeCode 节点编码
   * @return 水表集合
   */
  List<WaterMonthUseData> selectWaterMonthUseData(String useWaterUnitId,String nodeCode);

  /**
   * 分页
   * @param jsonObject
   * @return
   */
  Map<String,Object> queryPage(JSONObject jsonObject);
}
