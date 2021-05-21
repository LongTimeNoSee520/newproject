package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterUnitRole;
import com.zjtc.model.vo.AddressBook;
import java.util.LinkedList;
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


  /**
   * 查询所有人员信息和关联的权限模块类型
   * @param nodeCode 节点编码
   * @return 集合
   */
  ApiResponse selectUserRelevanceRoleMessage(String nodeCode);

  /**
   * 根据人员id查询所有操作批次
   * @param personId 人员id
   * @param nodeCode 节点编码
   * @return 结果集
   */
  ApiResponse selectByIdUnitTypeCodeAll(String personId,String nodeCode);

  /**
   * 授权单位批次号
   * @param personId 角色id
   * @param unitTypeCodes 单位批次号
   * @return 响应状态
   */
  ApiResponse add(String personId,String nodeCode,List<String> unitTypeCodes);

  /**
   * 授权单位批次号
   * @param personId 角色id
   * @param unitTypeCodes 单位批次号
   * @return 响应状态
   */
  ApiResponse addUseWaterUnitRole(String personId,String nodeCode,List<String> unitTypeCodes);

  /**
   * 查询用户类型权限
   * @param personId
   * @param nodeCode
   * @return
   */
 LinkedList<AddressBook> selectUnitRoles(String personId,String nodeCode);
}
