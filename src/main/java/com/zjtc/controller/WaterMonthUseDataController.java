package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.User;
import com.zjtc.model.WaterMonthUseData;
import com.zjtc.service.WaterMonthUseDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuyantian
 * @date 2020/12/26
 * @description
 */
@Api(tags = "水使用量月数据")
@RestController
@RequestMapping("waterMonthUseData/")
@Slf4j
public class WaterMonthUseDataController {

  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private WaterMonthUseDataService waterMonthUseDataService;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("水使用量月数据分页")
  public ApiResponse queryPage(
      @Param("{\n"
          + "  \"current\":\"页码，必填\",\n"
          + "  \"size\":\"显示数据条数，必填\",\n"
          + "  \"unitName\":\"单位名称\",\n"
          + "  \"waterMeterCode\":\"水表档案号\",\n"
          + "  \"waterNumberStart\":\"水量开始\",\n"
          + "  \"waterNumberEnd\":\"水量结束\",\n"
          + "  \"nowPriceStart\":\"水价开始\",\n"
          + "  \"nowPriceEnd\":\"水价结束\",\n"
          + "  \"caliberStart\":\"口径开始\",\n"
          + "  \"caliberEnd\":\"口径结束\",\n"
          + "  \"sectorStart\":\"区段开始\",\n"
          + "  \"sectorEnd\":\"区段结束\",\n"
          + "  \"useYear\":\"选择年份\"\n"
          + "  \n"
          + "  \n"
          + "}")
      @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.info("删除部门,参数param==={" + jsonObject.toJSONString() + "}");
    if (null != user && null != jsonObject) {
      jsonObject.put("nodeCode",user.getNodeCode());
      Map<String,Object> result=waterMonthUseDataService.queryPage(jsonObject);
      response.setData(result);
    } else {
      response.recordError(500);
    }
    return response;
  }

}
