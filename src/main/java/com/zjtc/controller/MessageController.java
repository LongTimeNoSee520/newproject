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
}
