package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.UseWaterUnitMapper;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitRefService;
import com.zjtc.service.UseWaterUnitRoleService;
import com.zjtc.service.UseWaterUnitService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Service
public class UseWaterUnitServiceImpl extends
    ServiceImpl<UseWaterUnitMapper, UseWaterUnit> implements
    UseWaterUnitService {

  @Autowired
  private UseWaterUnitRoleService useWaterUnitRoleService;
  @Autowired
  private UseWaterUnitRefService useWaterUnitRefService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse save(UseWaterUnit useWaterUnit, User user) {
    ApiResponse apiResponse = new ApiResponse();
    //当前序号，取unitCode 7-9位
    String rank = useWaterUnit.getUnitCode().substring(7, 9);
    /**验证当前用户是否有操作当前批次的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), useWaterUnit.getUnitCode(), user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该批次权限");
      return apiResponse;
    }
    /**验证单位编号是否重复,先查询出当前节点编码*/
    String maxCount = baseMapper.maxUnitCode(useWaterUnit.getUnitCode(), null, user.getNodeCode());
    if (maxCount == rank) {
      //排序号重复;
      rank += 1;
      apiResponse.recordError("当前序号已存在");
      apiResponse.setData(rank);
      return apiResponse;
    }
    /**新增用水单位表数据*/
    /**新增水表数据*/
    /**新增银行数据*/
    /**新增联系人数据*/
    /**新增责任书数据*/
    /**新增用水定额数据*/
    /**新增相关编号信息数据*/
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApiResponse update(UseWaterUnit useWaterUnit, User user) {
    ApiResponse apiResponse = new ApiResponse();
    //当前序号，取unitCode 7-9位
    String rank = useWaterUnit.getUnitCode().substring(7, 9);
    /**验证当前用户是否有操作当前批次的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), useWaterUnit.getUnitCode(), user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该批次权限");
      return apiResponse;
    }
    /**验证单位编号是否重复,先查询出当前节点编码*/
    String maxCount = baseMapper.maxUnitCode(useWaterUnit.getUnitCode(), useWaterUnit.getId(), user.getNodeCode());
    if (maxCount == rank) {
      //排序号重复
      rank += 1;
      apiResponse.recordError("当前序号已存在");
      apiResponse.setData(rank);
      return apiResponse;
    }
    /**修改用水单位表数据*/
    /**修改水表数据*/
    /**修改银行数据*/
    /**修改联系人数据*/
    /**修改责任书数据*/
    /**修改用水定额数据*/
    /**修改相关编号信息数据*/
    /**新增单位名称修改日志信息数据*/
    return apiResponse;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean delete(JSONObject jsonObject ,User user) {
    /**批量删除*/
    /**删除应税单位数据逻辑删除，逻辑删除*/
    /**删除水表数据*/
    /**删除银行数据，*逻辑删除/
    /**删除联系人数据，逻辑删除*/
    /**删除责任书数据*/
    /**删除用水定额数据*/
    /**删除相关编号信息数据*/
    /**删除单位名称修改日志数据*/
    return false;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    List<Map<String, Object>> result = baseMapper.queryPage(jsonObject);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //查询总数据条数
    long total = baseMapper.queryListTotal(jsonObject);
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  public List<UseWaterUnit> queryAll(JSONObject jsonObject) {
    return null;
  }

  @Override
  public List<Map<String, Object>> findUnitCode(JSONObject jsonObject) {

    return null;
  }

  @Override
  public String unitCodeValidate(String unitCode, String nodeCode) {
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("unit_code", unitCode);
    wrapper.eq("node_code", nodeCode);
    long count = this.selectCount(wrapper);
    if (count > 0) {
      /**当前要添加的单位编号已存在*/

    }
    return null;
  }

  @Override
  public ApiResponse createunitCode(User user, String unitCode, String id) {
    ApiResponse apiResponse = new ApiResponse();
    /**验证当前用户是否有新增当前批次的权限*/
    boolean flag = useWaterUnitRoleService
        .checkUserRight(user.getId(), unitCode, user.getNodeCode());
    if (!flag) {
      //当前用户没有操作权限
      apiResponse.recordError("当前用户没有操作该批次权限");
      return apiResponse;
    }
    /**排序号：查询当前节点编码、当前批次,节点编码后三位最大值*/
    String maxCount = baseMapper.maxUnitCode(unitCode, id, user.getNodeCode());
    if (StringUtils.isNotBlank(maxCount)) {
      //生成新的排序号，最大值加1
      maxCount += 1;
      apiResponse.setData(maxCount);
    }
    return apiResponse;
  }

  @Override
  public String createAreaCode(String nodeCode) {
    /**区域码，取当前节点编码后2位*/
    String areaCode = nodeCode.substring(nodeCode.length() - 2);
    return areaCode;
  }

  @Override
  public List<Map<String, Object>> addUnitCodeList(User user) {
    //查询当前节点编码下的所有单位的单位编码、单位名称
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("deleted", "0");
    wrapper.eq("node_code", user.getNodeCode());
    wrapper.setSqlSelect("unitCode", "unitName");
    return this.selectList(wrapper);
  }

  @Override
  public List<Map<String, Object>> editUnitCodeList(User user,String id) {
    List<Map<String, Object>> result=new ArrayList<>();
    //先查询出当前单位所有管理单位id
    List<String> ids=useWaterUnitRefService.findIdList(id);
    //先查询当前用水单位信息
    Wrapper wrapper = new EntityWrapper();
    wrapper.eq("id", id);
    wrapper.eq("deleted", "0");
    wrapper.eq("node_code", user.getNodeCode());
    wrapper.in("id",ids);
    wrapper.setSqlSelect("unitCode", "unitName");
    result= this.selectList(wrapper);
    return result;
  }

  @Override
  public boolean synData(JSONObject jsonObject) {
    //主数据id
    String id= jsonObject.getString("id");
    List<String> ids=jsonObject.getJSONArray("ids").toJavaList(String.class);
    /**todo*/
    return false;
  }


}
