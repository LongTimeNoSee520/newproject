package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterPlanAddService;
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
 * @Date: 2021/1/7
 */
@RestController
@RequestMapping("useWaterPlanAdd/")
@Api("用水计划调整审核")
@Slf4j
public class UseWaterPlanAddColtroller {

  @Autowired
  private UseWaterPlanAddService useWaterPlanAddService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("用水计划调整审核查询")
  public ApiResponse queryPage(@ApiParam("xx") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("用水计划调整审核分页查询,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("用水计划调整审核分页查询失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    try {
      response = useWaterPlanAddService.queryPage(jsonObject, user.getNodeCode(),user.getId());
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("用水计划调整审核分页查询异常");
      log.error("用水计划调整审核分页查询错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }


  @RequestMapping(value = "printed", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("打印办结单")
  public ApiResponse printed(@ApiParam("xx") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("打印办结单,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("打印办结单失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    try {
      response = useWaterPlanAddService.printed(ids);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("打印办结单异常");
      log.error("打印办结单错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
