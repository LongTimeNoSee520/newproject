package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.WaterMonthUseData;
import java.util.List;
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
}
