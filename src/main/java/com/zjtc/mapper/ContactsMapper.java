package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Contacts;
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

  String selectByUserId(@Param("operatorId") String operatorId);

  int selectMaxCount(@Param("useWaterUnitIds") List<String> useWaterUnitIds);
}