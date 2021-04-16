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
import org.springframework.web.bind.annotation.ResponseBody;
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
          + "\"year\":\n\"年份必填\",\n"
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

  //查询左侧管理户数、计划用水量、实际用水量
  @RequestMapping(value = "selectLeftData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查询左侧管理户数、计划用水量、实际用水量")
  public ApiResponse selectLeftData(
      @ApiParam("{"
          + "\"nodeCode\":\n\"选择区县,必填，默认成都市\",\n"
          + "\"year\":\n\"年份，默认当前年\"\n"
          + "\n"
          + "}")
      @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.info("查询左侧管理户数、计划用水量、实际用水量,参数param==={" + jsonObject.toJSONString() + "}");
    if (user == null) {
      response.recordError("系统异常");
      return response;
    }
    try {
      List<Map<String ,Object>> banks = bigScreenService
          .selectLeftData(jsonObject);
      response.setData(banks);
      return response;
    } catch (Exception e) {
      response.recordError("查询左侧管理户数、计划用水量、实际用水量错误");
      log.error("查询左侧管理户数、计划用水量、实际用水量错误,errMsg==={}" + e.getMessage());
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
      Map<String ,Object> banks = bigScreenService
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

  @RequestMapping(value = "industryUseWater", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "各行业节约用水情况")
  public ApiResponse industryUseWater(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "    \"nodeCode\":\"节点编码\",\n"
          + "    \"year\":\"年份 数字\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("各行业节约用水情况查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        // User user = jwtUtil.getUserByToken(token);
        response.setData(bigScreenService.queryList(jsonObject));
      } catch (Exception e) {
        log.error("各行业节约用水情况查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("各行业节约用水情况查询参数不能为空");
    }
    return response;
  }
  @RequestMapping(value = "industryRank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "各行业用水排名")
  public ApiResponse industryRank(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "    \"nodeCode\":\"节点编码\",\n"
          + "    \"year\":\"年份 数字\",\n"
          + "    \"id\":\"行业id\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("各行业用水排名数据查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        // User user = jwtUtil.getUserByToken(token);
        response.setData(bigScreenService.queryRankData(jsonObject));
      } catch (Exception e) {
        log.error("各行业用水排名数据查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("各行业用水排名数据查询参数不能为空");
    }
    return response;
  }
  @RequestMapping(value = "importantMonitorData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "重点用水单位监控数据")
  public ApiResponse importantMonitorData(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "    \"nodeCode\":\"节点编码\",\n"
          + "    \"year\":\"年份 数字\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("重点用水单位监控数据查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        // User user = jwtUtil.getUserByToken(token);
        response.setData(bigScreenService.importantMonitorData(jsonObject));
      } catch (Exception e) {
        log.error("重点用水单位监控数据查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("重点用水单位监控数据查询参数不能为空");
    }
    return response;
  }


  @ResponseBody
  @ApiOperation(value = "用水情况分析")
  @RequestMapping(value = "selectWaterUseAnalyse", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse selectWaterUseAnalyse(
      @ApiParam("{\n"
          + "\"nodeCode\":\"节点编码\"\n"
          + "\"year\":\"年度\"\n"
          + "}")
      @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    String nodeCode = null;
    if (null != jsonObject.getString("nodeCode")){
      nodeCode = jsonObject.getString("nodeCode");
    }
    Integer year = null;
    if (null != jsonObject.getInteger("year")){
      year = jsonObject.getInteger("year");
    }
    try {
      response  = bigScreenService.selectWaterUseAnalyse(nodeCode, year);
      return response;
    } catch (Exception e) {
      log.error("查询用水情况分析异常==" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

//  @ResponseBody
//  @ApiOperation(value = "业务申请")
//  @RequestMapping(value = "businessApply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//  public ApiResponse businessApply(
//      @ApiParam("{\n"
//          + "\"nodeCode\":\"节点编码\"\n"
//          + "\"year\":\"年度\"\n"
//          + "}")
//      @RequestBody JSONObject jsonObject) {
//    ApiResponse response = new ApiResponse();
//    String nodeCode = null;
//    if (null != jsonObject.getString("nodeCode")){
//      nodeCode = jsonObject.getString("nodeCode");
//    }
//    Integer year = null;
//    if (null != jsonObject.getInteger("year")){
//      year = jsonObject.getInteger("year");
//    }
//    try {
//      response  = bigScreenService.businessApply(nodeCode, year);
//      return response;
//    } catch (Exception e) {
//      log.error("查询业务申请异常==" + e.getMessage());
//      e.printStackTrace();
//    }
//    return response;
//  }

  @ResponseBody
  @ApiOperation(value = "查询业务申请+业务办理")
  @RequestMapping(value = "businessTransaction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse businessTransaction(
      @ApiParam("{\n"
          + "\"nodeCode\":\"节点编码\"\n"
          + "\"year\":\"年度\"\n"
          + "}")
      @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    String nodeCode = null;
    if (null != jsonObject.getString("nodeCode")){
      nodeCode = jsonObject.getString("nodeCode");
    }
    Integer year = null;
    if (null != jsonObject.getInteger("year")){
      year = jsonObject.getInteger("year");
    }
    try {
      response  = bigScreenService.businessTransaction(nodeCode, year);
      return response;
    } catch (Exception e) {
      log.error("查询业务办理异常==" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @ResponseBody
  @ApiOperation(value = "数据来源")
  @RequestMapping(value = "dataSources", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse dataSources(
      @ApiParam("{\n"
          + "\"nodeCode\":\"节点编码\"\n"
          + "\"year\":\"年度\"\n"
          + "}")
      @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    String nodeCode = null;
    if (null != jsonObject.getString("nodeCode")){
      nodeCode = jsonObject.getString("nodeCode");
    }
    Integer year = null;
    if (null != jsonObject.getInteger("year")){
      year = jsonObject.getInteger("year");
    }
    try {
      response  = bigScreenService.dataSources(nodeCode, year);
      return response;
    } catch (Exception e) {
      log.error("查询数据来源异常==" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
