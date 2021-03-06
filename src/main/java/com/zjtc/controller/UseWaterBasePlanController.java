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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
  public ApiResponse add(@RequestHeader("token") String token, @ApiParam("{\n"
      + "  \"oneQuarter\":\"第一季度计划(小数Float)\",\n"
      + "  \"twoQuarter\": \"第二季度计划(小数Float)\",\n"
      + "  \"threeQuarter\": \"第三季度计划(小数Float)\",\n"
      + "  \"fourQuarter\": \"第四季度计划(小数Float)\",\n"
      + "  \"curYearPlan\": \"当前年基础计划(小数Float)\",\n"
      + "  \"planYear\":\"计划年度 数字类型\",\n"
      + "  \"remarks\": \"备注\",\n"
      + "  \"nature\": \"用水性质\",\n"
      + "  \"unitCode\": \"单位编号\",\n"
      + "  \"unitName\": \"单位名称\",\n"
      + "  \"useWaterUnitId\": \"单位id\"\n"
      + "}")@RequestBody UseWaterBasePlan useWaterBasePlan) {
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
      @ApiParam("{ \n"
          + "  \"id\":\"基建计划id\",\n"
          + "  \"createTime\":\"创建时间\",\n"
          + "  \"oneQuarter\":\"第一季度计划(数字)\",\n"
          + "  \"twoQuarter\": \"第二季度计划(数字)\",\n"
          + "  \"threeQuarter\": \"第三季度计划(数字)\",\n"
          + "  \"fourQuarter\": \"第四季度计划(数字)\",\n"
          + "  \"curYearPlan\": \"当前年基础计划(数字)\",\n"
          + "  \"planYear\":\"计划年度 数字类型\",\n"
          + "  \"remarks\": \"备注\",\n"
          + "  \"nature\": \"用水性质\",\n"
          + "  \"unitCode\": \"单位编号\",\n"
          + "  \"unitName\": \"单位名称\",\n"
          + "  \"useWaterUnitId\": \"单位id\",\n"
          + "  \"deleted\": \"是否删除\",\n"
          + "  \"nodeCode\": \"节点编码\"\n"
          + "}")@RequestBody UseWaterBasePlan useWaterBasePlan) {
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
      response.recordError("基建计划id不能为空");
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
  @ApiOperation(value = "用水基建计划分页条件查询")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\"current\":\"当前页,数字类型\",\n"
          + "\"size\":\"每页条数,数字类型\",\n"
          + "\"unitCode\": \"单位编码\",\n"
          + "\"unitName\":\"单位名称\",\n"
          + "\"planYear\":\"查询年份,数字类型\",\n"
          + "\"nodeCode\":\"节点编码\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    log.debug("用水基建计划分页条件查询，参数param==={" + jsonObject.toString() + "}");
    try {
      User user =  jwtUtil.getUserByToken(token);
      Map<String, Object> result = useWaterBasePlanService.queryPage(user,jsonObject);
      response.setData(result);
    } catch (Exception e) {
      log.error("用水基建计划分页条件查询失败,errMsg==={}" + e.getMessage());
      response.recordError(500);
    }
    return response;
  }

  @RequestMapping(value = "export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "用水基建计划导出")
  public ApiResponse export(@RequestHeader("token") String token,
      @ApiParam("{\"unitCode\": \"单位编码\",\n"
          + "\"unitName\":\"单位名称\",\n"
          + "\"planYear\":\"查询年份,数字类型\",\n"
          + "\"nodeCode\":\"节点编码\"\n"
          + "}") @RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    log.debug("用水基建计划导出，参数param==={" + jsonObject.toString() + "}");
    try {
      User user =  jwtUtil.getUserByToken(token);
      apiResponse = useWaterBasePlanService.export(user,jsonObject,request,response);
    } catch (Exception e) {
      log.error("用水基建计划导出失败,errMsg==={}" + e.getMessage());
      apiResponse.recordError(500);
    }
    return apiResponse;
  }
}
