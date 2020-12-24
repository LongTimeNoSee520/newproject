package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.QuotaInfo;
import com.zjtc.model.User;
import com.zjtc.service.QuotaInfoService;
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
 * @author lianghao
 * @date 2020/12/23
 */

@Api(tags = "定额信息维护")
@RestController
@RequestMapping("/quotaInfo")
@Slf4j
public class QuotaInfoController {

  @Autowired
  private QuotaInfoService quotaInfoService;

  @Autowired
  private JWTUtil jwtUtil;

  @ApiOperation(value = "新增")
  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse add(@RequestHeader("token") String token, @ApiParam("{\n"
      + "  \"advanceValue\":\"先进值,float\",\n"
      + "  \"industryName\": \"行业名称\",\n"
      + "  \"parentId\": \"上级行业id\",\n"
      + "  \"quotaUnit\": \"定额单位\",\n"
      + "  \"quotaValue\":\"定额值,float\",\n"
      + "  \"remark\": \"备注\"\n"
      + "}")@RequestBody QuotaInfo quotaInfo) {
    log.info("定额信息新增参数{" + quotaInfo != null ? quotaInfo.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);//通过token取节点编码
    if (null != quotaInfo) {
      try {
        quotaInfoService.add(user, quotaInfo);
      } catch (Exception e) {
        log.error("定额信息新增失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
      response.recordError("参数不能为空");
    }
    return response;
  }

  @ApiOperation(value = "修改")
  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse edit(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"id\": \"id\",\n"
          + "  \"advanceValue\":\"先进值,float\",\n"
          + "  \"industryName\": \"行业名称\",\n"
          + "  \"parentId\": \"上级行业id\",\n"
          + "  \"quotaUnit\": \"定额单位\",\n"
          + "  \"quotaValue\":\"定额值,float\",\n"
          + "  \"remark\": \"备注\"\n"
          + "}")@RequestBody QuotaInfo quotaInfo) {
    log.info("修改==== 参数{" + quotaInfo != null ? quotaInfo.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    if (null != quotaInfo) {
      try {
          quotaInfoService.edit(quotaInfo);
      } catch (Exception e) {
        log.error("修改失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        response.recordError("修改失败");
      }
    } else {
      response.recordError(500);
      response.recordError("参数不能为空");
    }
    return response;
  }

  @ApiOperation(value = "删除")
  @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse remove(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"ids\":[\"定额信息id列表\"]\n"
          + "}")@RequestBody JSONObject jsonObject) {
    log.info("删除 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (null != ids && ids.size() > 0) {
      try {
        boolean result = quotaInfoService.delete(ids);
        if (result) {
          response.setCode(200);
        } else {
          response.recordError(500);
        }
      } catch (Exception e) {
        log.error("删除失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        response.recordError(500);
      }
    } else {
      response.recordError("定额信息id不能为空");
    }
    return response;
  }

  @ApiOperation(value = "查询树")
  @RequestMapping(value = "queryTree", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse queryTree(@RequestHeader("token") String token,
      @ApiParam("{ \"keyword\":\"行业名称，关键字查询\"}")@RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    log.debug("资源树查询，参数param==={" + jsonObject.toString() + "}");
    try {
      String keyword =jsonObject.getString("keyword");
      List<QuotaInfo> result = quotaInfoService.queryTree(keyword);
      response.setData(result);
      response.setCode(200);
    } catch (Exception e) {
      log.error("定额信息树查询失败,errMsg==={}" + e.getMessage());
      response.recordError(500);
    }
    return response;
  }

}
