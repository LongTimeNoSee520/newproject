package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Contacts;
import java.util.List;

/**
 * @author lianghao
 * @date 2020/12/23
 */

public interface ContactsService extends IService<Contacts> {

  /**
   * 通过用水单位id查询联系人信息
   * */
  List<Contacts> queryByUnitId(String useWaterUnitId);

  /**
   * 批量删除
   * */
  boolean delete(List<String> ids);

  /**
   * 通过用水单位id批量删除
   * */
  boolean deleteContacts(String useWaterUnitId);

  /**
   * 通过用水单位id列表批量删除
   * */
  boolean deleteContacts(List<String> useWaterUnitIds);
  /**
   * 批量新增
   * */
  boolean add(List<Contacts> contactsList,String useWaterUnitId,String unitCode,String nodeCode);
}
