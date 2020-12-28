package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.WaterUseData;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 水使用量数据
 */
@Mapper
public interface WaterUseDataMapper extends BaseMapper<WaterUseData> {


  /**
   * 根据单位id查询水使用量数据相关信息
   * @param id 单位id
   * @return 水使用量数据数据集
   */
  List<WaterUseData> selectWaterUseDataId(@Param("id") String id);

  /**
   * 删除部门信息(将部门id设为空)
   * @param ids id
   * @return 删除结果
   */
  boolean deletedUnit(@Param("ids") List<String> ids);

  /**
   * 查询当前nodeCode下可以选择的年份
   * @param nodeCode 节点编码
   * @return 结果集
   */
  List<Integer> queryYear(@Param("nodeCode") String nodeCode);

  /**
   * 根据水表档案号回填水表信息
   * @param waterMeterCode 水表档案号集
   * @return 水表使用量信息
   */
  List<WaterUseData> selectWaterUseData(List<String> waterMeterCode);

}
