package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.WaterUnitAssessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * @Date: 2021/1/18
 */
@RestController
@RequestMapping("waterUnitAssess/")
@Api(tags = "用水单位考核")
@Slf4j
public class WaterUnitAssessController {

  @Autowired
  private WaterUnitAssessService waterUnitAssessService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("用水单位考核查询")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "    \"current\":\"页数\",\n"
      + "    \"size\":\"条数\",\n"
      + "    \"unitName\":\"单位名称\",\n"
      + "    \"accessYear\":\"年度(Integer)\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("用水单位考核分页查询,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("用水单位考核分页查询失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    try {
      response = waterUnitAssessService.queryPage(jsonObject, user.getNodeCode(), user.getId());
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("用水单位考核分页查询异常");
      log.error("用水单位考核分页查询错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }


  /**
   * 导出
   */
  @ResponseBody
  @ApiOperation(value = "导出用水单位考核")
  @RequestMapping(value = "export", method = RequestMethod.POST)
  public ApiResponse excelExport(@ApiParam(""
      + "{\n"
      + "\"unitName\":\"单位名称\",\n"
      + "\"accessYear\":\"年度\"\n"
      + "}\n")@RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response, @RequestHeader("token") String token) {
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user) {
      apiResponse.recordError("系统异常");
      return apiResponse;
    }
    if (null == jsonObject) {
      apiResponse.recordError("系统异常");
      return apiResponse;
    }
    try {
      waterUnitAssessService.export(jsonObject, request, response, user);
    } catch (Exception e) {
      apiResponse.recordError("系统异常:" + e.getMessage());
    }
    return apiResponse;
  }
}
