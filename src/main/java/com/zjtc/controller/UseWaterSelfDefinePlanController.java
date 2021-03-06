package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterSelfDefinePlanService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TWUseWaterSelfDefinePlan的路由接口服务
 *
 * @author DaBo
 */
@RestController
@RequestMapping("tWUseWaterSelfDefinePlan/")
@Api(tags = "自平计划管理")
@Slf4j
public class UseWaterSelfDefinePlanController {

  /**
   * TWUseWaterSelfDefinePlanService服务
   */
  @Autowired
  private UseWaterSelfDefinePlanService tWUseWaterSelfDefinePlanService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("自平计划管理分页查询")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "    \"current\":\"页数(必填)\",\n"
      + "    \"size\":\"条数(必填)\",\n"
      + "    \"nodeCode\":\"节点编码(必填)\",\n"
      + "    \"unitName\":\"单位名称\",\n"
      + "    \"userType\":\"用户类型(截取的是3-4位)\",\n"
      + "    \"areaCode\":\"编号开头\",\n"
      + "    \"planYear \":\"自平年份\",\n"
      + "    \"auditStatus\":\"是否审核(0:未审核,2:已审核)\",\n"
      + "    \"executed\":\"是否执行(0:未执行,1:已执行)\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("自平计划管理分页查询,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("自平计划管理分页查询失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    String nodeCode;
    try {
      if (null != jsonObject.getString("nodeCode")) {
         nodeCode  = jsonObject.getString("nodeCode");
        response = tWUseWaterSelfDefinePlanService.queryPage(jsonObject, nodeCode, user.getId());
      }else{
        response = tWUseWaterSelfDefinePlanService.queryPage(jsonObject, user.getNodeCode(), user.getId());
      }
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("自平计划管理分页查询异常");
      log.error("自平计划管理分页查询错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @ResponseBody
  @ApiOperation(value = "审核自平申请")
  @RequestMapping(value = "audit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse audit(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + " \"id\":\"审核id\",\"auditStatus\":\"审核结果(1:不同意,2:同意)\",\"auditResult\":\"原因\"\n"
          + "}")
      @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user) {
      response.recordError("系统异常");
      return response;
    }
//    自平id
    String id = jsonObject.getString("id");
//    审核状态(1:未通过,2:通过)
    String auditStatus = "";
    if (null != jsonObject.getString("auditStatus")) {
      auditStatus = jsonObject.getString("auditStatus");
    }
//    审核结果(理由)
    String auditResult = "";
    if (null != jsonObject.getString("auditResult")) {
      auditResult = jsonObject.getString("auditResult");
    }
    try {
      response = tWUseWaterSelfDefinePlanService
          .audit(user,id, user.getUsername(), user.getId(),auditStatus, auditResult);
      return response;
    } catch (Exception e) {
      log.error("审核自平申请异常==" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @ResponseBody
  @ApiOperation(value = "执行自平申请")
  @RequestMapping(value = "execute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse execute(@RequestHeader("token") String token,
      @ApiParam("   {\n"
          + "     \"ids\":[\"id\"]\n"
          + "   }")
      @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user) {
      response.recordError("系统异常");
      return response;
    }
//  执行的数据id
    List<String> list = jsonObject.getJSONArray("ids").toJavaList(String.class);
    try {
      response = tWUseWaterSelfDefinePlanService.execute(user,list, user.getUsername(), user.getId(),user.getNodeCode());
      return response;
    } catch (Exception e) {
      log.error("执行自平申请异常==" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
