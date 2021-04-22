package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Algorithm;
import com.zjtc.model.User;
import com.zjtc.service.CommonService;
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
 * @Author: lianghao
 * @Date: 2021/04/22
 */
@Api(tags= "公共服务")
@RestController
@RequestMapping("common")
@Slf4j
public class CommonController {

  /**
   * AlgorithmService服务
   */
  @Autowired
  private CommonService commonService;

  @Autowired
  private JWTUtil jwtUtil;

  @ApiOperation(value = "修改打印状态")
  @RequestMapping(value = "updatePrintStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse updatePrintStatus(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"ids\":[//对应打印页面数据的id集合],\n"
          + " \"module\":\"日常调整传 ADJUST,缴费管理传 PAY,调整审核传 AUDIT。\"\n"
          + "}")@RequestBody JSONObject jsonObject) {
    ApiResponse apiResponse = new ApiResponse();
    try {
     // User user = jwtUtil.getUserByToken(token);
      List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
      String module=  jsonObject.getString("module");
      if (null != ids && !ids.isEmpty() ){
        boolean result = commonService.updatePrintStatus(ids,module);
        if (result) {
          apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
        } else {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
      }else {
        apiResponse.recordError("参数不能为空");
        return apiResponse;
      }

    } catch (Exception e) {
      log.error("修改打印状态出错,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

}
