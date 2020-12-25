package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Algorithm;
import com.zjtc.model.User;
import com.zjtc.service.AlgorithmService;
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
@Api(description = "编制算法服务")
@RestController
@RequestMapping("algorithm")
@Slf4j
public class AlgorithmController {

  /**
   * AlgorithmService服务
   */
  @Autowired
  private AlgorithmService algorithmService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "algorithms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "查询算法")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\"types(算法类型)\":\"[]}") @RequestBody JSONObject jsonObject) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    if (null != jsonObject) {
      User user = jwtUtil.getUserByToken(token);
      try {
        //算法类型
        List<String> algorithmTypes = jsonObject.getJSONArray("types").toJavaList(String.class);
        List<Algorithm> algorithm = algorithmService.queryList(user.getNodeCode(), algorithmTypes);
        apiResponse.setData(algorithm);
        apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
      } catch (Exception e) {
        log.error("查询算法错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }


  @ApiOperation(value = "新增或修改")
  @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse add(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "    \"algorithmType(算法类型)\": \"string\",\n"
          + "    \"basePro（基础比例）\": 0,\n"
          + "    \"createTime\": \"2020-12-25T08:30:44.006Z\",\n"
          + "    \"firstBatchNum（第一批次）\": \"string\",\n"
          + "    \"firstQuarterPro（第一季度比例）\": 0,\n"
          + "    \"firstYearPro（最近一年比重）\": 0,\n"
          + "    \"fourthQuarterPro（第四季度比例）\": 0,\n"
          + "    \"id\": \"string\",\n"
          + "    \"modifyTime\": \"2020-12-25T08:30:44.006Z\",\n"
          + "    \"n8Floot（n8Floot）\": 0,\n"
          + "    \"n8Up（n8Up）\": 0,\n"
          + "    \"newBatchNum（新户编码）\": \"string\",\n"
          + "    \"nodeCode\": \"string\",\n"
          + "    \"priceBottom（水价下届）\": 0,\n"
          + "    \"priceTop（水价上届）\": 0,\n"
          + "    \"reducePro（减扣比例）\": 0,\n"
          + "    \"rewardPro（奖励比例）\": 0,\n"
          + "    \"secondBatchNum（第二批次）\": \"string\",\n"
          + "    \"secondQuarterPro（第二季度比例）\": 0,\n"
          + "    \"secondYearPro（中间年比重）\": 0,\n"
          + "    \"thirdBatchNum（第三批次）\": \"string\",\n"
          + "    \"thirdQuarterPro（第三季度比例）\": 0,\n"
          + "    \"thirdYearPro（第一年水量）\": 0,\n"
          + "    \"threeAvgPro1（三年平均比例1）\": 0,\n"
          + "    \"threeAvgPro2（三年平均比例2）\": 0,\n"
          + "    \"threeAvgPro3（三年平均比例3）\": 0\n"
          + "  }")@RequestBody List<Algorithm> algorithms) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      User user = jwtUtil.getUserByToken(token);
      boolean result = algorithmService.saveOrUpdate(algorithms, user.getNodeCode());
      if (result) {
        apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
      } else {
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } catch (Exception e) {
      log.error("新增或修改错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

}
