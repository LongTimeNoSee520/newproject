package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterUnitRole;
import java.util.List;
import java.util.Map;

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


  /**
   * 查询所有人员信息和关联的权限模块类型
   * @param nodeCode 节点编码
   * @return 集合
   */
  ApiResponse selectUserRelevanceRoleMessage(String nodeCode);
}
