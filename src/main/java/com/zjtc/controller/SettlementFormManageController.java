package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.EndPaper;
import com.zjtc.model.User;
import com.zjtc.service.EndPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
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
          + " \"applyTimeEnd\":\"申请时间截止\"\n"
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
  public ApiResponse updateFromWeChat(@RequestHeader("token") String token,
      @ApiParam("EndPaper实体been") @RequestBody EndPaper endPaper) {
    log.info("分页查询 ==== 参数{" + endPaper.toString()+ "}");
    ApiResponse response = new ApiResponse();
    if (null != endPaper) {
      try {
        //User user = jwtUtil.getUserByToken(token);
         endPaperService.updateFromWeChat(endPaper);
      } catch (Exception e) {
        log.error("分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("参数为空或者解析失败");
    }
    return response;
  }
  @RequestMapping(value = "cancelSettlement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "撤销")
  public ApiResponse cancelSettlement(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"ids\":[\"办结单id列表\"]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("撤销 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        // User user = jwtUtil.getUserByToken(token);
        List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
        if(ids.isEmpty()){
          response.recordError("撤销办结单id列表不能为空");
          return response;
        }
        endPaperService.cancelSettlement(ids);
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
      @ApiParam("{\"id\":\"id\",\n"
          + " \"firstWater\":\"第一水量,数字\" ,\n"
          + " \"secondWater\":\"第二水量,数字\",\n"
          + " \"addWay\":\"加计划的方式：1-平均，2-最高\",\n"
          + " \"quarter\":\"季度，数字\",\n"
          + " \"year\":\"是否勾选年计划 0否，1是\",\n"
          + " \"addNumber\":\"增加水量，数字\",\n"
          + " \"opinions\":\"审核意见\"\n"
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
}
