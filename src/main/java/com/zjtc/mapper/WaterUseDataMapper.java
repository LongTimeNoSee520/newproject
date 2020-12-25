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
}
