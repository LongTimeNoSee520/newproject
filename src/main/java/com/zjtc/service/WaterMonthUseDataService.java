package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.User;
import com.zjtc.model.WaterMonthUseData;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

  /**
   * 根据水表档案号回填水表信息
   * @param waterMeterCodes 水表档案号集
   * @return 水表使用量信息
   */
  List<WaterMonthUseData> selectWaterUseData(List<String> waterMeterCodes);

  /**
   * 导出
   * @param jsonObject
   * @param request
   * @param response
   */
  void export(User user,JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);


  boolean updateUnitBatch(List<WaterMonthUseData> list);
}
