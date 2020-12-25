package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterUnitRole;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
public interface UseWaterUnitRoleService extends IService<UseWaterUnitRole> {


  /**
   * 查看用户是否有权限进行操作
   * @param personId 人员id
   * @param unitTypeCode 单位类型号
   * @param nodeCode 节点编码
   * @return 是否有权限 true=有
   */
  boolean checkUserRight(String personId,String unitTypeCode,String nodeCode);

  /**
   * 查询单位批次号
   * @param personId 人员id
   * @param nodeCode 节点编码
   * @return 批次号结果集
   */
  List<String> selectUseWaterUnitRole(String personId,String nodeCode);
}
