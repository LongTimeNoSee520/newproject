package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.base.util.StringUtil;
import com.zjtc.model.Dict;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties.Jwt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Api(tags = "用水单位管理")
@RestController
@RequestMapping("userWaterUnit/")
@Slf4j
public class UseWaterUnitController {

  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private UseWaterUnitService useWaterUnitService;

  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "用水单位新增")
  public ApiResponse save(@RequestBody UseWaterUnit useWaterUnit,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位新增，参数param==={" + useWaterUnit.toString() + "}");
    if (null != useWaterUnit && null != user) {
      ApiResponse result = useWaterUnitService.save(useWaterUnit, user);
      if (500 == result.getCode()) {
        return result;
      } else {
        return response;
      }
    } else {
      response.recordError(500);
    }

    return response;
  }

  @RequestMapping(value = "unitCodeValidate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "用水单位编号是否重复验证")
  public ApiResponse unitCodeValidate(@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位新增，参数param==={" + jsonObject.toString() + "}");
    if (null != jsonObject && null != user) {
      String result = useWaterUnitService
          .unitCodeValidate(jsonObject.getString("id"), user.getNodeCode());
      if (StringUtils.isNotBlank(result)) {
        response.setData(result);
      } else {
        response.recordError(500);
      }
    } else {
      response.recordError(500);
    }

    return response;
  }

}
