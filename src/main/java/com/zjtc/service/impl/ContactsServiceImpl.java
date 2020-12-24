package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.ContactsMapper;
import com.zjtc.model.Contacts;
import com.zjtc.service.ContactsService;
import java.util.List;
import java.util.UUID;
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
    return this.insertBatch(contactsList);
  }
  }
}
