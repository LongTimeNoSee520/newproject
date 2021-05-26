package com.zjtc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.ContactsMapper;
import com.zjtc.mapper.waterSys.PersonMapper;
import com.zjtc.model.Contacts;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.vo.AddressBook;
import com.zjtc.model.vo.OperatorVo;
import com.zjtc.model.vo.UnitVo;
import com.zjtc.service.ContactsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/23
 */

@Service
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, Contacts> implements
    ContactsService {


  @Value("${waterSms.transIp}")
  private String ipPort;

  @Value("${waterOperator.selectOperator}")
  private String selectOperator;

  @Autowired
  private PersonMapper personMapper;


  @Override
  public List<Contacts> queryByUnitId(String useWaterUnitId) {
    return this.baseMapper.queryByUnitId(useWaterUnitId);
  }

  @Override
  public boolean delete(List<String> ids) {
    if (ids.isEmpty()) {
      return false;
    } else {
      return this.baseMapper.delete(ids);
    }
  }

  @Override
  public boolean deleteContacts(String useWaterUnitId) {
    List<Contacts> contacts = this.queryByUnitId(useWaterUnitId);
    List<String> ids = new ArrayList<>();
    for (Contacts cont : contacts) {
      ids.add(cont.getId());
    }
    return this.delete(ids);
  }

  @Override
  public boolean deleteContacts(List<String> useWaterUnitIds) {
    if (useWaterUnitIds.isEmpty()) {
      return false;
    } else {
      for (String id : useWaterUnitIds) {
        this.deleteContacts(id);
      }
      return true;
    }
  }

  @Override
  public boolean add(List<Contacts> contactsList, String useWaterUnitId, String unitCode,
      String nodeCode) {
    if (contactsList.isEmpty()) {
      return false;
    } else {
      for (Contacts contacts : contactsList) {
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
    if (StringUtils.isNotBlank(unitCode)) {
      return this.baseMapper.selectByUnitCode(unitCode);
    } else {
      return null;
    }
  }

  @Override
  public int selectMaxCount(List<String> useWaterUnitIds) {
    return baseMapper.selectMaxCount(useWaterUnitIds);
  }

  @Override
  public LinkedList<AddressBook> selectContacts(String nodeCode, List<AddressBook> fathers) {
    return this.baseMapper.selectContacts(nodeCode, fathers);
  }

  @Override
  public List<Map<String, Object>> selectByMobileNumber(String mobileNumber) {
//    查询当前登录者所在的单位
    List<Map<String, Object>> units = this.baseMapper
        .selectByMobileNumberAll(mobileNumber, null);
    if (null != units) {
      for (Map<String, Object> unit : units) {
        unit.put("contacts",
            this.baseMapper.selectContactsByUnitName(unit.get("unitName").toString()));
      }
    }
    return units;
  }

  @Override
  public List<Map<String, Object>> selectByMobileNumberWX(String openId) {
//    查询当前登录者所在的单位
    List<Map<String, Object>> units = this.baseMapper
        .selectByMobileNumberAll(null, openId);
    if (null != units) {
      for (Map<String, Object> unit : units) {
        unit.put("contacts",
            this.baseMapper.selectContactsByUnitName(unit.get("unitName").toString()));
      }
    }
    return units;
  }

  @Override
  public List<OperatorVo> selectOperatorPublic(String mobileNumber) {
    List<OperatorVo> operatorVos = new ArrayList<>();
//    1、查询对应的节水办人员id
    List<String> personIds = this.baseMapper.selectPersonId(mobileNumber, null);
//    2、根据人员id查询人员信息
    operatorVos = personMapper.selectOperator(personIds);
    return operatorVos;
  }

  @Override
  public List<OperatorVo> selectOperatorWX(String openId) {
    List<OperatorVo> operatorVos = new ArrayList<>();
//    1、查询对应的节水办人员id
    List<String> personIds = this.baseMapper.selectPersonId(null, openId);
//    2、根据人员id查询人员信息
    operatorVos = personMapper.selectOperator(personIds);
    return operatorVos;
  }
}
