package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitMeterService;
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
 * @Date: 2020/12/24 用水单位水表
 */
@Api(tags = "用水单位水表")
@RestController
@RequestMapping("UseWaterUnitMeter/")
@Slf4j
public class UseWaterUnitMeterController {

  @Autowired
  private UseWaterUnitMeterService unitMeterService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "selectUseWaterUnitMeter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查看用水单位水表")
  public ApiResponse deleteTemplate(
      @ApiParam("{\"id\":\n"
          + "\"当前单位id\"\n"
          + "\n"
          + "}")
      @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.info("删除部门,参数param==={" + jsonObject.toJSONString() + "}");
    if (user == null) {
      response.recordError("系统异常");
      return response;
    }
    try {
      List<UseWaterUnitMeter> useWaterUnitId = unitMeterService
          .selectUseWaterUnitMeter(jsonObject.getString("id"), user.getNodeCode());
      response.setCode(200);
      response.setData(useWaterUnitId);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("查看用水单位水表信息异常");
      log.error("查看用水单位水表信息失败,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "backFillData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("新增时回填水表信息")
  public ApiResponse selectUseWaterUnitMeterAll(
      @ApiParam("{\"useWaterUnitId\":\n"
          + "\"当前用水单位id\"\n"
          + "\n"
          + "}")
      @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.info("删除部门,参数param==={" + jsonObject.toJSONString() + "}");
    if (user == null) {
      response.recordError("系统异常");
      return response;
    }
    List<String> list = jsonObject.getJSONArray("waterMeterCodes").toJavaList(String.class);
    String useWaterUnitId = jsonObject.getString("useWaterUnitId");
    if (list.isEmpty()){
      response.recordError("系统异常");
      return response;
    }
    try {
      response = unitMeterService.selectUseWaterUnitMeterAll(list, user.getNodeCode(),useWaterUnitId);
      response.setCode(200);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("查看用水单位水表信息异常");
      log.error("查看用水单位水表信息失败,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
