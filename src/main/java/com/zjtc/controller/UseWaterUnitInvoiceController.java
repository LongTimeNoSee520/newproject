package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterUnitInvoice;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterUnitInvoiceService;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

/**
 * UseWaterUnitInvoice的路由接口服务
 * @Author: ZhouDaBo
 * @Date: 2020/12/26
 */
@Api(tags = "发票管理")
@RestController
@RequestMapping("useWaterUnitInvoice/")
@Slf4j
public class UseWaterUnitInvoiceController {

  /**
   * UseWaterUnitInvoiceService服务
   */
  @Autowired
  private UseWaterUnitInvoiceService useWaterUnitInvoiceService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "saveModel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "新增发票管理")
  public ApiResponse saveModel(@ApiParam("{\n"
      + "    \"unitInvoices\":[\n"
      + "        {\n"
      + "            \"invoiceNumber\":\"发票号\"\n"
      + "        },\n"
      + "        {\n"
      + "            \"invoiceNumber\":\"发票号\"\n"
      + "        }\n"
      + "        \n"
      + "    ]\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("新增发票管理 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    try {
      response = useWaterUnitInvoiceService.saveModel(jsonObject, user.getNodeCode());
      return response;
    } catch (Exception e) {
      response.recordError("新增发票管理异常");
      e.printStackTrace();
    }
    return response;
  }

  /**
   * 通过id删除UseWaterUnitInvoice数据方法
   *
   * @param
   * @return
   */
  @ResponseBody
  @ApiOperation(value = "删除发票信息")
  @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse remove(@ApiParam(""
      + "{\n"
      + "    \"ids\":[\n"
      + "       \"id数据集\"\n"
      + "    ]\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("删除发票信息 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (ids.isEmpty()) {
      response.recordError("参数错误");
      return response;
    }
    try {
      response = useWaterUnitInvoiceService.deleteModel(ids);
      return response;
    } catch (Exception e) {
      e.printStackTrace();
      log.error("删除发票信息异常");
    }
    return response;
  }

  /**
   * 通过id作废UseWaterUnitInvoice数据方法
   *
   * @param
   * @return
   */
  @ResponseBody
  @ApiOperation(value = "作废发票信息")
  @RequestMapping(value = "abolish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse abolish(@ApiParam(""
      + "{\n"
      + "    \"ids\":[\n"
      + "       \"id数据集\"\n"
      + "    ]\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("作废发票信息 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (ids.isEmpty()) {
      response.recordError("参数错误");
      return response;
    }
    try {
      response = useWaterUnitInvoiceService.abolish(ids);
      return response;
    } catch (Exception e) {
      e.printStackTrace();
      log.error("作废发票信息异常");
    }
    return response;
  }

  /**
   * 通过id取消作废UseWaterUnitInvoice数据方法
   *
   * @param
   * @return
   */
  @ResponseBody
  @ApiOperation(value = "取消作废发票信息")
  @RequestMapping(value = "cancelAbolish", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse cancelAbolish(@ApiParam(""
      + "{\n"
      + "    \"ids\":[\n"
      + "       \"id数据集\"\n"
      + "    ]\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("取消作废发票信息 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (ids.isEmpty()) {
      response.recordError("参数错误");
      return response;
    }
    try {
      response = useWaterUnitInvoiceService.cancelAbolish(ids);
      return response;
    } catch (Exception e) {
      e.printStackTrace();
      log.error("取消作废发票信息异常");
    }
    return response;
  }

  /**
   * 插入UseWaterUnitInvoice属性不为空的数据方法
   *
   * @param
   * @return
   */
  @ResponseBody
  @ApiOperation(value = "开票登记")
  @RequestMapping(value = "updateModel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse updateModel(@ApiParam(""
      + "{\n"
      + "    \"id\":\"发票id\",\n"
      + "    \"invoiceDate\":\"十三位时间戳\",\n"
      + "    \"invoiceMoney\":\"double类型金额\",\n"
      + "    \"remarks\":\"备注\"\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("新增==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user) {
      response.recordError("系统异常");
      return response;
    }
    UseWaterUnitInvoice entity;
    if (null != jsonObject) {
      entity = jsonObject.toJavaObject(UseWaterUnitInvoice.class);
      if (null != entity) {
        response = useWaterUnitInvoiceService.updateModel(entity,user.getUsername());
        return response;
      }
      response.recordError("开票登记失败");
      return response;
    }
    response.recordError("开票登记失败");
    return response;
  }

  /**
   * 重置
   *
   * @param
   * @return
   */
  @ResponseBody
  @ApiOperation(value = "重置")
  @RequestMapping(value = "exchange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exchange(@RequestHeader("token") String token,
      @ApiParam(""
          + "{\n"
          + "    \"frontId\":\"当前发票id\",\n"
          + "    \"rearId\":\"交换发票id\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("重置==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      String frontId = jsonObject.getString("frontId");
      String rearId = jsonObject.getString("rearId");
      response = useWaterUnitInvoiceService.exchange(frontId, rearId, user.getNodeCode());
      return response;
    }
    response.recordError("重置异常");
    return response;
  }

  /**
   * 移交票段
   *
   * @param
   * @return
   */
  @ResponseBody
  @ApiOperation(value = "移交票段")
  @RequestMapping(value = "shift", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse shift(@RequestHeader("token") String token,
      @ApiParam(""
          + "{\n"
          + "    \"begin\":\"开始票段\",\n"
          + "    \"end\":\"结束票段\"\n"
          + "    \"personId\":\"移交人id\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("重置==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user) {
      response.recordError("系统异常");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("重置异常");
      return response;
    }
    String begin = jsonObject.getString("begin");
    String end = jsonObject.getString("end");
    String personId = jsonObject.getString("personId");
    response = useWaterUnitInvoiceService.shift(begin, end, personId, user.getId());
    return response;
  }

  /**
   * 移交票段
   *
   * @param
   * @return
   */
  @ResponseBody
  @ApiOperation(value = "发票标记")
  @RequestMapping(value = "sign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse sign(@RequestHeader("token") String token,
      @ApiParam(""
          + "{\n"
          + "    \"ids\":[\n"
          + "       \"id数据集\"\n"
          + "    ]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("重置==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user) {
      response.recordError("系统异常");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("重置异常");
      return response;
    }
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (ids.isEmpty()) {
      response.recordError("发票标记失败");
      return response;
    }
    response = useWaterUnitInvoiceService.sign(ids);
    return response;
  }

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("分页查询人员信息")
  public ApiResponse queryPage(@ApiParam(""
      + "{\n"
      + "    \"current\":\"页数\",\n"
      + "    \"size\":\"条数\",\n"
      + "    \"invoiceNumber\":\"发票号\",\n"
      + "    \"begin\":\"开始票段(Integer类型)\",\n"
      + "    \"end\":\"结束票段(Integer类型)\"\n"
      + "    \"enabled\":\"是否作废(0代表否)\",\n"
      + "    \"received\":\"是否领取(0代表否)\",\n"
      + "}\n") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("分页查询数据,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("人员分页查询失败");
      return response;
    }
    if (null == jsonObject){
      response.recordError("系统异常");
      return response;
    }
    try {
      response = useWaterUnitInvoiceService.queryPage(jsonObject, user.getNodeCode(),user.getLoginId());
      return response;
    } catch (Exception e) {
      response.recordError("分页查询人员信息异常");
      log.error("分页查询人员信息错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "selectInvoices", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("查询未被使用的发票编号")
  public ApiResponse selectInvoices( @RequestBody JSONObject jsonObject) {
    log.info("分页查询数据,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null == jsonObject){
      response.recordError("系统异常");
      return response;
    }
    try {
      List<Map<String, Object>> maps = useWaterUnitInvoiceService.selectInvoices();
      response.setData(maps);
      return response;
    } catch (Exception e) {
      response.recordError("分页查询人员信息异常");
      log.error("分页查询人员信息错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "updateInvoicesUnitMessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("更新发票的单位信息")
  public ApiResponse updateInvoicesUnitMessage( @ApiParam("{\n"
      + "\"id\":\"主键\",\n"
      + "\"payInfoId\":\"缴费记录id\",\n"
      + "\"invoiceMoney\":\"发票金额\",\n"
      + "\"invoiceUnitName\":\"发票单位名称\",\n"
      + "\"invoiceType\":\"0有效1作废\",\n"
      + "\"invoiceUnitCode\":\"发票单位编号\"\n"
      + "}")@RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("分页查询数据,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null == jsonObject){
      response.recordError("系统异常");
      return response;
    }
    User user = jwtUtil.getUserByToken(token);
    if (null == user){
      response.recordError("系统异常");
      return response;
    }
    UseWaterUnitInvoice unitInvoice = jsonObject.toJavaObject(UseWaterUnitInvoice.class);
    try {
      response = useWaterUnitInvoiceService.updateInvoicesUnitMessage(unitInvoice,user.getUsername());
      return response;
    } catch (Exception e) {
      response.recordError("更新发票的单位信息异常");
      log.error("分页查询人员信息错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
