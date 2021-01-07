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
   * 统计条数
   *
   * @param unitName    单位名称
   * @param userType    用户类型
   * @param executed    是否执行
   * @param nodeCode    节点编码
   * @param auditStatus 是否审核
   * @return 匹配的条数
   */
  Integer selectCount(
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("executed") String executed,
      @Param("nodeCode") String nodeCode,
      @Param("auditStatus") String auditStatus,
      @Param("userId") String userId);

  /**
   * 分页查询
   *
   * @param currPage    页数
   * @param pageSize    条数
   * @param unitName    单位名称
   * @param userType    用户类型
   * @param executed    是否执行
   * @param nodeCode    节点编码
   * @param auditStatus 是否审核
   * @return 数据集
   */
  List<UseWaterPlanAdd> UseWaterPlanAdd(
      @Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize,
      @Param("unitName") String unitName,
      @Param("userType") String userType,
      @Param("executed") String executed,
      @Param("nodeCode") String nodeCode,
      @Param("auditStatus") String auditStatus,
      @Param("userId") String userId);
}
