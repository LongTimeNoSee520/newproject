package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterBasePlan;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterBasePlanService;
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
 * @date 2020/12/24
 */

@Api(tags = "用水基建计划")
@RestController
@RequestMapping("/basePlan")
@Slf4j
public class UseWaterBasePlanController {

  @Autowired
  private UseWaterBasePlanService useWaterBasePlanService;

  @Autowired
  private JWTUtil jwtUtil;

  @ApiOperation(value = "新增")
  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse add(@RequestHeader("token") String token, @ApiParam("")@RequestBody UseWaterBasePlan useWaterBasePlan) {
    log.info("用水基建计划参数{" + useWaterBasePlan != null ? useWaterBasePlan.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);//通过token取节点编码
    if (null != useWaterBasePlan) {
      try {
      response = useWaterBasePlanService.add(user, useWaterBasePlan);
      } catch (Exception e) {
        log.error("用水基建计划新增失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
      response.recordError("参数不能为空");
    }
    return response;
  }

  @ApiOperation(value = "修改")
  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse edit(@RequestHeader("token") String token,
      @ApiParam("")@RequestBody UseWaterBasePlan useWaterBasePlan) {
    log.info("修改==== 参数{" + useWaterBasePlan != null ? useWaterBasePlan.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    if (null != useWaterBasePlan) {
      try {
        User user = jwtUtil.getUserByToken(token);//通过token取节点编码
        response = useWaterBasePlanService.edit(user,useWaterBasePlan);
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

  @ApiOperation(value = "删除")
  @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse remove(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"ids\":[\"用水基建计划id列表\"]\n"
          + "}")@RequestBody JSONObject jsonObject) {
    log.info("删除 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (null != ids && ids.size() > 0) {
      try {
        boolean result = useWaterBasePlanService.delete(ids);
        if (result) {
          response.setCode(200);
        } else {
          response.recordError(500);
        }
      } catch (Exception e) {
        log.error("删除失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        response.recordError(500);
      }
    } else {
      response.recordError("角色id不能为空");
    }
    return response;
  }

  @ApiOperation(value = "查询可选年份列表")
  @RequestMapping(value = "queryYear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse queryYear(@RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    try {
      User user = jwtUtil.getUserByToken(token);//通过token取节点编码
      List<Integer> years = useWaterBasePlanService.queryYear(user);
      response.setData(years);
      response.setCode(200);
    } catch (Exception e) {
      log.error("查询可选年份列表失败,errMsg==={}" + e.getMessage());
      response.recordError(500);
    }
    return response;
  }

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "短信分页条件查询")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\"current\":\"当前页\",\n"
          + "\"size\":\"每页条数\",\n"
          + "\"unitCode\": \"单位编码\",\n"
          + "\"unitName\":\"单位名称\",\n"
          + "\"planYear\":\"查询年份\",\n"
          + "}") @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    log.debug("短信分页条件查询，参数param==={" + jsonObject.toString() + "}");
    try {
      User user =  jwtUtil.getUserByToken(token);
      Map<String, Object> result = useWaterBasePlanService.queryPage(user,jsonObject);
      response.setData(result);
    } catch (Exception e) {
      log.error("短信分页条件查询失败,errMsg==={}" + e.getMessage());
      response.recordError(500);
    }
    return response;
  }
}
