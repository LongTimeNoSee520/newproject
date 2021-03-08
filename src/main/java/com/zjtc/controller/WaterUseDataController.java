package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.model.WaterUseData;
import com.zjtc.service.WaterMonthUseDataService;
import com.zjtc.service.WaterUseDataService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/26
 */
@Api(tags = "水使用量数据")
@RestController
@RequestMapping("waterUseData/")
@Slf4j
public class WaterUseDataController {


  @Autowired
  private WaterUseDataService waterUseDataService;

  @Autowired
  private JWTUtil jwtUtil;

  @Autowired
  private WaterMonthUseDataService waterMonthUseDataService;

  @ResponseBody
  @ApiOperation(value = "查询可使用年份")
  @RequestMapping(value = "queryYear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse exchange(@RequestHeader("token") String token,
      @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null == user) {
      response.recordError("系统异常");
      return response;
    }
    try {
      List<Integer> list = waterUseDataService.queryYear(jsonObject.getString("nodeCode"));
      if (list.isEmpty()) {
        response.recordError("查询可使用年份失败");
        return response;
      } else {
        response.setData(list);
        response.setCode(200);
        return response;
      }
    } catch (Exception e) {
      log.error("查询可使用年份异常==" + e.getMessage());
      e.printStackTrace();
    }
    return response;
  }

  @RequestMapping(value = "selectWaterUseData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("根据水表档案号回填水表信息")
  public ApiResponse selectWaterUseData(@ApiParam("   {\n"
      + "     \"waterMeterCode\":[\"水表档案号集\"]\n"
      + "   }") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("根据水表档案号回填水表信息,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (user == null) {
      response.setMessage("根据水表档案号回填水表信息失败");
      return response;
    }
    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    List<String> waterMeterCode = jsonObject.getJSONArray("waterMeterCode")
        .toJavaList(String.class);
    try {
      List<WaterMonthUseData> waterMonthUseDataList = waterMonthUseDataService
          .selectWaterUseData(waterMeterCode);
      for (WaterMonthUseData waterMonthUseData : waterMonthUseDataList){
        waterMonthUseData.setWaterNumber(String.valueOf(
            waterMonthUseData.getJanuaryCount()
                +waterMonthUseData.getFebruaryCount()
                +waterMonthUseData.getMarchCount()
                +waterMonthUseData.getAprilCount()
                +waterMonthUseData.getMayCount()
                +waterMonthUseData.getJuneCount()
                +waterMonthUseData.getJulyCount()
                +waterMonthUseData.getAugustCount()
                +waterMonthUseData.getSeptemberCount()
                +waterMonthUseData.getOctoberCount()
                +waterMonthUseData.getNovemberCount()
                +waterMonthUseData.getDecemberCount()));
      }
      response.setData(waterMonthUseDataList);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("根据水表档案号回填水表信息异常");
      log.error("根据水表档案号回填水表信息错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
