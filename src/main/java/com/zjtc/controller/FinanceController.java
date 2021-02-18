package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Finance;
import com.zjtc.model.User;
import com.zjtc.service.FinanceService;
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
@Api(tags = "加价费管理")
@RestController
@RequestMapping("finance/")
@Slf4j
public class FinanceController {

  @Autowired
  private FinanceService financeService;

  @Autowired
  private JWTUtil jwtUtil;


  @RequestMapping(value = "insertFinance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("新增加价费管理")
  public ApiResponse insertPerson(@ApiParam("{\n"
      + "    \"finance\":[\n"
      + "        {\n"
      + "            \"unitName\":\"单位名称\",\n"
      + "            \"money\":\"缴费金额(float类型)\",\n"
      + "            \"paymentDate\":\"十三位时间戳:入账时间\",\n"
      + "            \"invoiceState\":\"开票状态(0:否,1:是)\",\n"
      + "            \"drawer\":\"开票人\",\n"
      + "            \"unitCode\":\"单位编号\"\n"
      + "        }\n"
      + "    ]\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    log.info("新增加价费开票记录,参数param==={" + jsonObject.toJSONString() + "}");
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.recordError("系统错误");
      return response;
    }
    List<Finance> finances;
    try {
      finances = jsonObject.getJSONArray("finance").toJavaList(Finance.class);
      response = financeService.insertFinance(finances, user.getNodeCode());
      return response;
    } catch (Exception e) {
      response.setMessage("新增加价费开票记录失败");
      response.setCode(500);
      log.error("新增加价费开票记录失败,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "deletedFinance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "删除单位")
  public ApiResponse deletedFinance(
      @ApiParam("{\"ids\":[\"单位id集\"]}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    log.debug("删除单位失败，参数param==={" + jsonObject.toString() + "}");
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (ids.size() == 0) {
      response.recordError("系统错误");
      return response;
    }
    try {
      response = financeService.deletedFinance(ids);
      return response;
    } catch (Exception e) {
      log.error("删除单位失败,errMsg==={}" + e.getMessage());
      response.recordError(500);
    }
    return response;
  }


  @RequestMapping(value = "updateFinance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("修改加价费开票记录")
  public ApiResponse updateFinance(@ApiParam("{\n"
      + "        {\n"
      + "            \"id\":\"1\",\n"
      + "            \"unitName\":\"单位名称\",\n"
      + "            \"money\":\"缴费金额\",\n"
      + "            \"paymentDate\":\"十三位时间戳:入账时间\",\n"
      + "            \"invoiceState\":\"开票状态(0:否,1:是)\",\n"
      + "            \"drawerId\":\"开票人Id\",\n"
      + "            \"drawer\":\"开票人\",\n"
      + "            \"unitCode\":\"单位编号\"\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    log.info("修改加价费开票记录,参数param==={" + jsonObject.toJSONString() + "}");
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.recordError("系统错误");
      return response;
    }
    Finance finance;
    try {
      finance = jsonObject.toJavaObject(Finance.class);
      response = financeService.updateFinance(finance);
      return response;
    } catch (Exception e) {
      response.setMessage("修改加价费开票记录失败");
      response.setCode(500);
      log.error("修改加价费开票记录失败,errMsg==={}" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("加价费用管理分页查询")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "    \"current\":\"页数\",\n"
      + "    \"size\":\"条数\",\n"
      + "    \"unitName\":\"部门名称\",\n"
      + "    \"paymentDateBegin\":\"十三位时间戳:到账时间开始\",\n"
      + "    \"paymentDateFinish\":\"十三位时间戳:到账时间结束\",\n"
      + "    \"money\":\"金额(float类型)\",\n"
      + "    \"invoiceState\":\"是否开票\",\n"
      + "    \"drawer\":\"开票人\"\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("分页查询数据,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("加价费用管理分页查询失败");
      return response;
    }
    try {
      response = financeService.queryPageFinance(jsonObject, user.getNodeCode());
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("加价费用管理分页查询异常");
      log.error("加价费用管理分页查询错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }


}
