package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.Contacts;
import com.zjtc.model.vo.AddressBook;
import java.util.List;
import java.util.Map;

/**
 * @author lianghao
 * @date 2020/12/23
 */

public interface ContactsService extends IService<Contacts> {

  /**
   * 通过用水单位id查询联系人信息
   */
  List<Contacts> queryByUnitId(String useWaterUnitId);

  /**
   * 批量删除
   */
  boolean delete(List<String> ids);

  /**
   * 通过用水单位id批量删除
   */
  boolean deleteContacts(String useWaterUnitId);

  /**
   * 通过用水单位id列表批量删除
   */
  boolean deleteContacts(List<String> useWaterUnitIds);

  /**
   * 批量新增
   */
  boolean add(List<Contacts> contactsList, String useWaterUnitId, String unitCode, String nodeCode);

  /**
   * 通过单位编号查询主要联系人信息
   */
  Contacts selectByUnitCode(String unitCode);


  /**
   * 查询当前单位编号中联系人最多的条数
   *
   * @param useWaterUnitIds
   * @return
   */
  int selectMaxCount(List<String> useWaterUnitIds);


  /**
   * 查询部门下的人员
   *
   * @param nodeCode 节点编码
   * @param fathers 父级部门
   */
  List<AddressBook> selectContacts(String nodeCode,List<AddressBook> fathers);

  /**
   *   通过联系电话查询所在部门和该部门下所有的人员
   */
  Map<String, Object> selectByMobileNumber(String mobileNumber,String personId,String unitCode);
}
