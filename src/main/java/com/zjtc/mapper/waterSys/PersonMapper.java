package com.zjtc.mapper.waterSys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.Person;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7 人员mapper
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {

  /**
   * 查询人员的排序号是否已重复
   *
   * @param personRank 排序号
   * @param parentId   当前部门id
   * @param userId     修改的人员id
   * @return 匹配的个数
   */
  int selectCountPersonRank(@Param("personRank") int personRank,
      @Param("parentId") String parentId,
      @Param("userId") String userId);

  /**
   * 通过人员id查询到对应角色关联表id
   *
   * @param id 角色id
   * @return 角色关联表id
   */
  String selectUserById(@Param("id") String id);

  /**
   * 查新相匹配的总条数
   *
   * @param parameter 姓名和移动电话
   * @param orgId       当前部门的id
   * @return 查询到的条数
   */
  int selectCountAll(@Param("parameter") String parameter, @Param("orgId") String orgId);

  /**
   * 人员分页查询
   *
   * @param currPage  页数
   * @param pageSize  条数
   * @param parameter 查询条件
   * @param orgId       当前部门的id
   * @return 查询结果
   */
  List<Person> queryList(@Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize, @Param("parameter") String parameter,
      @Param("orgId") String orgId);


  List<Person> queryAll();

  /**
   * 根据id查询人员信息
   * @param personId 人员id
   */
  List<Person> selectPersonAll(@Param("personId") String personId);
}
