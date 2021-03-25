package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/2/18
 */
@Api(tags = "用户权限信息管理")
@RestController
@RequestMapping("useWaterUnitRole/")
@Slf4j
public class UseWaterUnitRoleController {


  @Autowired
  private UseWaterUnitRoleService useWaterUnitRoleService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "selectUserRelevanceRoleMessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "查询人员信息和关联权限模块类型")
  public ApiResponse saveModel( @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("查询人员信息和关联权限模块类型 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user){
      response.recordError("系统异常");
      return response;
    }
    try {
      response = useWaterUnitRoleService.selectUserRelevanceRoleMessage(user.getNodeCode());
      return response;
    } catch (Exception e) {
      response.recordError("查询人员信息和关联权限模块类型异常");
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "selectByIdUnitTypeCodeAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "查询所有操作批次")
  public ApiResponse selectByIdUnitTypeCodeAll(@ApiParam("{\n"
      + "\"personId\":\"人员id\",\n"
      + "\"nodeCode\":\"节点编码\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("查询所有操作批次 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user){
      response.recordError("系统异常");
      return response;
    }
    String personId = jsonObject.getString("personId");
    String nodeCode = jsonObject.getString("nodeCode");
    try {
      response = useWaterUnitRoleService.selectByIdUnitTypeCodeAll(personId,nodeCode);
      return response;
    } catch (Exception e) {
      response.recordError("查询人员信息和关联权限模块类型异常");
      e.printStackTrace();
    }
    return response;
  }


  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "确认授权")
  public ApiResponse add(@ApiParam("{\n"
      + "\"personId\":\"人员id\",\n"
      + "\"nodeCode\":\"节点编码\",\n"
      + "\"unitTypeCodes\":[\"批次号\"]\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("确认授权 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user){
      response.recordError("系统异常");
      return response;
    }
    String personId = jsonObject.getString("personId");
    String nodeCode = jsonObject.getString("nodeCode");
    List<String> unitTypeCodes = jsonObject.getJSONArray("unitTypeCodes").toJavaList(String.class);
    try {
      response = useWaterUnitRoleService.add(personId,nodeCode,unitTypeCodes);
      return response;
    } catch (Exception e) {
      response.recordError("确认授权异常");
      e.printStackTrace();
    }
    return response;
  }

}
