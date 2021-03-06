package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterUnitMonitor;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lianghao
 * @date 2021/01/15
 */

@RestController
@RequestMapping("/unitMonitor")
@Api(tags = "用水单位监控")
@Slf4j
public class UseWaterUnitMonitorController {

  @Autowired
  private UseWaterUnitMonitorService useWaterUnitMonitorService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "用水单位监控分页查询")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"current\":\"当前页,数字\", \n"
          + "  \"size\":\"每页条数,数字\" ,\n"
          + "  \"unitName\":\"单位名称\",\n"
          + "  \"unitCode\":\"单位编号\",\n"
          + "  \"unitCodeType\":\"用户类型\",\n"
          + "  \"year\":\"年份,数字\",\n"
          + "  \"industryId\":\"行业类型id\",\n"
          + "  \"mobileNumber\":\"手机号码\",\n"
          + "  \"contacts\":\"联系人\",\n"
          + "  \"monitorType\":\"监控类型(必填):1重点,2节水型\",\n"
          + "  \"nodeCode\":\"节点编码\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("分页查询用水单位监控信息 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
       // String monitorType = "1";//重点单位
        Map<String, Object> result = useWaterUnitMonitorService
            .queryPage(user, jsonObject);
        response.setData(result);
      } catch (Exception e) {
        log.error("用水单位分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }

  @ApiOperation(value = "新增")
  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse add(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"useWaterUnitId\": \"单位id\",\n"
          + "\"unitCode\": \"单位编号\",\n"
          + "\"unitName\": \"单位名称\",\n"
          + " \"unitCodeType\": \"用户类型\",\n"
          + "\"unitAddress\": \"单位地址\",\n"
          + "\"industryName\": \"所属行业\",\n"
          + "\"industry\": \"所属行业id\",\n"
          + "\"year\":\"年份，数字\",\n"
          + "\"monitorType\":\"监控类型(必填):1重点,2节水型\" \n"
          + "}") @RequestBody UseWaterUnitMonitor monitor) {
    log.info("用水单位监控新增参数{" + monitor != null ? monitor.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != monitor) {
      try {
        useWaterUnitMonitorService.add(user, monitor);
      } catch (Exception e) {
        log.error("用水单位监控新增失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
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
          + "\"ids\":[\"用水单位监控id列表\"]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("用水单位删除 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (null != ids && ids.size() > 0) {
      try {
        useWaterUnitMonitorService.delete(ids);
      } catch (Exception e) {
        log.error("删除失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        response.recordError(500);
      }
    } else {
      response.recordError("用水单位监控id列表不能为空");
    }
    return response;
  }

  @ApiOperation(value = "初始化下一年数据")
  @RequestMapping(value = "initNextYear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse initNextYear(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"monitorType\":\"监控类型(必填):1重点,2节水型\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("初始化下一年数据 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
      try {
        User user =jwtUtil.getUserByToken(token);
        String monitorType = jsonObject.getString("monitorType");
        if(StringUtils.isBlank(monitorType)){
          response.recordError("监控类型不能为空");
        }
        useWaterUnitMonitorService.initNextYear(user,monitorType);
      } catch (Exception e) {
        log.error("初始化下一年数据失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        response.recordError(500);
      }
    return response;
  }

  @RequestMapping(value = "export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "用水单位监控数据导出")
  public ApiResponse export(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"unitName\":\"单位名称\",\n"
          + "  \"unitCode\":\"单位编号\",\n"
          + "  \"unitCodeType\":\"用户类型\",\n"
          + "  \"year\":\"年份,数字\",\n"
          + "  \"industryId\":\"行业类型id\",\n"
          + "  \"monitorType\":\"监控类型(必填):1重点,2节水型\",\n"
          + "  \"nodeCode\":\"节点编码\"\n"
          + "}") @RequestBody JSONObject jsonObject,HttpServletRequest request,
      HttpServletResponse response) {
    log.info("用水单位监控数据导出 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        apiResponse = useWaterUnitMonitorService.export(user, jsonObject,request,response);
      } catch (Exception e) {
        log.error("用水单位监控数据导出失败,errMsg==={}" + e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError("用水单位监控数据导出参数不能为空");
    }
    return apiResponse;
  }
}
