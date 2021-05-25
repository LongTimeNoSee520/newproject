package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.model.vo.OperatorVo;
import com.zjtc.model.vo.OrgTreeVO;
import com.zjtc.model.vo.UnitVo;
import com.zjtc.model.vo.UseWaterPlanAddWXVO;
import com.zjtc.service.ContactsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/4/28
 */
@Api(tags = "用水单位联系人")
@RestController
@RequestMapping("contacts/")
@Slf4j
public class ContactsController {

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private ContactsService contactsService;

  @RequestMapping(value = "selectByMobileNumber", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("通过联系电话查询所在部门和该部门下所有的人员  --公共服务平台")
  public ApiResponse selectByMobileNumber(@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    System.out.println("登录的用户信息:"+user);
    String mobileNumber = null;
    if (null != user.getMobileNumber()){
      mobileNumber = user.getMobileNumber();
    }

    String personId = null;
    if (null != user.getId()){
      personId = user.getId();
    }
    String unitCode = user.getUnitCode();
    try {
      Map<String, Object> map = contactsService.selectByMobileNumber(mobileNumber,personId,unitCode);
      response.setData(map);
      return response;
    } catch (Exception e) {
      response.recordError("通过联系电话查询所在部门和该部门下所有的人员异常");
      log.error("通过联系电话查询所在部门和该部门下所有的人员异常,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "selectByMobileNumberWX", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("通过微信号查询所在部门和该部门下所有的人员  --微信公众号")
  public ApiResponse selectByMobileNumber(@RequestBody JSONObject jsonObject,
      @RequestHeader("openId") String openId,@RequestHeader("unitCode") String unitCode) {
    ApiResponse response = new ApiResponse();
    try {
      Map<String, Object> map = contactsService.selectByMobileNumberWX(openId,unitCode);
      response.setData(map);
      return response;
    } catch (Exception e) {
      response.recordError("通过微信号查询所在部门和该部门下所有的人员异常");
      log.error("通过微信号查询所在部门和该部门下所有的人员异常,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "selectOperatorPublic", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查询经办人-公共服务平台")
  public ApiResponse selectOperatorPublic(@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    System.out.println("登录的用户信息:"+user);
    String mobileNumber = null;
    if (StringUtils.isBlank( user.getMobileNumber())){
      response.setCode(200);
      return response;
    } else {
      mobileNumber = user.getMobileNumber();
    }
    try {
      List<UnitVo> operatorVos = contactsService
          .selectOperatorPublic(mobileNumber);
      response.setData(operatorVos);
      return response;
    } catch (Exception e) {
      response.recordError("查询经办人异常");
      log.error("查询经办人异常,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "selectOperatorWX", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查询经办人-微信")
  public ApiResponse selectOperatorWX(@RequestBody JSONObject jsonObject,
      @RequestHeader("openId") String openId) {
    ApiResponse response = new ApiResponse();
    try {
      List<UnitVo> operatorVos = contactsService.selectOperatorWX(openId);
      response.setData(operatorVos);
      return response;
    } catch (Exception e) {
      response.recordError("查询经办人异常");
      log.error("查询经办人异常,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
