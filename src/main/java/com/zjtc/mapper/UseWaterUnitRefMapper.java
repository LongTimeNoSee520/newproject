package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnitRef;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Mapper
public interface UseWaterUnitRefMapper extends BaseMapper<UseWaterUnitRef> {

  /**
   * 根据单位编号查询相关编号信息
   * @param ids
   * @return
   */
  List<Map<String,Object>> selectBatchIds(@Param("ids") List<String> ids);
}
