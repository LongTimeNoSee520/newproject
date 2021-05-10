package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.WaterSavingUnitService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * WaterSavingUnit的路由接口服务
 *
 * @author zengqingsong
 */
@RestController
@RequestMapping("waterSavingUnit")
@Api(tags = "节水型单位管理")
@Slf4j
public class WaterSavingUnitController {

  /**
   * WaterSavingUnitService服务
   */
  @Autowired
  private WaterSavingUnitService waterSavingUnitService;
  @Autowired
  private JWTUtil jwtUtil;


  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询xxx内容")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "  \"current\":\"当前页\",\n"
      + "  \"size\":\"显示数据条数\",\n"
      + "  \"unitCode\":\"单位编号\",\n"
      + "  \"unitName\":\"单位名称\",\n"
      + "  \"legalRepresentative\":\"法人代表\"\n"
      + "  \n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    if (null != jsonObject) {
      try {
        Map<String, Object> result = waterSavingUnitService.queryPage(jsonObject);
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
          + "    \"id\":\"节水单位id\",\n"
          + "    \"unitCode\":\"单位编号\",\n"
          + "    \"unitName\":\"单位名称\",\n"
          + "    \"address\":\"地址\",\n"
          + "    \"legalRepresentative\":\"张三\",\n"
          + "    \"phoneNumber\":\"联系电话\",\n"
          + "    \"centralizedDepartment\":\"归口部门\",\n"
          + "    \"reviewTime\":\"复查时间，时间戳\",\n"
          + "    \"reviewScore\":\"复查分数 double\",\n"
          + "    \"zipCode\":\"邮寄地址\",\n"
          + "    \"createTime\":\"创建时间 时间戳\",\n"
          + "    \"createScore\":\"创建分数\",\n"
          + "    \"industrialAdded\":\"工业增加值 double\",\n"
          + "    \"totalWaterQuantity\":\"总取水量 double\",\n"
          + "    \"industrialAddedWater\":\"万元工业增加值取水量m3/万元 double\",\n"
          + "    \"zbRate\":\"装表率 double\",\n"
          + "    \"reuseRate\":\"复查率 double\",\n"
          + "    \"leakageRale\":\"漏失率 double\",\n"
          + "    \"remarks\":\"备注\",\n"
          + "    \"sysFiles\":[\n"
          + "        {\n"
          + "            \"id\":\"附件id\",\n"
          + "            \"fileName\":\"附件名\",\n"
          + "            \"deleted\":\"是否删除 ，0否1是\",\n"
          + "            \"url\":\"附件预览路径\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"waterSavingUnitQuotaList\":[\n"
          + "        {\n"
          + "            \"id\":\"定量考核标准id\",\n"
          + "            \"waterSavingUnitId\":\"节水单位id\",\n"
          + "            \"quotaIndex\":\"定量指标考核\",\n"
          + "            \"assessAlgorithm\":\"考核计算方法\",\n"
          + "            \"assessStandard\":\"考核标准\",\n"
          + "            \"standardLevel\":\"标准水平\",\n"
          + "            \"companyScore\":\"企业分数 double\",\n"
          + "            \"unitScore\":\"单位分数 double\",\n"
          + "            \"checkScore\":\"自查分数 double\",\n"
          + "            \"actualScore\":\"实际分数 double\",\n"
          + "            \"deleted\":\"是否删除 ，0否1是,新增或修改：0，删除1\",\n"
          + "            \"remarks\":\"备注\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"waterSavingUnitBaseList\":[\n"
          + "        {\n"
          + "            \"id\":\"基础考核标准id\",\n"
          + "            \"waterSavingUnitId\":\"节水单位id\",\n"
          + "            \"contents\":\"考核内容\",\n"
          + "            \"assessMethod\":\"考核方法\",\n"
          + "            \"assessStandard\":\"考核标准\",\n"
          + "            \"companyScore\":\"企业分数 double\",\n"
          + "            \"unitScore\":\"单位分数 double\",\n"
          + "            \"checkScore\":\"自查分数 double\",\n"
          + "            \"actualScore\":\"实际分数 double\",\n"
          + "            \"deleted\":\"是否删除 ，0否1是,新增或修改：0，删除1\",\n"
          + "            \"remarks\":\"备注\"\n"
          + "        }\n"
          + "    ]\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("修改==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user=jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      try {
        apiResponse = waterSavingUnitService.updateModel(jsonObject,user);
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
   * 通过id删除WaterSavingUnit数据方法
   */
  @ResponseBody
  @ApiOperation(value = "删除")
  @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse remove(@RequestHeader("token") String token,
      @ApiParam("{\"id\":\"节水单位id\"}") @RequestBody JSONObject jsonObject) {
    log.info("删除 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user=jwtUtil.getUserByToken(token);
    if (null != jsonObject) {
      try {
        boolean result = waterSavingUnitService.deleteModel(jsonObject.getString("id"),user);
        if (result) {
          apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
        } else {
          apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
        }
      } catch (Exception e) {
        log.error("删除错误,errMsg==={}", e.getMessage());
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
      @RequestHeader("token") String token) {
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    try {
      jwtUtil.getUserByToken(token);
      apiResponse = waterSavingUnitService
          .importExcel(file, user);
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
      waterSavingUnitService.downloadTemplate(request, response);
    } catch (Exception e) {
      log.error("证书模版下载错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
    }
    return apiResponse;
  }
}
