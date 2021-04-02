package com.zjtc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.waterBiz.UseWaterUnitRoleMapper;
import com.zjtc.model.UseWaterUnitRole;
import com.zjtc.service.UseWaterUnitRoleService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

  @Override
  public ApiResponse selectByIdUnitTypeCodeAll(String personId, String nodeCode) {
    ApiResponse response = new ApiResponse();
    List<String> list;
    if (StringUtils.isBlank(personId)) {
      response.recordError("系统异常");
      return response;
    }
    try {
      list = this.baseMapper.selectByIdUnitTypeCodeAll(personId, nodeCode);
    } catch (Exception e) {
      log.error("查询到人员批次信息为空=========" + e.getMessage());
      response.recordError("查询到人员批次信息为空");
      return response;
    }
    response.setData(list);
    return response;
  }

  @Override
  public ApiResponse add(String personId, String nodeCode, List<String> unitTypeCodes) {
    ApiResponse response = new ApiResponse();
    List<UseWaterUnitRole> unitRoles = new ArrayList<>();
    if (StringUtils.isBlank(personId) || StringUtils.isBlank(nodeCode)) {
      response.recordError("系统异常");
      return response;
    }
//    先删除该人员id对应的批次号
    try {
      this.baseMapper.deletedByPersonId(personId);
    } catch (Exception e) {
      log.error("该用户信息异常:"+e.getMessage());
    }
//    if (i == 0) {
//      response.recordError("系统异常");
//      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//      return response;
//    }
//    如果单位批次号传为空,那么说明这个人没有批次号权限
    if (unitTypeCodes.isEmpty()) {
        UseWaterUnitRole useWaterUnitRole = new UseWaterUnitRole();
        useWaterUnitRole.setId(UUID.randomUUID().toString().replace("-", ""));
        useWaterUnitRole.setPersonId(personId);
        useWaterUnitRole.setUnitTypeCode(null);
        useWaterUnitRole.setNodeCode(nodeCode);
        useWaterUnitRole.setCreateTime(new Date());
        unitRoles.add(useWaterUnitRole);
    } else {
//    批量新增
      for (String unitTypeCode : unitTypeCodes) {
        UseWaterUnitRole useWaterUnitRole = new UseWaterUnitRole();
        useWaterUnitRole.setId(UUID.randomUUID().toString().replace("-", ""));
        useWaterUnitRole.setPersonId(personId);
        useWaterUnitRole.setUnitTypeCode(unitTypeCode);
        useWaterUnitRole.setNodeCode(nodeCode);
        useWaterUnitRole.setCreateTime(new Date());
        unitRoles.add(useWaterUnitRole);
      }
    }
    boolean b = this.saveBatch(unitRoles);
    if (b) {
      response.setCode(200);
      response.setMessage("授权成功");
      return response;
    }
    response.recordError("授权失败");
    return response;
  }


  @Override
  public ApiResponse addUseWaterUnitRole(String personId, String nodeCode,
      List<String> unitTypeCodes) {

    ApiResponse response = new ApiResponse();
    List<UseWaterUnitRole> unitRoles = new ArrayList<>();
    if (StringUtils.isBlank(personId) || StringUtils.isBlank(nodeCode) || unitTypeCodes.isEmpty()) {
      response.recordError("系统异常");
      return response;
    }
    //    批量新增
    for (String unitTypeCode : unitTypeCodes) {
      UseWaterUnitRole useWaterUnitRole = new UseWaterUnitRole();
      useWaterUnitRole.setId(UUID.randomUUID().toString().replace("-", ""));
      useWaterUnitRole.setPersonId(personId);
      useWaterUnitRole.setUnitTypeCode(unitTypeCode);
      useWaterUnitRole.setNodeCode(nodeCode);
      useWaterUnitRole.setCreateTime(new Date());
      unitRoles.add(useWaterUnitRole);
    }
    boolean b = this.saveBatch(unitRoles);
    if (b) {
      response.setCode(200);
      return response;
    }
    response.recordError("授权失败");
    return response;
  }
}
