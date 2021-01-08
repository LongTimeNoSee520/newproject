package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlanAdd;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
@Mapper
public interface UseWaterPlanAddMapper extends BaseMapper<UseWaterPlanAdd> {

  void updateRemarks(@Param("id") String id, @Param("remarks") String remarks);
  void updatePlanAdd(@Param("useWaterPlanAdds") List<UseWaterPlanAdd> useWaterPlanAdds);

  void updateStatusOrPrinted(Map<String, Object> map);

  /**
   * 修改用水计划调整数据是否打印状态
   * @param id 打印的主键id
   * @return 响应行数
   */
  int updatePrinted(@Param("id") String id);


}
