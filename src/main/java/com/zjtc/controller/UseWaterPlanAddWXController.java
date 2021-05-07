package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.User;
import com.zjtc.model.vo.UseWaterPlanAddWXVO;
import com.zjtc.service.FlowNodeInfoService;
import com.zjtc.service.FlowProcessService;
import com.zjtc.service.UseWaterPlanAddWXService;
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
 * UseWaterPlanAdd的路由接口服务
 *
 * @author Justin DaBo
 */
@RestController
@RequestMapping("useWaterPlanAddWX/")
@Api(tags = "用水计划调整审核")
@Slf4j
public class UseWaterPlanAddWXController {

  @Autowired
  private UseWaterPlanAddWXService useWaterPlanAddWXService;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private FlowNodeInfoService flowNodeInfoService;

  @Autowired
  private FlowProcessService flowProcessService;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("用水计划调整审核查询")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "    \"current\":\"页数(必填)\",\n"
      + "    \"size\":\"条数(必填)\",\n"
      + "    \"nodeCode\":\"节点编码(必填)\",\n"
      + "    \"unitName\":\"单位名称\",\n"
      + "    \"userType\":\"用户类型(截取的是5-6位)\",\n"
      + "    \"auditStatus\":\"审核状态(0:未审核,2:已审核)\",\n"
      + "    \"executed\":\"是否执行(0:未执行,1:已执行)\"\n"
      + "    \"unitCode\":\"单位编号\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("用水计划调整审核分页查询,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.recordError("用水计划调整审核分页查询失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    try {
      String nodeCode = null != jsonObject.getString("nodeCode") ? jsonObject.getString("nodeCode")
          : user.getNodeCode();
      response = useWaterPlanAddWXService.queryPage(jsonObject, nodeCode, user.getId());
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
  public ApiResponse printed(@ApiParam("   {\n"
      + "     \"ids\":[\"打印的id\"]\n"
      + "   }") @RequestBody JSONObject jsonObject,
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
      response = useWaterPlanAddWXService.printed(ids, user);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("打印办结单异常");
      log.error("打印办结单错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "audit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("用水计划增加或调整审核")
  public ApiResponse audit(@ApiParam("{\n"
      + "    \"id\":\"主键\",\n"
      + "    \"auditStatus\":\"审核状态(1:不通过,2:通过)\",\n"
      + "    \"auditResult\":\"审核结果\",\n"
      + "    \"firstWater\":\"第一水量\",\n"
      + "    \"secondWater\":\"第二水量\",\n"
      + "    \"auditorName\":\"下一环节审核人\",\n"
      + "    \"auditorId\":\"下一环节Role审核人id\",\n"
      + "    \"nextNodeId\":\"下一审核环节id\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("用水计划调整审核,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("用水计划调整审核失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    String id = jsonObject.getString("id");
//    审核状态(0:为审核,1:审核不通过,2:审核通过)
    String auditStatus = jsonObject.getString("auditStatus");
//    审核结果
    String auditResult = jsonObject.getString("auditResult");
//    第一水量
    Double firstWater = jsonObject.getDouble("firstWater");
//    第二水量
    Double secondWater = jsonObject.getDouble("secondWater");
//    下一环节审核人
    String auditorName = jsonObject.getString("auditorName");
//    下一环节审核人id
    String auditorId = jsonObject.getString("auditorId");
//    关联业务json数据(待办相关)
    String businessJson = jsonObject.getString("businessJson");
//    详情配置文件(待办相关)
    String detailConfig = jsonObject.getString("detailConfig");
//    下一审核环节id
    String nextNodeId = jsonObject.getString("nextNodeId");
    try {
      response = useWaterPlanAddWXService
          .audit(id, auditStatus, auditResult, firstWater,
              secondWater, user, auditorName, auditorId, businessJson, detailConfig, nextNodeId);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("用水计划调整审核异常");
      log.error("用水计划调整审核错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }


  @RequestMapping(value = "firstAuditRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查询第一个审核流程id")
  public ApiResponse firstAuditRole(@ApiParam("   {\n"
      + "     \"flowCode\":\"流程编码\n"
      + "   }") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("查询第一个审核流程,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("查询第一个审核流程失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    String flowCode = null;
    if (null != jsonObject.getString("flowCode")) {
      flowCode = jsonObject.getString("flowCode");
    }
    try {
      List<Map<String, Object>> list = flowProcessService
          .firstAuditRole(flowCode, user.getNodeCode());
      response.setData(list);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("查询第一个审核流程异常");
      log.error("查询第一个审核流程错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "selectNowPlan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("用水计划调整 查看当前计划")
  public ApiResponse selectNowPlan(@ApiParam("   {\n"
      + "     \"id\":\"用水计划调整审核id\"\n"
      + "   }") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("用水计划调整 查看当前计划,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();

    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    String id = jsonObject.getString("id");
    try {
      UseWaterPlanAddWXVO useWaterPlanAddWXVO = useWaterPlanAddWXService.selectNowPlan(id);
      response.setData(useWaterPlanAddWXVO);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("用水计划调整 查看当前计划异常");
      log.error("用水计划调整 查看当前计划错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
