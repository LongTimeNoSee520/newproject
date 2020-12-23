package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnitRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Mapper
public interface UseWaterUnitRoleMapper extends BaseMapper<UseWaterUnitRole> {

  /**
   * 查看用户是否有权限进行操作
   *
   * @param personId      人员id
   * @param unitTypeCodes 截取的单位类型号的第三位和第四位
   * @param nodeCode      节点编码
   * @return 是否有权限
   */
  int checkUserRight(@Param("personId") String personId,
      @Param("unitTypeCodes") String unitTypeCodes, @Param("nodeCode") String nodeCode);
}
