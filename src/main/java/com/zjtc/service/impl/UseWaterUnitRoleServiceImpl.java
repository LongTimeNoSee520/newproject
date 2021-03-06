package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.waterBiz.UseWaterUnitRoleMapper;
import com.zjtc.model.UseWaterUnitRole;
import com.zjtc.service.UseWaterUnitRoleService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Service
@Slf4j
public class UseWaterUnitRoleServiceImpl extends
    ServiceImpl<UseWaterUnitRoleMapper, UseWaterUnitRole> implements
    UseWaterUnitRoleService {

  @Override
  public boolean checkUserRight(String personId, String unitTypeCode, String nodeCode) {
    if (StringUtils.isBlank(personId) || StringUtils.isBlank(unitTypeCode) || StringUtils
        .isBlank(nodeCode)) {
      log.error("查看用户是否有权限进行操作传入参数有误,方法名为:+checkUserRight");
      return false;
    }
//    截取单位类型号的五至六位进行匹配
    String unitTypeCodes = unitTypeCode.substring(4, 6);
    int i = this.baseMapper.checkUserRight(personId, unitTypeCodes, nodeCode);
    if (i > 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<String> selectUseWaterUnitRole(String personId, String nodeCode) {

    if (StringUtils.isBlank(personId) || StringUtils
        .isBlank(nodeCode)) {
      log.error("查看用户是否有权限进行操作传入参数有误,方法名为:+checkUserRight");
      return null;
    }
    return this.baseMapper.selectUseWaterUnitRole(personId, nodeCode);
  }

  @Override
  public ApiResponse selectUserRelevanceRoleMessage(String nodeCode) {
    ApiResponse response = new ApiResponse();
    Map<Object, List<String>> message = this.baseMapper
        .selectUserRelevanceRoleMessage(nodeCode);
    response.setCode(200);
    response.setData(message);
    return response;
  }
}
