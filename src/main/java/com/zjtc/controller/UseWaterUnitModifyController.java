package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterUnitModify;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitModifyService;
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
 * @Date: 2020/12/24
 */
@Api(tags = "用水单位名称修改日志")
@RestController
@RequestMapping("UseWaterUnitModify/")
@Slf4j
public class UseWaterUnitModifyController {


  @Autowired
  private UseWaterUnitModifyService unitModifyService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "selectUseWaterUnitModify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查询用水单位名称修改日志")
  public ApiResponse insertOrg( @ApiParam(
      "{\"id\":\n"
      + "\"当前单位id\"\n"
      + "\n"
      + "}")
      @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);

    try {
      List<UseWaterUnitModify> waterUnitModify = unitModifyService
          .selectUseWaterUnitModify(jsonObject.getString("id"), user.getNodeCode());
      response.setCode(200);
      response.setData(waterUnitModify);
      return response;
    } catch (Exception e) {
      response.setMessage("查询用水单位名称修改日志失败");
      response.setCode(500);
      log.error("查询用水单位名称修改日志失败,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}


