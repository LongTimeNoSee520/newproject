package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
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
  @RequestMapping(value = "cancelSettlement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "撤销")
  public ApiResponse cancelSettlement(@RequestHeader("token") String token,
      @ApiParam("") @RequestBody JSONObject jsonObject) {
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
}
