package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Bank;
import com.zjtc.model.User;
import com.zjtc.service.BankService;
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
 * @Author: ZhouDaBo
 * @Date: 2020/12/24
 */
@Api(tags = "银行信息")
@RestController
@RequestMapping("bank/")
@Slf4j
public class BankController {

  @Autowired
  private BankService bankService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "selectBank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查看银行信息")
  public ApiResponse deleteTemplate(
      @ApiParam("{\"id\":\n"
          + "\"当前单位id\"\n"
          + "\n"
          + "}")
      @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.info("查看银行信息,参数param==={" + jsonObject.toJSONString() + "}");
    if (user == null) {
      response.recordError("系统异常");
      return response;
    }
    try {
      List<Bank> banks = bankService
          .selectBank(jsonObject.getString("id"), user.getNodeCode());
      response.setCode(200);
      response.setData(banks);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("查看银行信息异常");
      log.error("查看银行信息失败,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
