package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.WaterQuantityManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author lianghao
 * @date 2020/12/23
 */

@RestController
@RequestMapping("/waterQuantity")
@Api(tags = "水量管理")
@Slf4j
public class WaterQuantityManageController {

  @Autowired
  private WaterQuantityManageService waterQuantityManageService;

  @Autowired
  private JWTUtil jwtUtil;

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "分页查询水量")
  public ApiResponse queryPage(@RequestHeader("token") String token,
      @ApiParam("{\"current\":\"当前页,数字类型\",\n"
          + "\"size\":\"每页条数,数字类型\"\n"
          + "}{\"current\":\"当前页,数字类型\",\n"
          + " \"size\":\"每页条数,数字类型\" ,\n"
          + " \"unitName\":\"单位名称\",\n"
          + " \"waterMeterCode\":\"水表档案号\",\n"
          + " \"waterNumberStart\":\"水量区间起始数(小数Float)\",\n"
          + " \"waterNumberEnd\":\"水量区间结束数(小数Float)\",\n"
          + " \"priceStart\":\"价格区间起始数(小数Float)\",\n"
          + " \"priceEnd\":\"价格区间结束数(小数Float)\",\n"
          + " \"caliberStart\":\"口径区间起始数(小数Float)\",\n"
          + " \"caliberEnd\":\"口径区间结束数(小数Float)\",\n"
          + " \"sectorStart\":\"区段区间开始数 数字类型\",\n"
          + " \"sectorEnd\":\"区段区间结束数 数字类型\",\n"
          + " \"useYear\":\"年份 数字类型\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("分页查询水量 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        Map<String, Object> result = waterQuantityManageService.queryPage(user, jsonObject);
        response.setData(result);
      } catch (Exception e) {
        log.error("水量分页条件查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("分页查询参数不能为空");
    }
    return response;
  }

  @ApiOperation(value = "下载模板")
  @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)
  public ApiResponse downloadTemplate(HttpServletResponse response, HttpServletRequest request) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      waterQuantityManageService.downloadTemplate(request, response);
    } catch (Exception e) {
      log.error("模版下载错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError("模版下载出错");
    }
    return apiResponse;
  }

  @ApiOperation("分片上传")
  @RequestMapping(value = "uploadZone",method = RequestMethod.POST,consumes = "multipart/form-data;charset=utf8")
  public ApiResponse uploadZone(@RequestHeader("token") String token,
      @ApiParam("文件")@RequestParam("file") MultipartFile file,@ApiParam("文件上传的UUID") @RequestParam("fileProcessId") String fileProcessId,
      @RequestParam("chunk") int chunk) {
    ApiResponse apiResponse = new ApiResponse();
    //1.判断分片文件是否存在
    if (file.isEmpty()) {
      apiResponse.recordError("上传失败，附件不能为空");
      return apiResponse;
    }
    //获取用户信息
    // User user = jwtUtil.getUserByToken(token);
    //2.分片上传
    boolean result = waterQuantityManageService.zoneUpload(file,fileProcessId,chunk);
    if (!result) {
      apiResponse.recordError("分片上传失败");
    }
    return apiResponse;
  }
  @ApiOperation("合并")
  @RequestMapping(value = "merge", method = RequestMethod.POST)
  public ApiResponse merge(@RequestHeader("token") String token,
      @Param("{ \"fileProcessId\":\"文件上传的UUID\"}")@RequestBody JSONObject jsonObject) {
    ApiResponse apiResponse = new ApiResponse();
    String fileProcessId = jsonObject.getString("fileProcessId");
  //  String fileName = jsonObject.getString("fileName");
    if (StringUtils.isBlank(fileProcessId) || StringUtils.isBlank(fileProcessId)  ) {
      apiResponse.recordError("合并失败，上传文件id和文件名不能为空");
      return apiResponse;
    }
    //获取用户信息
    User user = jwtUtil.getUserByToken(token);
    //合并分片文件
    boolean result = waterQuantityManageService.merge(fileProcessId);
    if (!result) {
      apiResponse.recordError("合并文件失败");
    }
    return apiResponse;
  }
  @RequestMapping(value = "checkAndInsertData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "数据检查和入库")
  public ApiResponse checkAndInsertData(@RequestHeader("token") String token,
      @ApiParam("{ \n"
          + "  \"fileProcessId\":\"文件上传的UUID\",\n"
          + "  \"fileName\":\"上传文件的名称\"\n"
          + "}") @RequestBody JSONObject jsonObject) {
    log.info("数据检查 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        String fileProcessId = jsonObject.getString("fileProcessId");
        String fileName = jsonObject.getString("fileName");
        response = waterQuantityManageService.checkAndInsertData(user,fileProcessId,fileName);
      } catch (Exception e) {
        log.error("检查到错误:errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError("参数不能为空");
    }
    return response;
  }
  @RequestMapping(value = "importEnd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "导入完毕后往月使用表更新或者插入数据")
  public ApiResponse importEnd(@RequestHeader("token") String token,
      @ApiParam("{}") @RequestBody JSONObject jsonObject) {
    log.info("数据检查 ==== 参数{" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();
      try {
       // User user = jwtUtil.getUserByToken(token);
         waterQuantityManageService.importEnd();
      } catch (Exception e) {
        log.error("数据错误:errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    return response;
  }

}
