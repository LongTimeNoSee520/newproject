package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnitRole;
import com.zjtc.model.vo.AddressBook;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

  /**
   * 查询全部批次
   * @param personId      人员id
   * @param nodeCode      节点编码
   * @return 批次结果集
   */
 List<String> selectUseWaterUnitRole(@Param("personId") String personId,
     @Param("nodeCode") String nodeCode);

  /**
   * 查询所有人员信息和关联的权限模块类型
   * @param nodeCode 节点编码
   * @return 集合
   */
  Map<Object,List<String>> selectUserRelevanceRoleMessage(@Param("nodeCode") String nodeCode);

  /**
   * 根据人员id查询所有操作批次
   * @param personId 人员id
   * @param nodeCode 节点编码
   * @return 结果集
   */
  List<String> selectByIdUnitTypeCodeAll(@Param("personId") String personId,@Param("nodeCode") String nodeCode);


  /**
   * 根据人员id删除批次号
   * @param personId 人员id
   * @return 结果集
   */
  int deletedByPersonId(@Param("personId") String personId);

  /**
   * 查询用户类型权限
   * @param personId
   * @param nodeCode
   * @return
   */
  LinkedList<AddressBook> selectUnitRoles(@Param("personId")String personId,@Param("nodeCode")String nodeCode);
}
