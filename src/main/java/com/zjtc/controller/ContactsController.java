package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.model.vo.OrgTreeVO;
import com.zjtc.model.vo.UseWaterPlanAddWXVO;
import com.zjtc.service.ContactsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
  @ApiOperation("通过联系电话查询所在部门和该部门下所有的人员")
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
      response.setCode(500);
      response.setMessage("通过联系电话查询所在部门和该部门下所有的人员");
      log.error("通过联系电话查询所在部门和该部门下所有的人员,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

}
