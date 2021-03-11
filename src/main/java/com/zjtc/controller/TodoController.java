package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.QuotaInfo;
import com.zjtc.model.Todo;
import com.zjtc.model.User;
import com.zjtc.service.QuotaInfoService;
import com.zjtc.service.TodoService;
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
 * @author lianghao
 * @date 2021/03/08
 */

@Api(tags = "待办")
@RestController
@RequestMapping("/todo")
@Slf4j
public class TodoController {

  @Autowired
  private TodoService todoService;

  @Autowired
  private JWTUtil jwtUtil;

  @ApiOperation(value = "区级用水计划超额上报待办")
  @RequestMapping(value = "report", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse report(@RequestHeader("token") String token, @ApiParam("")@RequestBody Todo todo) {
    log.info("计划超额上报待办参数{" + todo != null ? todo.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != todo) {
      try {
        todoService.report(user, todo);
      } catch (Exception e) {
        log.error("计划超额上报待办失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
      response.recordError("参数不能为空");
    }
    return response;
  }

  @ApiOperation(value = "区级用水计划超额上报待办处理(同意/不同意)")
  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse edit(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"businessId\": \"待办的业务id\",\n"
          + "  \"pass\":\"是否同意，0不同意，1同意\"\n"
          + "}")@RequestBody JSONObject jsonObject) {
    log.info("用水计划超额上报待办处理==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        String businessId = jsonObject.getString("businessId");
        String pass = jsonObject.getString("pass");
        todoService.edit(user,businessId,pass);
      } catch (Exception e) {
        log.error("修改失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        response.recordError("修改失败");
      }
    } else {
      response.recordError(500);
      response.recordError("参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "queryList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "列表查询")
  public ApiResponse queryList(@RequestHeader("token") String token,
      @ApiParam("{}") @RequestBody JSONObject jsonObject) {
    log.info("列表查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        response.setData( todoService.queryList(user));
      } catch (Exception e) {
        log.error("列表查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }

}
