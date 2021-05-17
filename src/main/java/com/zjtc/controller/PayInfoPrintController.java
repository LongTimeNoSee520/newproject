package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.PayInfoPrintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * @date 2021/5/17
 * @description
 */

@Api(tags = "缴费打印记录api")
@RestController
@RequestMapping("payInfoPrint")
@Slf4j
public class PayInfoPrintController {
  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private PayInfoPrintService payInfoPrintService;

  @RequestMapping(value = "deleteBatch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "批量删除打印记录")
  public ApiResponse deleteBatch(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "ids:[\"要删除的打印记录集合\"]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("批量删除打印记录 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    if (null != jsonObject) {
      try {
        payInfoPrintService.deleteBatch(jsonObject);
      } catch (Exception e) {
        log.error("批量删除打印记录,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询")
  public ApiResponse queryByPayId(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"current\":\"当前页码\",\n"
          + "\"size\":\"当前页条数\",\n"
          + "\"unitCode\":\"单位编号\",\n"
          + "\"countYear\":\"年份\",\n"
          + "\"countQuarter\":\"季度\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user= jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      try {
        jsonObject.put("userId",user.getId());
        jsonObject.put("nodeCode",user.getNodeCode());
        Map<String,Object> result = payInfoPrintService.queryPage(jsonObject);
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("分页查询,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

}
