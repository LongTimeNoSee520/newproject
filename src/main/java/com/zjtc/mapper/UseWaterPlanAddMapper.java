package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterPlanAdd;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/5
 */
@Mapper
public interface UseWaterPlanAddMapper extends BaseMapper<UseWaterPlanAdd> {

  void updateRemarks(@Param("id") String id, @Param("remarks") String remarks);

  /**
   *
   * @param unitName 单位名称
   * @param userType 用户类型
   * @param executed 是否执行
   * @param nodeCode 节点编码
   * @param auditStatus 是否审核
   * @return 匹配的条数
   */
  Integer selectCount(String unitName, String userType, String executed, String nodeCode, String auditStatus);

  /**
   *
   * @param currPage  页数
   * @param pageSize  条数
   * @param unitName 单位名称
   * @param userType 用户类型
   * @param executed 是否执行
   * @param nodeCode 节点编码
   * @param auditStatus 是否审核
   * @return 数据集
   */
  List<UseWaterPlanAdd> UseWaterPlanAdd(Integer currPage, Integer pageSize, String unitName, String userType, String executed, String nodeCode, String auditStatus);

//  通过登录者查询出只负责登录人自己负责的单位id
  List<String> selectUseWaterUnitId(@Param("loginId") String loginId);
}
