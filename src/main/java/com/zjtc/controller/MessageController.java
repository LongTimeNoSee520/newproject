package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Message;
import com.zjtc.model.User;
import com.zjtc.service.AlgorithmService;
import com.zjtc.service.MessageService;
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
 * @Author: yuchen
 * @Date: 2020/12/25
 */
@Api(tags = "通知api")
@RestController
@RequestMapping("message")
@Slf4j
public class MessageController {

  /**
   * AlgorithmService服务
   */
  @Autowired
  private AlgorithmService algorithmService;

  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private MessageService messageService;

  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "新增通知")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @RequestBody JSONObject jsonObject) {
    log.info("新增通知 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        Message message = jsonObject.toJavaObject(Message.class);
        messageService.save(message);
      } catch (Exception e) {
        log.error("新增通知,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @RequestMapping(value = "messageInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("通知查询")
  public ApiResponse messageInfo(@RequestHeader("token") String token) {
    log.info("查询,参数param==={" + token + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse response = new ApiResponse();
    try {
      List<Message> result = messageService.messageInfo(user.getId());
      response.setData(result);
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("用水量分页查询异常");
      log.error("查询错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "updateMsgStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("修改通知状态为已读")
  public ApiResponse updateMsgStatus(@ApiParam("{\n"
      + "  \"id\":\"通知id\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("修改,参数param==={" + jsonObject + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        boolean result = messageService.updateStatus(jsonObject.getString("id"));
        if (!result) {
          response.recordError(500);
        }
      } catch (Exception e) {
        response.recordError(500);
        log.error("修改错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
      }
    } else {
      response.recordError(500);
    }
    return response;
  }

  @RequestMapping(value = "updateMsgStatusAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("一键已读")
  public ApiResponse updateMsgStatusAll(
      @RequestHeader("token") String token) {
    log.info("修改,参数param==={" + token + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse response = new ApiResponse();
    if (null != token) {
      try {
        boolean result = messageService.updateMsgStatusAll(user.getId());
        if (!result) {
          response.recordError(500);
        }
      } catch (Exception e) {
        response.recordError(500);
        log.error("修改错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
      }
    } else {
      response.recordError(500);
    }
    return response;
  }

}
