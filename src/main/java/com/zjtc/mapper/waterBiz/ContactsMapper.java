package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.Contacts;
import com.zjtc.model.vo.OrgTreeVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 联系人信息
 *
 * @author lianghao
 * @date 2020/12/23
 */
@Mapper
public interface ContactsMapper extends BaseMapper<Contacts> {

  /**
   * 通过用水单位id查询联系人信息
   */
  List<Contacts> queryByUnitId(@Param("useWaterUnitId") String useWaterUnitId);

  /**
   * 批量删除
   */
  boolean delete(@Param("ids") List<String> ids);

  Contacts selectByUnitCode(@Param("unitCode") String unitCode);


  int selectMaxCount(@Param("useWaterUnitIds") List<String> useWaterUnitIds);

  /**
   * 部门人员树 查询人员
   * @return
   */
  List<OrgTreeVO> selectContacts(@Param("nodeCodes") List<String> nodeCodes,@Param("condition")String condition,@Param("userId") String userId,@Param("nodeCode") String nodeCode);

  /**
   * 查看部门下是否有人员
   * @param orgId
   * @return
   */
  int selectOrgIsHavePerson(@Param("orgId") String orgId);
}