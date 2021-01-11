package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterPlanAddWXService;
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
 * UseWaterPlanAdd的路由接口服务
 * 
 * @author Justin DaBo
 *
 */
@RestController
@RequestMapping("useWaterPlanAddWX/")
@Api(description = "用水计划调整申请服务")
@Slf4j
public class UseWaterPlanAddWXController {

  @Autowired
  private UseWaterPlanAddWXService useWaterPlanAddWXService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("用水计划调整审核查询")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "    \"current\":\"页数\",\n"
      + "    \"size\":\"条数\",\n"
      + "    \"unitName\":\"单位名称\",\n"
      + "    \"userType\":\"用户类型(截取的是3-4位)\",\n"
      + "    \"auditStatus\":\"审核状态(0:未审核,2:已审核)\",\n"
      + "    \"executed\":\"是否执行(0:未执行,1:已执行)\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("用水计划调整审核分页查询,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("用水计划调整审核分页查询失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    try {
      response = useWaterPlanAddWXService.queryPage(jsonObject, user.getNodeCode(),user.getId());
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
      response = useWaterPlanAddWXService.printed(ids);
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
  @ApiOperation("用水计划调整审核")
  public ApiResponse audit(@ApiParam("   {\n"
      + "     \"id\":\"审核id\",\"auditStatus\":\"审核状态(1:未通过,2:通过)\",\"auditResult\":\"审核意见\"\n"
      + "   }") @RequestBody JSONObject jsonObject,
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
//    {
//      "id":"1","auditStatus":"2","auditResult":"浪费水,洗漱的时候一直开着水龙头"
//    }
    String id = jsonObject.getString("id");
//    审核状态(0:为审核,1:审核不通过,2:审核通过)
    String auditStatus = jsonObject.getString("auditStatus");
    String auditResult = jsonObject.getString("auditResult");
    try {
      response = useWaterPlanAddWXService.audit(user.getId(),user.getUsername(),id,auditStatus,auditResult);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("用水计划调整审核异常");
      log.error("用水计划调整审核错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
