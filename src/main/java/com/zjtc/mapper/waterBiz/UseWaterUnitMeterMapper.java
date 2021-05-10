package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.WaterMonthUseData;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Mapper
public interface UseWaterUnitMeterMapper extends BaseMapper<UseWaterUnitMeter> {

  /**
   * 查看档案号是否已被其他部门关联
   *
   * @param waterMeterCode 档案号
   * @return 匹配到的单位名称
   */
  String selectWaterMeterCodeWhetherOccupy(@Param("waterMeterCode") String waterMeterCode);


//  /**
//   * 批量删除水表
//   * @param ids 部门id
//   * @return 删除结果
//   */
//  boolean deletedUseWaterUnitMeter(@Param("ids") List<String> ids);

  /**
   * 根据部门id查询对应信息
   * @param id 部门id
   * @return 用水单位水表数据集
   */
  List<UseWaterUnitMeter> selectUseWaterUnitMeter(@Param("id") String id);

  /**
   * 通过水表档案号查询对应数据的id
   * @param waterMeterCode 水表档案号
   * @return 对应数据的id
   */
  List<WaterMonthUseData> selectWaterMeterCodeMyId(@Param("waterMeterCode") String waterMeterCode);
}
