package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlanAdd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
@Mapper
public interface UseWaterPlanAddMapper extends BaseMapper<UseWaterPlanAdd> {

  void updateRemarks(@Param("id") String id, @Param("remarks") String remarks);
}
