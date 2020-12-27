package com.zjtc.mapper;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.WaterMonthUseData;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 * 水使用量月数据
 */
@Mapper
public interface WaterMonthUseDataMapper extends BaseMapper<WaterMonthUseData> {

  /**
   * 查询当年水表信息
   * @param useWaterUnitId 单位id
   * @param nodeCode 节点编码
   * @return 水表集合
   */
  List<WaterMonthUseData> selectWaterMonthUseData(@Param("useWaterUnitId") String useWaterUnitId,@Param("nodeCode") String nodeCode);

  /**
   * 根据单位id查询水使用量月数据相关数据
   * @param id 单位id
   * @return 水使用量月数据数据集
   */
  List<WaterMonthUseData> selectWaterMonthUseDataId(@Param("id") String id);

  /**
   * 删除
   * @param ids 删除单位(将水使用量月数据的部门id清空)
   * @return 删除结果
   */
 boolean updateWaterMonthUseData(@Param("ids") List<String> ids);

  /**
   * 分页
   */
  List<Map<String,Object>> queryPage(JSONObject jsonObject);

  /**
   * 分页查询出的数据总条数
   * @param jsonObject
   * @return
   */
  long queryListTotal(JSONObject jsonObject);
}
