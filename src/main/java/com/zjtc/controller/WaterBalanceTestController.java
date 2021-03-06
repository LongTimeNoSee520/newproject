package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.WaterBalanceTestService;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

/**
 * @author lianghao
 * @date 2021/02/25
 */
@RestController
@RequestMapping("/waterBalanceTest")
@Api(tags = "水平衡测试管理")
@Slf4j
public class WaterBalanceTestController {

  @Autowired
  private WaterBalanceTestService waterBalanceTestService;
  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "水平衡测试分页查询")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "  \"current\":\"当前页,数字\", \n"
          + "  \"size\":\"每页条数,数字\" ,\n"
          + "  \"unitName\":\"单位名称\",\n"
          + "  \"unitCode\":\"单位编号\",\n"
          + "  \"nodeCode\":\"节点编码\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("水平衡测试分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        Map<String, Object> result = waterBalanceTestService
            .queryPage(user, jsonObject);
        response.setData(result);
      } catch (Exception e) {
        log.error("水平衡测试分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }

  @ResponseBody
  @ApiOperation(value = "修改")
  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse edit(@RequestHeader("token") String token, @ApiParam("{\n"
      + "\"id\": \"id\",\n"
      + "\"nodeCode\": \"节点编码\",\n"
      + "\"unitCode\": \"单位编码\",\n"
      + "\"unitName\": \"单位名称\",\n"
      + "\"unitAddress\": \"单位地址\",\n"
      + "\"industryName\": \"单位所属行业\",\n"
      + "\"industryCode\": \"所属行业编码\",\n"
      + "\"coverSpace\": \"单位占地面积 数字\",\n"
      + "\"floorSpace\": \"单位建筑面积 数字\",\n"
      + "\"usePeopleNum\": \"单位用水人数 数字\",\n"
      + "\"yearAmount\": \"单位年用水量 数字\",\n"
      + "\"lastTestTime\": \"上次测试时间\",\n"
      + "\"certificateNo\": \"合格证编号\",\n"
      + "\"leakageRate\": \"管网漏损率 数字\"\n"
      + "\"products\": [//主要产品用水单耗情况,删除的也要传(将deleted置为\"1\") \n"
      + "{\n"
      + " \"id\": \"产品id\"( 通过添加行新增的则传\"\"),\n"
      + "\"nodeCode\": \"节点编码\",\n"
      + "\"balanceTestId\": \"水平衡测试表id\",\n"
      + "\"productName\": \"产品名称\",\n"
      + "\"productCode\": \"产品编码\",\n"
      + "\"diffToQuota\": \"单耗比对定额值\",\n"
      + "\"perUseAmount\": \"人均用水量\",\n"
      + "\"yearPlan\": \"年计划用水量\",\n"
      + "\"deleted\": \"逻辑删除，1是0否\"\n"
      + "}],\n"
      + "\"fileList\": [{//附件，删除的也要传(将deleted置为\"1\"\n"
      + "\"id\": \"附件id\",\n"
      + "\"deleted\": \"是否删除，1是0否\"\n"
      + "}]\n"
      + "\n"
      + "}") @RequestBody JSONObject jsonObject) {
    log.info("修改==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);//通过token取节点编码
        boolean result = waterBalanceTestService.updateModel(user,jsonObject);
        if (!result){
          response.recordError("修改失败");
        }
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

  @ResponseBody
  @ApiOperation(value = "删除")
  @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse remove(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"ids\":[\"水平衡测试id列表\"]\n"
          + "}")@RequestBody JSONObject jsonObject) {
    log.info("删除 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse response = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (null != ids && ids.size() > 0) {
      try {
        waterBalanceTestService.deleteModel(ids);
      } catch (Exception e) {
        log.error("删除失败,errMsg==={}", e.getMessage());
        e.printStackTrace();
        response.recordError(500);
      }
    } else {
      response.recordError("水平衡测试id列表不能为空");
    }
    return response;
  }
  @RequestMapping(value = "importData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "数据导入")
  public ApiResponse importData(@RequestHeader("token") String token,
      @ApiParam("{ \n"
          + "  \"filePath\":\"文件上传后返回对象中的filePath值\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("数据检查 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        String filePath = jsonObject.getString("filePath");
        waterBalanceTestService.importData(user,filePath);
      } catch (Exception e) {
        log.error("数据导入出错:errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("参数不能为空");
    }
    return response;
  }

  @ApiOperation(value = "下载模板")
  @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)
  public ApiResponse downloadTemplate(@RequestHeader("token") String token,
      HttpServletResponse response, HttpServletRequest request) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      waterBalanceTestService.downloadTemplate(request, response);
    } catch (Exception e) {
      log.error("模版下载错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError("模版下载出错");
    }
    return apiResponse;
  }
}
