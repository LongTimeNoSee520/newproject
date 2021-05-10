package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.WaterSavingEfficiencyManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * WaterSavingUnit的路由接口服务
 *
 * @author zengqingsong
 */
@RestController
@RequestMapping("WaterSavingEfficiencyManage")
@Api(tags = "节水效率指标评估")
@Slf4j
public class WaterSavingEfficiencyManageController {

  /**
   * WaterSavingUnitService服务
   */
  @Autowired
  private WaterSavingEfficiencyManageService waterSavingEfficiencyManageService;
  @Autowired
  private JWTUtil jwtUtil;


  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询xxx内容")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "  \"current\":\"当前页\",\n"
      + "  \"size\":\"显示数据条数\",\n"
      + "  \"unitCode\":\"单位编号\",\n"
      + "  \"unitName\":\"单位名称\"\n"
      + "  \n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user= jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      try {
        jsonObject.put("nodeCode",user.getNodeCode());
        Map<String, Object> result = waterSavingEfficiencyManageService.queryPage(jsonObject);
        apiResponse.setData(result);
        apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
      } catch (Exception e) {
        log.error("查询错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  /**
   * 修改
   */
  @ResponseBody
  @ApiOperation(value = "修改")
  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse edit(@RequestHeader("token") String token,
      @ApiParam("{\n"
          + "\"id\":\"水平衡测试id\",\n"
          + "\"manages\":[\n"
          + "{\n"
          + "\"id\": \"用水效率id\",\n"
          + "\"type\": \"分类 string\",\n"
          + "\"evaluationIndex\": \"评估指标 string\",\n"
          + "\"calculationFormula\": \"计算公式 string\",\n"
          + "\"actualValue\": \"实测值 double\",\n"
          + "\"industryQuota\":\"行业定额 double\" ,\n"
          + "\"interAdvancedValue\": \"国际先进值 double\",\n"
          + "\"levelAnalysis\": \"水平分析 string\",\n"
          + "\"deleted\": \"是否删除，新增行前端默认传值0，删除行传值1\",\n"
          + "\"pcquota\": \"省市定额 double\"\n"
          + "}\n"
          + "]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("修改==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      try {
        boolean result = waterSavingEfficiencyManageService.addOrUpdate(jsonObject, user);
        if (!result) {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
      } catch (Exception e) {
        log.error("修改错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
      }
    } else {
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  /**
   * 导入
   */
  @ResponseBody
  @ApiOperation(value = "导入")
  @RequestMapping(value = "import", method = RequestMethod.POST)
  public ApiResponse excelImport(
      MultipartFile file,
      @RequestParam("id") String id,
      @RequestHeader("token") String token) {
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    try {
      jwtUtil.getUserByToken(token);
      apiResponse = waterSavingEfficiencyManageService
          .importExcel(file, user, id);
    } catch (Exception e) {
      log.error("导入错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }

  /**
   * 下载模版
   */
  @ResponseBody
  @ApiOperation(value = "下载模版")
  @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)
  public ApiResponse downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      waterSavingEfficiencyManageService.downloadTemplate(request, response);
    } catch (Exception e) {
      log.error("模版下载错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }
}
