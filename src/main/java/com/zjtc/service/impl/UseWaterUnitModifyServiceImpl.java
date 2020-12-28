package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitModifyMapper;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.UseWaterUnitModify;
import com.zjtc.service.UseWaterUnitModifyService;
import com.zjtc.service.UseWaterUnitService;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 用水单位名称修改日志
 */
@Service
@Slf4j
public class UseWaterUnitModifyServiceImpl extends
    ServiceImpl<UseWaterUnitModifyMapper, UseWaterUnitModify> implements
    UseWaterUnitModifyService {

  @Autowired
  private UseWaterUnitService useWaterUnitService;

  @Override
  public boolean insertUnitName(String id, String nodeCode, String unitName, String personName,
      String personId) {

    if (StringUtils.isBlank(id) || StringUtils.isBlank(nodeCode) || StringUtils.isBlank(unitName)
        || StringUtils.isBlank(personName) || StringUtils.isBlank(personId)) {
      log.error("判断部门名称是否被修改时传入参数出错");
      return false;
    }
    UseWaterUnitModify modify = new UseWaterUnitModify();
    EntityWrapper<UseWaterUnit> useWaterUnitEntityWrapper = new EntityWrapper<>();
    useWaterUnitEntityWrapper.eq("id", id);
    useWaterUnitEntityWrapper.eq("node_code", nodeCode);
    useWaterUnitEntityWrapper.eq("deleted", "0");
    List<UseWaterUnit> useWaterUnits = this.useWaterUnitService
        .selectList(useWaterUnitEntityWrapper);
    int insert = 0;
    for (UseWaterUnit useWaterUnit : useWaterUnits) {
      if (!useWaterUnit.getUnitName().equals(unitName)) {
        modify.setUseWaterUnitId(useWaterUnit.getId());
        modify.setNodeCode(nodeCode);
        modify.setBeforeName(useWaterUnit.getUnitName());
        modify.setAfterName(unitName);
        modify.setModifyTime(new Date());
        modify.setModifyPerson(personName);
        modify.setModifyPersonId(personId);
        insert = this.baseMapper.insert(modify);
      }
    }
    if (insert > 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<UseWaterUnitModify> selectUseWaterUnitModify(String id, String nodeCode) {
    EntityWrapper<UseWaterUnitModify> entityWrapper = new EntityWrapper<>();
    entityWrapper.eq("use_water_unit_id", id);
    entityWrapper.eq("node_code", nodeCode);
    return this.baseMapper.selectList(entityWrapper);
  }

//  @Override
//  public boolean deletedUseWaterUnitModify(String id) {
//    EntityWrapper<UseWaterUnitModify> entityWrapper = new EntityWrapper<>();
//    entityWrapper.eq("use_water_unitId", id);
//    List<UseWaterUnitModify> useWaterUnitModifies = this.baseMapper.selectList(entityWrapper);
//    int integer = 0;
//    for (UseWaterUnitModify unitModify : useWaterUnitModifies) {
//      integer = this.baseMapper.deleteById(unitModify.getId());
//    }
//    if (integer > 0) {
//      return true;
//    } else {
//      return false;
//    }
//  }
}
