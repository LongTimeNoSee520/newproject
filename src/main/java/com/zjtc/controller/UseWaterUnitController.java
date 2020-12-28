package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.base.util.StringUtil;
import com.zjtc.model.Dict;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.User;
import com.zjtc.model.vo.UseWaterUnitRefVo;

import com.zjtc.service.UseWaterUnitService;
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
 * @date 2020/12/23
 * @description
 */
@Api(tags = "用水单位管理")
@RestController
@RequestMapping("userWaterUnit/")
@Slf4j
public class UseWaterUnitController {

  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private UseWaterUnitService useWaterUnitService;

  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "用水单位新增")
  public ApiResponse save(
      @ApiParam("{\n"
          + "    \"unitCode\":\"单位编号，区域码：createAreaCode()接口，序号：createUnitCode()接口\",\n"
          + "    \"unitName\":\"单位名称\",\n"
          + "    \"industry\":\"所属行业id，下拉框：\",\n"
          + "    \"useWaterUnitIdRef\":\"相关编号：下拉框：addUnitCodeList()接口\",\n"
          + "    \"areaCountry\":\"所属区域，字典码：area_country_code\",\n"
          + "    \"unitAddress\":\"地址\",\n"
          + "    \"zipAddress\":\"邮寄地址\",\n"
          + "    \"zipName\":\"邮寄名称\",\n"
          + "    \"department\":\"部门\",\n"
          + "    \"invoiceUnitName\":\"开票单位名称\",\n"
          + "    \"gisX\":\"坐标X轴\",\n"
          + "    \"gisY\":\"坐标Y轴\",\n"
          + "    \"saveUnitType\":\"是否节水单位,0否，1是\",\n"
          + "    \"meterList\":[\n"
          + "        {\n"
          + "            \"waterMeterCode\":\"水表档案号\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"bankList\":[\n"
          + "        {\n"
          + "            \"bankOfDeposit\":\"具体支行\",\n"
          + "            \"peopleBankPaySysNumber\":\"开户行号\",\n"
          + "            \"bankAccount\":\"银行帐号\",\n"
          + "            \"agreementNumber\":\"协议号\",\n"
          + "            \"entrustUnitName\":\"托收单位名称\",\n"
          + "            \"focusUserRemark\":\"集中户备注\",\n"
          + "            \"main\":\"是否是主账号：0否，1是\",\n"
          + "            \"otherBank\":\"是否他行，0否，1是\",\n"
          + "            \"signed\":\"是否签约，0否，1是\"\n"
          + "            \"bankCode\":\"银行代码\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"contactsList\":[\n"
          + "        {\n"
          + "            \"contacts\":\"联系人\",\n"
          + "            \"main\":\"是否主联系人：0否，1是\",\n"
          + "            \"mobileNumber\":\"手机号码\",\n"
          + "            \"phoneNumber\":\"座机电话\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"quotaFile\":[\n"
          + "        {\n"
          + "            \"industry\":\"所属行业id\",\n"
          + "            \"sub_industry\":\"行业名称\",\n"
          + "            \"product\":\"产品\",\n"
          + "            \"quota_unit\":\"定额单位\",\n"
          + "            \"quota_value\":\"定额值\",\n"
          + "            \"amount\":\"数量\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"responsibilityCode\":\"责任书编号，取上传附件时输入的责任书编号，多个责任书编号分号隔开\",\n"
          + "    \"sysFile\":[\n"
          + "        {\n"
          + "            \"id\":\"附件id\",\n"
          + "            \"fileType\":\"文件类型\",\n"
          + "            \"size\":\"大小\",\n"
          + "            \"filePath\":\"文件保存路径\",\n"
          + "            \"fileName\":\"文件名称\",\n"
          + "            \"deleted\":\"是否删除，0否，1是\"\n"
          + "        }\n"
          + "    ]\n"
          + "}")
      @RequestBody UseWaterUnit useWaterUnit,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位新增，参数param==={" + useWaterUnit.toString() + "}");
    if (null != useWaterUnit && null != user) {
      ApiResponse result = useWaterUnitService.save(useWaterUnit, user);
      if (500 == result.getCode()) {
        return result;
      } else {
        return response;
      }
    } else {
      response.recordError(500);
    }

    return response;
  }

  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "用水单位修改")
  public ApiResponse edit(@RequestBody UseWaterUnit useWaterUnit,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位修改，参数param==={" + useWaterUnit.toString() + "}");
    if (null != useWaterUnit && null != user) {
      ApiResponse result = useWaterUnitService.update(useWaterUnit, user);
      if (500 == result.getCode()) {
        return result;
      } else {
        return response;
      }
    } else {
      response.recordError(500);
    }

    return response;
  }

  @RequestMapping(value = "delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "用水单位删除")
  public ApiResponse edit(
      @ApiParam("{\n"
          + "\"ids\":[\"单位id数组\"]\n"
          + "}")
      @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位删除，参数param==={" + jsonObject + "}");
    if (null != jsonObject && null != user) {
      boolean result = useWaterUnitService.delete(jsonObject, user);
      if (result) {
        response.setCode(200);
      } else {
        response.setCode(200);
      }
    } else {
      response.recordError(500);
    }

    return response;
  }


  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "分页")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "  \"current\":\"1\",\n"
      + "  \"size\":\"2\",\n"
      + "  \"unitCode\":\"单位编号\",\n"
      + "  \"unitName\":\"单位名称\",\n"
      + "  \"waterMeterCode\":\"水表档案号\",\n"
      + "  \"unitCodeType\":\"用户类型\",\n"
      + "  \"bankAccount\":\"银行账号\",\n"
      + "  \"responsibilityCode\":\"责任书编号\",\n"
      + "  \"mobileMumber\":\"电话号码\",\n"
      + "  \"signed\":\"是否签约：0，否，1是\",\n"
      + "  \"abnormal\":\"是否异常：0，否，1是\"\n"
      + "  \"unitCodeRank\":\"单位编号排序，asc：升序，desc：降序\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("分页，参数param==={" + jsonObject.toString() + "}");
    if (null != jsonObject && null != user) {
      jsonObject.put("userId", user.getId());
      jsonObject.put("nodeCode", user.getNodeCode());
      Map<String, Object> result = useWaterUnitService.queryPage(jsonObject);
      response.setData(result);
    } else {
      response.recordError(500);
    }

    return response;
  }

  @RequestMapping(value = "selectById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "查询详情")
  public ApiResponse selectById(
      @ApiParam("{\\n\"\n"
          + "          + \"\\\"id\\\":\\\"单位id\\\"\\n\"\n"
          + "          + \"} ")
      @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位新增，参数param==={" + jsonObject.toString() + "}");
    if (null != jsonObject && null != user) {
      UseWaterUnit result = useWaterUnitService.selectById(jsonObject, user);
      response.setData(result);
    } else {
      response.recordError(500);
    }

    return response;
  }


  @RequestMapping(value = "addUnitCodeList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "新增界面：相关编号下拉回填数据")
  public ApiResponse addUnitCodeList(@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位新增，参数param==={" + jsonObject.toString() + "}");
    if (null != user) {
      List<Map<String, Object>> result = useWaterUnitService
          .addUnitCodeList(user);
      response.setData(result);
    } else {
      response.recordError(500);
    }

    return response;
  }

  @RequestMapping(value = "createRank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "新增界面：新增用水单位编号需要的排序号")
  public ApiResponse createRank(
      @ApiParam(" {\n"
          + "\"unitCode\":\"单位编号\"\n"
          + "} ") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("新增用水单位编号需要的排序号，参数param==={" + jsonObject.toString() + "}");
    if (null != user) {
      ApiResponse result = useWaterUnitService
          .createunitCode(user, jsonObject.getString("unitCode"), null);
      response = result;
    } else {
      response.recordError(500);
    }

    return response;
  }

  /**
   * 生成新增用水单位编号需要的区域码
   */
  @RequestMapping(value = "createAreaCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "新增界面：生成新增用水单位编号需要的区域码")
  public ApiResponse createunitCode(
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("新增用水单位编号需要的排序号");
    if (null != user) {
      String result = useWaterUnitService
          .createAreaCode(user.getNodeCode());
      response.setData(result);
    } else {
      response.recordError(500);
    }

    return response;
  }

  /**
   * 根据单位编号查询单位信息
   */
  @RequestMapping(value = "selectByUnitCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "根据单位编号查询单位信息")
  public ApiResponse selectByUnitCode(@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("根据单位编号查询单位信息");
    if (null != user) {
      UseWaterUnit result = useWaterUnitService
          .selectByUnitCode(jsonObject.getString("unitCode"), user);
      response.setData(result);
    } else {
      response.recordError(500);
    }

    return response;
  }

}
