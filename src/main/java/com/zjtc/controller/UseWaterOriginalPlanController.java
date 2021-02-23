package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterOriginalPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TWUseWaterOriginalPlan的路由接口服务
 *
 * @author zengqingsong
 */
@RestController
@RequestMapping("useWaterOriginalPlan")
@Api(tags = "计划编制")
@Slf4j
public class UseWaterOriginalPlanController {

  /**
   * TWUseWaterOriginalPlanService服务
   */
  @Autowired
  private UseWaterOriginalPlanService useWaterOriginalPlanService;
  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryAllOld", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "老户查询列表")
  public ApiResponse queryAllOld(
      @ApiParam("{\n"
          + "\"userType\":\"用户类型\",\n"
          + "\"unitStart\":\"编号开头 \",\n"
          + "\"year\":\"年份\"\n"
          + "\n"
          + "}")@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("查询列表==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        jsonObject.put("userId",user.getId());
        jsonObject.put("nodeCode",user.getNodeCode());
        List<Map<String, Object>> result = useWaterOriginalPlanService.goPlanningOld(jsonObject);
        apiResponse.setData(result);
        apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
      } catch (Exception e) {
        log.error("老户查询错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @RequestMapping(value = "queryAllNew", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "新户查询列表")
  public ApiResponse queryAllNew(
      @RequestHeader("token") String token) {
    log.info("查询列表==== 参数{" + token + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != user) {
      try {
        List<Map<String, Object>> result = useWaterOriginalPlanService.goPlanningNew(user.getId(),user.getNodeCode());
        apiResponse.setData(result);
        apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
      } catch (Exception e) {
        log.error("新户查询错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }
  @ResponseBody
  @ApiOperation(value = "老户/新户保存")
  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse addOld(@RequestHeader("token") String token,@ApiParam("{\n"
      + "\"data\":[选择的保存数据集合]\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("老户/新户保存==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user=jwtUtil.getUserByToken(token);
    if (null != jsonObject && null!=user) {
      try {
        jsonObject.put("nodeCode",user.getNodeCode());
        ApiResponse result = useWaterOriginalPlanService.save(jsonObject);
        if(200!=result.getCode()){
          return result;
        }
      } catch (Exception e) {
        log.error("老户/新户保存错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "老户/新户编制")
  @RequestMapping(value = "saveOriginal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse saveOriginal(@RequestHeader("token") String token, @ApiParam("{\n"
      + "\"data\":[选择的编制数据集合]\n"
      + "}")@RequestBody JSONObject jsonObject) {
    log.info("老户/新户编制==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null !=user) {
      try {
        ApiResponse result = useWaterOriginalPlanService.saveOriginal(jsonObject,user);
        if(200!=result.getCode()){
          return result;
        }
      } catch (Exception e) {
        log.error("老户,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }
  @ResponseBody
  @ApiOperation(value = "新户调整三年平均水量时，回填重新计算的数据项")
  @RequestMapping(value = "getNewResultByThreeYearAvg", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse getNewResultByThreeYearAvg(@RequestHeader("token") String token,
     @ApiParam("{\n"
         + "  \"nowPrice\":\"水价\",\n"
         + "  \"threeYearAvg\":\"三年平均\",\n"
         + "  \"nextYearBaseStartPlan\":\"下年初计划(基础)\",\n"
         + "  \"unitCode\":\"单位编号\"\n"
         + "}") @RequestBody JSONObject jsonObject) {
    log.info("老户/新户编制==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null !=user) {
      try {
        Map<String,Object> result = useWaterOriginalPlanService.getNewResultByThreeYearAvg(jsonObject,user);
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("老户,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }
  @ResponseBody
  @ApiOperation(value = "新户调整下年终计划时，回填重新计算的数据项")
  @RequestMapping(value = "getNewByNextYearBase", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse getNewByNextYearBase(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"nextYearBaseEndPlan\":\"下年终计划\",\n"
          + "  \"threeYearAvg\":\"三年平均\",\n"
          + "  \"unitCode\":\"单位编号\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null !=user) {
      try {
        Map<String,Object> result = useWaterOriginalPlanService.getNewByNextYearBase(jsonObject,user);
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
  @ApiOperation(value = "老户/新户勾选【扣加价】，选择【水平衡】、【创建】，回填重新计算的数据项")
  @RequestMapping(value = "getResultBycheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse getResultBycheck(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"minusPayStatus\":\"扣加价，0否，1是\",\n"
          + "  \"balanceTest\":\"水平衡，1奖，2扣\",\n"
          + "  \"createType\":\"创建，1奖，2扣\"\n"
          + "  \"curYearPlan\":\"当年计划\"\n"
          + "  \"nextYearBaseStartPlan\":\"下年初始计划\"\n"
          + "  \"unitCode\":\"单位编号\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null !=user) {
      try {
        Map<String,Object> result = useWaterOriginalPlanService.getResultBycheck(jsonObject,user);
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
  @ApiOperation(value = "老户调整【三年平均水量】时,重新计算【下年初始计划(基础)】,回填重新计算的数据项")
  @RequestMapping(value = "getOldResultByThreeYearAvg", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse getOldResultByThreeYearAvg(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"nowPrice\":\"水价\",\n"
          + "  \"threeYearAvg\":\"三年平均\",\n"
          + "  \"unitCode\":\"单位编号\",\n"
          + "  \"n8\":\"n8\",\n"
          + "  \"curYearPlan\":\"当年计划\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null !=user) {
      try {
        Map<String,Object> result = useWaterOriginalPlanService.getOldResultByThreeYearAvg(jsonObject,user);
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
  @ApiOperation(value = "老户调整【下年年终计划(基础)】,重新计算【各季度计划(基础)】,回填重新计算的数据项")
  @RequestMapping(value = "getOldByNextYearBase", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse getOldByNextYearBase(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"nextYearBaseEndPlan\":\"下年终计划(基础)\",\n"
          + "  \"unitCode\":\"单位编号\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null !=user) {
      try {
        Map<String,Object> result = useWaterOriginalPlanService.getOldByNextYearBase(jsonObject,user);
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
  @ApiOperation(value = "新户调整水价，重新计算【下年初计划(基础)】,回填重新计算的数据项")
  @RequestMapping(value = "getNewByNowPrice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse getNewByNowPrice(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"nowPrice\":\"水价\",\n"
          + "  \"threeYearAvg\":\"三年平均\",\n"
          + "  \"nextYearBaseStartPlan\":\"下年初计划(基础)\",\n"
          + "  \"unitCode\":\"单位编号\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("查询==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null !=user) {
      try {
        Map<String,Object> result = useWaterOriginalPlanService.getNewByNowPrice(jsonObject,user);
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
  @ApiOperation(value = "老户导出")
  @RequestMapping(value = "exportOldData", method = RequestMethod.POST)
  public ApiResponse exportOldData
      (@ApiParam("{\n"
          + "\"data\":[\"页面查询的数据集合\"]\n"
          + "}") @RequestBody JSONObject jsonObject,
          HttpServletRequest request,
          HttpServletResponse response, @RequestHeader("token") String token) {
    log.info("导出老户数据 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        useWaterOriginalPlanService.exportOldData(jsonObject, request, response);
      } catch (Exception e) {
        log.error("导出老户数据错误,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }
  @ResponseBody
  @ApiOperation(value = "新户导出")
  @RequestMapping(value = "exportNewData", method = RequestMethod.POST)
  public ApiResponse exportNewData
      (@ApiParam("{\n"
          + "\"data\":[\"页面查询的数据集合\"]\n"
          + "}") @RequestBody JSONObject jsonObject,
          HttpServletRequest request,
          HttpServletResponse response, @RequestHeader("token") String token) {
    log.info("导出新户数据 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        useWaterOriginalPlanService.exportNewData(jsonObject, request, response);
      } catch (Exception e) {
        log.error("导出新户数据错误,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }
}
