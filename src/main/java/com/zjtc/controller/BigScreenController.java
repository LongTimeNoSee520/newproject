package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.BigScreenService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuyantian
 * @date 2021/4/9
 * @description
 */
@Api(tags = "数字大屏")
@RestController
@RequestMapping("bigScreen/")
@Slf4j
public class BigScreenController {
  @Autowired
  JWTUtil jwtUtil;
  @Autowired
  BigScreenService bigScreenService;

  //查询中间地图区域标点
  @RequestMapping(value = "selectUnitMap", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查看中间地图区域标点")
  public ApiResponse selectUnitMap(
      @ApiParam("{"
          + "\"nodeCode\":\n\"选择区县,必填\",\n"
          + "\"unitName\":\n\"取水单位名称，支持关键字查询\"\n"
          + "\n"
          + "}")
      @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.info("查看中间地图区域标点,参数param==={" + jsonObject.toJSONString() + "}");
    if (user == null) {
      response.recordError("系统异常");
      return response;
    }
    try {
      List<Map<String ,Object>> banks = bigScreenService
          .selectUnitMap(jsonObject);
      response.setData(banks);
      return response;
    } catch (Exception e) {
      response.recordError("查看中间地图区域标点错误");
      log.error("查看中间地图区域标点错误,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
  //取水单位信息
  //查询中间地图区域标点
  @RequestMapping(value = "selectUnitById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查询取水单位信息")
  public ApiResponse selectUnitById(
      @ApiParam("{"
          + "\"nodeCode\":\n\"选择区县,必填\",\n"
          + "\"id\":\n\"单位id\",\n"
          + "\"year\":\n\"年份，必填\"\n"
          + "\n"
          + "}")
      @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.info("查询取水单位信息,参数param==={" + jsonObject.toJSONString() + "}");
    if (user == null) {
      response.recordError("系统异常");
      return response;
    }
    try {
      List<Map<String ,Object>> banks = bigScreenService
          .selectUnitById(jsonObject);
      response.setData(banks);
      return response;
    } catch (Exception e) {
      response.recordError("查询取水单位信息错误");
      log.error("查询取水单位信息错误,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
