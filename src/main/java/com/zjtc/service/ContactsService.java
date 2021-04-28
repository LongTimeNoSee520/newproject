package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.Contacts;
import com.zjtc.model.vo.OrgTreeVO;
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

  /**
   * 通过单位编号查询主要联系人信息
   * */
  Contacts selectByUnitCode(String unitCode);



  /**
   * 查询当前单位编号中联系人最多的条数
   * @param useWaterUnitIds
   * @return
   */
  int selectMaxCount(List<String> useWaterUnitIds);


  /**
   * 部门人员树 查询人员
   * @return
   */
  List<OrgTreeVO> selectContacts(String nodeCode);
}
