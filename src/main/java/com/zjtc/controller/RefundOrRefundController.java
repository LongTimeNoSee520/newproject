package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.RefundOrRefundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefundOrRefund的路由接口服务
 *
 * @author zengqingsong
 */
@RestController
@RequestMapping("refundOrRefund")
@Api(tags = "退减免单管理")
@Slf4j
public class RefundOrRefundController {

  /**
   * RefundOrRefundService服务
   */
  @Autowired
  private RefundOrRefundService refundOrRefundService;
  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询所有内容")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "  \"current\":\"当前页\",\n"
      + "  \"size\":\"当前页显示数据条数\",\n"
      + "  \"keyWrod\":\"1:待审核(退减免单管理)界面，2：已审核界面，必填\",\n"
      + "  \"unitName\":\"单位名称\",\n"
      + "  \"unitCode\":\"单位编码\",\n"
      + "  \"waterMeterCode\":\"水表档案号\",\n"
      + "  \"year\":\"年度，必填\",\n"
      + "  \"type\":\"单据类型，1退款单，2减免单\",\n"
      + "  \"startTime\":\"申请开始时间\",\n"
      + "  \"endTime\":\"申请结束时间\"\n"
      + "  \n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    User user = jwtUtil.getUserByToken(token);
    log.info("查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    if (null != jsonObject && null != user) {
      try {
        String nodeCode = jsonObject.getString("nodeCode");
        if (StringUtils.isBlank(nodeCode)) {
          jsonObject.put("nodeCode", user.getNodeCode());
        }
        jsonObject.put("userId", user.getId());
        Map<String,Object> result = refundOrRefundService.queryPage(jsonObject);
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
   * 修改
   */
  @ResponseBody
  @ApiOperation(value = "保存")
  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse edit(@Param("{\n"
      + "  \"id\":\"退减免单id\",\n"
      + "  \"reason\":\"退款原因\",\n"
      + "  \"treatmentAdvice\":\"服务人员意见\",\n"
      + "  \"actualAmount\":\"实收金额，修改退款单实收金额时，需要改参数\",\n"
      + "  \"sysFiles\":\"[\"附件集合,删除附件，修改delete为1\"]\"\n"
      + "}") @RequestHeader("token") String token, @RequestBody JSONObject jsonObject) {
    log.info("修改==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      try {
        boolean result = refundOrRefundService.updateModel(user,jsonObject);
        if (result) {
          apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
        } else {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
      } catch (Exception e) {
        log.error("修改错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  /**
   * 通过id删除RefundOrRefund数据方法
   */
  @ResponseBody
  @ApiOperation(value = "审核")
  @RequestMapping(value = "audit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse audit(@ApiParam("{\n"
      + "  \"id\":\"退减免单id\",\n"
      + "  \"auditBtn\":\"审核是否通过，0：不通过，1:通过\n"
      + "  \"content\":\"处理意见\",\n"
      + "  \"nextPersonId\":\"下一环节处理人id\",\n"
      + "  \"nextPersonName\":\"下一环节处理人\",\n"
      + "\"businessJson\":\"关联业务json数据(待办相关)\",\n"
      + "\"detailConfig\":\"详情配置文件(待办相关)\"\n"
      + "  \n"
      + "}") @RequestHeader("token") String token,
      @RequestBody JSONObject jsonObject) {
    log.info("审核 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && user != null) {
      try {
        boolean result = refundOrRefundService.audit(jsonObject, user);
        if (result) {
          apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
        } else {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
      } catch (Exception e) {
        log.error("审核错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "查询下一环节可提交审核的角色人员")
  @RequestMapping(value = "nextAuditRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse nextAuditRole(@ApiParam("{\n"
      + "  \"id\":\"退减免单id\",\n"
      + "  \"auditBtn\":\"审核是否通过，0：不通过，1:通过\"\n"
      + "  \n"
      + "}") @RequestHeader("token") String token,
      @RequestBody JSONObject jsonObject) {
    log.info("查询 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && user != null) {
      try {
        List<Map<String, Object>> result = refundOrRefundService
            .nextAuditRole(jsonObject.getString("id"), user.getNodeCode(),
                jsonObject.getString("auditBtn"));
        apiResponse.setData(result);
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

  @ResponseBody
  @ApiOperation(value = "撤销")
  @RequestMapping(value = "revoke", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse revoke(@ApiParam("{\n"
      + "  \"ids\":[\"退减免单id数组\"]\n"
      + "}") @RequestHeader("token") String token,
      @RequestBody JSONObject jsonObject) {
    log.info("撤销 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      try {
        boolean result = refundOrRefundService
            .revoke(jsonObject,user);
        if (result) {
          apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
        } else {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
        apiResponse.setData(result);
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
}
