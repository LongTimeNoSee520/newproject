package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterUnitRoleMapper;
import com.zjtc.model.UseWaterUnitRole;
import com.zjtc.service.UseWaterUnitRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Service
public class UseWaterUnitRoleServiceImpl extends
    ServiceImpl<UseWaterUnitRoleMapper, UseWaterUnitRole> implements
    UseWaterUnitRoleService {

  @Override
  public boolean checkUserRight(String personId, String unitTypeCode, String nodeCode) {
    if (StringUtils.isBlank(personId) || StringUtils.isBlank(unitTypeCode) || StringUtils
        .isBlank(nodeCode)) {
      return false;
    }
//    截取单位类型号的二至四位进行匹配
    String unitTypeCodes = unitTypeCode.substring(2, 4);
    int i = this.baseMapper.checkUserRight(personId, unitTypeCodes, nodeCode);
    if (i > 0) {
      return true;
    } else {
      return false;
    }
  }
}
