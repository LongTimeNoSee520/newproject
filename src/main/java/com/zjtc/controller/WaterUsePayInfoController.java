package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.WaterUsePayInfoService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * WaterUsePayInfo的路由接口服务
 *
 * @author zengqingsong
 */
@Api(tags = "缴费管理")
@RestController
@RequestMapping("waterUsePayInfo")
@Slf4j
public class WaterUsePayInfoController {

  @Autowired
  private JWTUtil jwtUtil;

  /**
   * WaterUsePayInfoService服务
   */
  @Autowired
  private WaterUsePayInfoService waterUsePayInfoService;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询xxx内容")
  public ApiResponse queryPage(@RequestBody JSONObject jsonObject,
      @ApiParam("{\n"
          + "  \"nodeCode\":\"节点编码\",\n"
          + "  \"current\":\"当前页，必填\",\n"
          + "  \"size\":\"当前页数据条数，必填\",\n"
          + "  \"unitName\":\"单位名称\",\n"
          + "  \"unitCode\":\"单位编号\",\n"
          + "  \"countYear\":\"年，必填\",\n"
          + "  \"countQuarter\":\"季度\",\n"
          + "  \"payStatus\":\"缴费状态：0：未缴费，1已缴费\",\n"
          + "  \"actualAmount\":\"金额\",\n"
          + "  \"userType\":\"用户类型\",\n"
          + "  \"waterMeterCode\":\"水表档案号\",\n"
          + "  \"editedActual\":\"是否调整,0否，1是\",\n"
          + "  \"payType\":\"付款方式：2现金,3转账\"\n"
          + "}") @RequestHeader("token") String token) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        String nodeCode = jsonObject.getString("nodeCode");
        if (StringUtils.isBlank(nodeCode)) {
          jsonObject.put("nodeCode",user.getNodeCode());
        }
        jsonObject.put("userId", user.getId());
        Map<String, Object> result = waterUsePayInfoService.queryPage(jsonObject);
        apiResponse.setData(result);
        apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
      } catch (Exception e) {
        log.error("查询错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  /**
   * 插入WaterUsePayInfo属性不为空的数据方法
   */
  @ResponseBody
  @ApiOperation(value = "保存")
  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse add(@RequestHeader("token") String token, @ApiParam("{\n"
      + "\"payInfoList\": [{\n"
      + "\"id\": \"缴费记录id\",\n"
      + "\"entrusted\": \"是否需要托收，0否，1是\",\n"
      + "\"payType\": \"付款方式，默认0，2现金,3转账\",\n"
      + "\"invoiceId\": \"发票id\",\n"
      + "\"invoiceNum\": \"发票号\",\n"
      + "\"payStatus\": \"托收缴费状态，0不勾选，1勾选\",\n"
      + "\"cashCheck\": \"现金财务复核，0不勾选，1勾选\",\n"
      + "\"transferCheck\": \"转账财务复核，0不勾选，1勾选\",\n"
      + "\"printed\": \"是否打印,0否，1是\",\n"
      + "\"remarks\": \"备注\"\n"
      + "\n"
      + "}]\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("新增==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        boolean result = waterUsePayInfoService.updateModel(jsonObject, user);
        if (result) {
          apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
        } else {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
      } catch (Exception e) {
        log.error("新增错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }


  @ResponseBody
  @ApiOperation(value = "重算加价")
  @RequestMapping(value = "initPayInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse initPayInfo(@RequestHeader("token") String token, @ApiParam("{\n"
      + "  \"countYear\":\"年，必填\",\n"
      + "  \"countQuarter\":\"季度，必填\",\n"
      + "  \"unitIds\":[单位id数组 取字段useWaterUnitId的值]\n"
      + "  \n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("重算加价， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        boolean result = waterUsePayInfoService.initPayInfo(jsonObject);
        if (result) {
          apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
        } else {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
      } catch (Exception e) {
        log.error("重算加价错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "发起减免单")
  @RequestMapping(value = "toStartReduction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse toStartReduction(@RequestHeader("token") String token, @ApiParam("{\n"
      + "  \"payId\":\"缴费记录id\",\n"
      + "  \"useWaterUnitId\":\"单位id\",\n"
      + "  \"unitCode\":\"单位编号\",\n"
      + "  \"unitName\":\"单位名称\",\n"
      + "  \"waterMeterCode\":\"水表档案号\",\n"
      + "  \"year\":\"年\",\n"
      + "  \"quarter\":\"季度\",\n"
      + "  \"money\":\"减免金额\",\n"
      + "  \"reason\":\"减免原因\",\n"
      + "  \"treatmentAdvice\":\"服务人员意见\",\n"
      + "  \"nextPersonId\":\"下一环节审核人id\",\n"
      + "  \"nextPersonName\":\"下一环节审核人\",\n"
      + "\"businessJson\":\"关联业务json数据(待办相关)\",\n"
      + "\"detailConfig\":\"详情配置文件(待办相关)\",\n"
      + "  \"sysFiles\":\"[\"附件集合,删除附件，修改delete为1\"]\"\n"
      + "  \n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("发起减免单， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        ApiResponse result = waterUsePayInfoService.toStartReduction(jsonObject, user);
        if (result.getCode() != 200) {
          return result;
        }
      } catch (Exception e) {
        log.error("发起减免单,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "发起退款单")
  @RequestMapping(value = "toStartRefund", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse toStartRefund(@RequestHeader("token") String token, @ApiParam("{\n"
      + "    \"payId\":\"缴费记录id\",\n"
      + "    \"useWaterUnitId\":\"单位id\",\n"
      + "    \"unitCode\":\"单位编号\",\n"
      + "    \"unitName\":\"单位名称\",\n"
      + "    \"waterMeterCode\":\"水表档案号\",\n"
      + "    \"year\":\"年\",\n"
      + "    \"quarter\":\"季度\",\n"
      + "    \"actualAmount\":\"实收金额\",\n"
      + "    \"money\":\"退款金额\",\n"
      + "    \"reason\":\"退款原因\",\n"
      + "    \"treatmentAdvice\":\"服务人员意见\",\n"
      + "    \"nextPersonId\":\"下一环节审核人id\",\n"
      + "    \"nextPersonName\":\"下一环节审核人\",\n"
      + "    \"businessJson\":\"关联业务json数据(待办相关)\",\n"
      + "    \"detailConfig\":\"详情配置文件(待办相关)\",\n"
      + "  \"sysFiles\":\"[\"附件集合,删除附件，修改delete为1\"]\"\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("发起退款单， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        ApiResponse result = waterUsePayInfoService.toStartRefund(jsonObject, user);
        if (result.getCode() != 200) {
          return result;
        }
      } catch (Exception e) {
        log.error("发起退款单,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "查询退缴费第一个提交流程的角色信息")
  @RequestMapping(value = "firstRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse firstRole(@RequestHeader("token") String token) {
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user) {
      try {
        List<Map<String, Object>> result = waterUsePayInfoService.firstRole(user);
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("查询,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "查询近3年加价记录")
  @RequestMapping(value = "ThreePayMess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse ThreePayMess(@RequestHeader("token") String token, @ApiParam("{\n"
      + "  \"useWaterUnitId\":\"单位id\"\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        List<Map<String, Object>> result = waterUsePayInfoService
            .ThreePayMess(jsonObject.getString("useWaterUnitId"));
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("查询,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "查询当前单位所有未缴费记录")
  @RequestMapping(value = "findPayBefor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse findPayBefor(@RequestHeader("token") String token, @ApiParam("{\n"
      + "  \"useWaterUnitId\":\"单位id\"\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        List<Map<String, Object>> result = waterUsePayInfoService
            .findPayBefor(jsonObject.getString("useWaterUnitId"));
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("查询,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "催缴通知列表查询")
  @RequestMapping(value = "selectPayNotice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse selectPayNotice(@RequestHeader("token") String token, @ApiParam("{\n"
      + "  \"year\":\"年份,int\"\n"
      + "  \"unitCode\":\"单位编号 stirng\"\n"
      + "  \"status\":\"状态\"\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        jsonObject.put("userId",user.getId());
        jsonObject.put("nodeCode",user.getNodeCode());
        List<Map<String, Object>> result = waterUsePayInfoService
            .selectPayNotice(jsonObject);
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("查询,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出查询结果")
  @RequestMapping(value = "exportQueryData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exportQueryData(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "    \"unitName\":\"单位名称\",\n"
          + "    \"unitCode\":\"单位编号\",\n"
          + "    \"countYear\":\"年，必填\",\n"
          + "    \"countQuarter\":\"季度\",\n"
          + "    \"payStatus\":\"缴费状态：0：未缴费，1已缴费\",\n"
          + "    \"actualAmount\":\"金额\",\n"
          + "    \"userType\":\"用户类型\",\n"
          + "    \"waterMeterCode\":\"水表档案号\",\n"
          + "    \"editedActual\":\"是否调整,0否，1是\",\n"
          + "    \"payType\":\"付款方式：2现金,3转账\"\n"
          + "}")@RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    log.info("缴费管理：导出查询结果， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user && null != jsonObject) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        waterUsePayInfoService.exportQueryData(jsonObject, request, response);
      } catch (Exception e) {
        log.error("缴费管理：导出查询结果失败,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出用户信息")
  @RequestMapping(value = "exportUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exportUser(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"year\":\"年份，必填\",\n"
          + "  \"quarter\":\"季度\"\n"
          + "  \n"
          + "}")@RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    log.info("缴费管理：导出用户信息， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user && null != jsonObject) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        waterUsePayInfoService.exportUser(jsonObject, request, response);
      } catch (Exception e) {
        log.error("缴费管理：导出用户信息失败,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出计划用水户超计划情况汇总表")
  @RequestMapping(value = "exportPayInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exportPayInfo(@ApiParam("{\n"
      + "        \"year\":\"年份，必填\",\n"
      + "       \"quarter\":\"季度\"\n"
      + "}") @RequestHeader("token") String token,
      @RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    log.info(
        "缴费管理：导出计划用水户超计划情况汇总表， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user && null != jsonObject) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        waterUsePayInfoService.exportPayInfo(jsonObject, request, response);
      } catch (Exception e) {
        log.error("缴费管理：导出计划用水户超计划情况汇总表失败,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出本行托收数据")
  @RequestMapping(value = "exportBankInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exportBankInfo(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"year\":\"年份，必填\",\n"
          + "  \"quarter\":\"季度\"\n"
          + "  \n"
          + "}")@RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    log.info("缴费管理：导出本行托收数据， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user && null != jsonObject) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        ApiResponse result = waterUsePayInfoService.exportBankInfo(jsonObject, request, response);
        if (result.getCode() == 500) {
          return result;
        }
      } catch (Exception e) {
        log.error("缴费管理：导出本行托收数据失败,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出他行托收数据")
  @RequestMapping(value = "exportOtherBankInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exportOtherBankInfo(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"year\":\"年份，必填\",\n"
          + "  \"quarter\":\"季度\"\n"
          + "  \n"
          + "}")@RequestBody JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response) {
    log.info("缴费管理：导出他行托收数据， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user && null != jsonObject) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        ApiResponse result = waterUsePayInfoService
            .exportOtherBankInfo(jsonObject, request, response);
        if (500 == result.getCode()) {
          return result;
        }
      } catch (Exception e) {
        log.error("缴费管理：导出他行托收数据失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }
  @ResponseBody
  @ApiOperation(value = "短信发送")
  @RequestMapping(value = "send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse send(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"data\":[\n"
          + "{\n"
          + "\"id\": \"主键id\",\n"
          + "\"unitCode\": \"单位id\",\n"
          + "\"unitName\": \"单位名称\",\n"
          + "\"countQuarter\": \"季度\",\n"
          + "\"countYear\": \"年度\",\n"
          + "\"mobileNumber\": \"手机号码\",\n"
          + "\"contacts\": \"收件人\",\n"
          + "\"statusName\": \"状态\"\n"
          + "\"exceedWater\": \"超计划水量\"\n"
          + "\"statusName\": \"加价费\"\n"
          + "}\n"
          + "]\n"
          + "}")@RequestBody JSONObject jsonObject) {
    log.info("短信发送， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user && null != jsonObject) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        boolean result = waterUsePayInfoService
            .send(jsonObject);
        if (result) {
         return apiResponse;
        }
      } catch (Exception e) {
        log.error("短信发送,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }
}
