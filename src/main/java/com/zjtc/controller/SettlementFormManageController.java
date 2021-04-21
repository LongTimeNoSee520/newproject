package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.EndPaper;
import com.zjtc.model.User;
import com.zjtc.model.vo.SendListVO;
import com.zjtc.service.EndPaperService;
import com.zjtc.service.SystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lianghao
 * @date 2021/01/08
 */

@RestController
@RequestMapping("/formManage")
@Api(tags = "办结单管理")
@Slf4j
public class SettlementFormManageController {

  @Autowired
  private EndPaperService endPaperService;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private SystemLogService systemLogService;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\"current\":\"当前页,数字类型\",\n"
          + " \"size\":\"每页条数,数字类型\" ,\n"
          + " \"unitCode\":\"单位编号\",\n"
          + " \"unitName\":\"单位名称\",\n"
          + " \"executed\":\"是否已执行0否1是\",\n"
          + " \"waterMeterCode\":\"水表档案号\",\n"
          + " \"applyTimeStart\":\"申请时间起始\",\n"
          + " \"applyTimeEnd\":\"申请时间截止\",\n"
          + "\"nodeCode\":\"节点编码\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        Map<String, Object> result = new HashMap<>();
        result = endPaperService.queryPage(user, jsonObject);
        response.setData(result);
      } catch (Exception e) {
        e.printStackTrace();
        log.error("分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "updateFromWeChat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "微信端办结单确认后更新")
  public ApiResponse updateFromWeChat(@RequestHeader("openId") String openId,
      @ApiParam("EndPaper实体been") @RequestBody EndPaper endPaper) {
    log.info("办结单确认后更新 ==== 参数{" + endPaper.toString()+ "}");
    ApiResponse response = new ApiResponse();
    if (null != endPaper) {
      if (StringUtils.isBlank(openId)){
        response.recordError("系统错误");
        return response;
      }
      try {
        //User user = jwtUtil.getUserByToken(token);
         endPaperService.updateFromWeChat(endPaper);
      } catch (Exception e) {
        log.error("办结单确认后更新失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("参数为空或者解析失败");
    }
    return response;
  }
  @RequestMapping(value = "cancelSettlement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "撤销(审核中的办结单不能撤销)")
  public ApiResponse cancelSettlement(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"ids\":[\"办结单id列表\"]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("撤销 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
        if(ids.isEmpty()){
          response.recordError("撤销办结单id列表不能为空");
          return response;
        }
        response = endPaperService.cancelSettlement(ids);
        /**日志*/
        systemLogService.logInsert(user,"办结单管理","撤销",null);
      } catch (Exception e) {
        log.error("撤销失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("撤销参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "examineSettlement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "办结单审核")
  public ApiResponse examineSettlement(@RequestHeader("token") String token,
      @ApiParam("{\"id\":\"办结单id\",\n"
          + "\"firstQuarter\": \"一季度水量 数字(调整计划时才填)\",\n"
          + "\"secondQuarter\": \"二季度水量 数字(调整计划时才填)\",\n"
          + "\"thirdQuarter\": \"三季度水量 数字(调整计划时才填)\",\n"
          + "\"fourthQuarter\": \"四季度水量 数字(调整计划时才填)\",\n"
          + " \"firstWater\":\"第一水量,数字(增加计划时才填)\" ,\n"
          + " \"secondWater\":\"第二水量,数字(增加计划时才填)\",\n"
          + " \"addWay\":\"加计划的方式：1-平均，2-最高(增加计划时才填)\",\n"
          + " \"quarter\":\"调整季度(增加计划时才填)\",\n"
          + " \"year\":\"是否勾选年计划 0否，1是(增加计划时才填)\",\n"
          + " \"addNumber\":\"增加水量，数字(增加计划时才填)\",\n"
          + " \"auditBtn\":\"审核是否通过0否1是\",\n"
          + " \"opinions\":\"审核意见\",\n"
          + "\"auditorName\":\"下一环节审核人员名称\",\n"
          + "\"auditorId\":\"下一环节审核人员id\",\n"
          + "\"businessJson\":\"关联业务json数据(待办相关)\",\n"
          + "\"detailConfig\":\"详情配置文件(待办相关)\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("办结单审核 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
         endPaperService.examineSettlement(user, jsonObject);
      } catch (Exception e) {
        log.error("办结单审核失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("办结单审核参数不能为空");
    }
    return response;
  }
  @RequestMapping(value = "executeSettlement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "办结单执行")
  public ApiResponse executeSettlement(@RequestHeader("token") String token,
      @ApiParam("{\"id\":\"id\",\n"
          + "\"curYearPlan\":\"年计划,数字\",\n"
          + "\"firstQuarter\": 第一季度水量 数字,\n"
          + "\"secondQuarter\": 第二季度水量 数字,\n"
          + "\"thirdQuarter\": 第三季度水量 数字,\n"
          + "\"fourthQuarter\": 第四季度水量 数字\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("办结单执行 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        response =endPaperService.executeSettlement(user, jsonObject);
      } catch (Exception e) {
        log.error("办结单执行失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("办结单执行参数不能为空");
    }
    return response;
  }
  @ResponseBody
  @ApiOperation(value = "查询下一环节可提交审核的角色人员")
  @RequestMapping(value = "nextAuditRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse nextAuditRole(@RequestHeader("token") String token,
      @ApiParam("{\n"
      + "  \"id\":\"办结单id\",\n"
      + "  \"auditBtn\":\"审核是否通过，0：不通过，1:通过\"\n"
      + "  \n"
      + "}")
      @RequestBody JSONObject jsonObject) {
    log.info("查询 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && user != null) {
      try {
        List<Map<String, Object>> result = endPaperService
            .nextAuditRole(jsonObject.getString("id"), user.getNodeCode(),
                jsonObject.getString("auditBtn"));
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("查询失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }
  @RequestMapping(value = "sendInfoPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "计划调整结果发送列表信息查询")
  public ApiResponse sendInfoPage(@RequestHeader("token") String token,
      @ApiParam("{\"current\":\"当前页,数字类型\",\n"
          + " \"size\":\"每页条数,数字类型\" ,\n"
          + " \"unitCode\":\"单位编号\",\n"
          + " \"status\":\"短信发送状态\",\n"
          + "\"nodeCode\":\"节点编码\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        response.setData(endPaperService.sendInfoPage(user, jsonObject));
      } catch (Exception e) {
        log.error("分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "adjustResultNotification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "调整结果通知")
  public ApiResponse adjustResultNotification(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"sendList\":(发送列表) [{\n"
          + "\"id\": \"id\",\n"
          + "\"unitCode\": \"单位编号\",\n"
          + "\"unitName\": \"单位名称\",\n"
          + "\"mobileNumber\": \"手机号码\",\n"
          + "\"receiverName\": \"收件人\",\n"
//          + "\"paperType\": \"调整类型\",\n"
//          + "\"changeQuarter\": \"调整季度\",\n"
//          + "\"addNumber\": \"增加水量\",\n"
//          + "\"firstQuarter\": \"一季度水量\",\n"
//          + "\"secondQuarter\": \"二季度水量\",\n"
//          + "\"thirdQuarter\": \"三季度水量\",\n"
//          + "\"fourthQuarter\": \"四季度水量\",\n"
          + "}]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("调整结果通知 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    List<SendListVO> sendList = jsonObject.getJSONArray("sendList").toJavaList(SendListVO.class);
//    Integer year = jsonObject.getInteger("year");
    if (null != sendList && sendList.size()>0) {
      try {
        User user = jwtUtil.getUserByToken(token);
        endPaperService.adjustResultNotification(user, sendList);
      } catch (Exception e) {
        log.error("调整结果通知发送失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("年计划自平通知参数不能为空");
    }
    return response;
  }

  @RequestMapping(value = "printData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "查询打印数据(办结单打印)")
  public ApiResponse printData(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "    \"wxIds\":[//调整审核页面数据的id集合(调整审核页面数据的id打印时必传)\n"
          + "        \"id1\",\n"
          + "        \"id2\"\n"
          + "    ],\n"
          + "    \"ids\":[//办结单id集合(办结单页面打印时必传)\n"
          + "        \"id1\",\n"
          + "        \"id2\"\n"
          + "    ],\n"
          + "\"quarter\":\"当前季度 数字\"\n"
          + "\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询打印数据 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        //  User user = jwtUtil.getUserByToken(token);
        Map<String, Object> result = new HashMap<>();
        List<String> wxIds= jsonObject.getJSONArray("wxIds").toJavaList(String.class);
        List<String> ids= jsonObject.getJSONArray("ids").toJavaList(String.class);
        Integer quarter = jsonObject.getInteger("quarter");
        if(!wxIds.isEmpty() || !ids.isEmpty() ){
          result = endPaperService.printData(wxIds,ids,quarter);
          response.setData(result);
        }else {
          response.recordError("请选择要打印的单位");
          return  response;
        }
      } catch (Exception e) {
        e.printStackTrace();
        log.error("打印数据查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }
}
