package com.zjtc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.ContactsMapper;
import com.zjtc.model.Contacts;
import com.zjtc.service.ContactsService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/23
 */

@Service
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, Contacts> implements
    ContactsService {


  @Override
  public List<Contacts> queryByUnitId(String useWaterUnitId) {
    return this.baseMapper.queryByUnitId(useWaterUnitId);
  }

  @Override
  public boolean delete(List<String> ids) {
    if (ids.isEmpty()){
      return false;
    }else {
      return this.baseMapper.delete(ids);
    }
  }

  @Override
  public boolean deleteContacts(String useWaterUnitId) {
    List<Contacts> contacts = this.queryByUnitId(useWaterUnitId);
    List<String> ids = new ArrayList<>();
    for (Contacts cont :contacts){
      ids.add(cont.getId());
    }
    return this.delete(ids);
  }

  @Override
  public boolean deleteContacts(List<String> useWaterUnitIds) {
    if (useWaterUnitIds.isEmpty()){
      return false;
    }else {
    for (String id :useWaterUnitIds){
      this.deleteContacts(id);
    }
    return true;
    }
  }

  @Override
  public boolean add(List<Contacts> contactsList, String useWaterUnitId, String unitCode,
      String nodeCode) {
    if(contactsList.isEmpty()){
      return false;
    }else {
    for (Contacts contacts : contactsList){
         contacts.setId(UUID.randomUUID().toString().replace("-", ""));
         contacts.setDeleted("0");
         contacts.setNodeCode(nodeCode);
         contacts.setUnitCode(unitCode);
         contacts.setUseWaterUnitId(useWaterUnitId);
    }
    return this.saveBatch(contactsList);
  }
  }

  @Override
  public Contacts selectByUnitCode(String unitCode) {
    if (StringUtils.isNotBlank(unitCode)){
      return this.baseMapper.selectByUnitCode(unitCode);
    }else {
      return null;
    }
  }

  @Override
  public int selectMaxCount(List<String> useWaterUnitIds) {
    return baseMapper.selectMaxCount(useWaterUnitIds);
  }
}
