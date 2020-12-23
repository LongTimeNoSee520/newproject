package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnitMeter;
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
   * @param waterMeterCode 档案号
   * @return 匹配的条数
   */
 int selectWaterMeterCodeWhetherOccupy(@Param("waterMeterCode") String waterMeterCode);
}
