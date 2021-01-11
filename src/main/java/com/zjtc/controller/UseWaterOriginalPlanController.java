package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterOriginalPlan;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterOriginalPlanService;
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
import io.swagger.annotations.Api;

/**
 * TWUseWaterOriginalPlan的路由接口服务
 *
 * @author zengqingsong
 */
@Api(description = "计划编制 rest服务")
@RestController
@RequestMapping("useWaterOriginalPlan")
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
  public ApiResponse queryAllOld(@RequestBody JSONObject jsonObject,
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
  public ApiResponse queryAllNew(@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("查询列表==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject && null != user) {
      try {
        jsonObject.put("userId",user.getId());
        jsonObject.put("nodeCode",user.getNodeCode());
        List<Map<String, Object>> result = useWaterOriginalPlanService.goPlanningNew(jsonObject);
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
  /**
   * 插入TWUseWaterOriginalPlan属性不为空的数据方法
   */
  @ResponseBody
  @ApiOperation(value = "老户/新户保存")
  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse addOld(@RequestHeader("token") String token, @RequestBody JSONObject jsonObject) {
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
  public ApiResponse saveOriginal(@RequestHeader("token") String token, @RequestBody JSONObject jsonObject) {
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
}
