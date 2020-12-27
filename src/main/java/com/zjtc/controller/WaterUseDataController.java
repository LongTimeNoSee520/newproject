package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.WaterUseDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/26
 */
@Api(tags = "水使用量数据")
@RestController
@RequestMapping("waterUseData/")
@Slf4j
public class WaterUseDataController {


  @Autowired
  private WaterUseDataService waterUseDataService;

  @Autowired
  private JWTUtil jwtUtil;

  @ResponseBody
  @ApiOperation(value = "查询可使用年份")
  @RequestMapping(value = "queryYear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exchange(@RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user){
      response.recordError("系统异常");
      return response;
    }
    try {
      List<Integer> list = waterUseDataService.queryYear(user.getNodeCode());
      if (list.isEmpty()){
        response.recordError("查询可使用年份失败");
        return response;
      }else {
        response.setData(list);
        response.setCode(200);
        return response;
      }
    } catch (Exception e) {
      log.error("查询可使用年份异常=="+e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
