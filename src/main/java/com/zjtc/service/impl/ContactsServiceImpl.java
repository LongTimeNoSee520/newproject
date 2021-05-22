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
  public Map<String, Object> selectByMobileNumber(String mobileNumber, String personId,
      String unitCode) {
    Map<String, Object> map = new HashMap<>();
    if (StringUtils.isBlank(unitCode)) {
      return map;
    }
//    查询当前登录者所在的单位
    UseWaterUnit useWaterUnit = this.baseMapper
        .selectByMobileNumberAll(mobileNumber, personId, unitCode);
//      查询部门下的人员名称
    List<String> persons = this.baseMapper
        .selectByUnitIdInquirePerson(useWaterUnit.getId(), mobileNumber, personId);
    map.put("unit", useWaterUnit);
    map.put("personnel", persons);
    return map;
  }

  @Override
  public List<UnitVo> selectOperatorPublic(String mobileNumber) {
//    1、查询登录人所对应的单位信息
    List<UnitVo> useWaterUnits = this.baseMapper.selectUnitCode(mobileNumber,null);
//    System.out.println("查询到的单位信息：" + useWaterUnits);
//    2、根据单位编号查询对应的节水办人员id
    for (UnitVo unitVo : useWaterUnits){
      List<String> personIds = this.baseMapper.selectPersonIdPublic(unitVo.getUnitCode());
//      System.out.println("查询到的人员id"+ personIds);
//    3、根据人员id查询人员信息
      List<OperatorVo> operatorVos = personMapper.selectOperatorPublic(personIds);
//      System.out.println("查询到的人员信息："+operatorVos);
      for (OperatorVo operatorVo : operatorVos){
        operatorVo.setUnitType(unitVo.getUnitType());
      }
      unitVo.setOperatorVos(operatorVos);
    }
    return useWaterUnits;
  }

  @Override
  public List<UnitVo> selectOperatorWX(String openId) {
//    1、查询登录人所对应的单位信息
    List<UnitVo> useWaterUnits = this.baseMapper.selectUnitCode(null,openId);
//    System.out.println("查询到的单位信息：" + useWaterUnits);
//    2、根据单位编号查询对应的节水办人员id
    for (UnitVo unitVo : useWaterUnits){
      List<String> personIds = this.baseMapper.selectPersonIdPublic(unitVo.getUnitCode());
//      System.out.println("查询到的人员id"+ personIds);
//    3、根据人员id查询人员信息
      List<OperatorVo> operatorVos = personMapper.selectOperatorPublic(personIds);
//      System.out.println("查询到的人员信息："+operatorVos);
      for (OperatorVo operatorVo : operatorVos){
        operatorVo.setUnitType(unitVo.getUnitType());
      }
      unitVo.setOperatorVos(operatorVos);
    }
    return useWaterUnits;
  }
}
