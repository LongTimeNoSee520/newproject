package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.User;
import com.zjtc.model.vo.OrgTreeVO;
import com.zjtc.model.vo.UseWaterUnitVo;
import com.zjtc.service.UseWaterUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
          + "            \"main\":\"是否是主账号：0否，1是,3撤销\",\n"
          + "            \"otherBank\":\"银行代码\",\n"
          + "            \"signed\":\"是否签约，0否，1是\"\n"
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
          + "            \"subIndustry\":\"行业id\",\n"
          + "            \"product\":\"产品id\",\n"
          + "            \"quotaUnit\":\"定额单位\",\n"
          + "            \"quotaValue\":\"定额值\",\n"
          + "            \"amount\":\"数量\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"responsibilityCode\":\"责任书编号，取上传附件时输入的责任书编号，多个责任书编号分号隔开\",\n"
          + "    \"sysFile\":[\n"
          + "        {\n"
          + "            \"id\":\"附件id\",\n"
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
      response=result;
    } else {
      response.recordError(500);
    }

    return response;
  }

  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "用水单位修改")
  public ApiResponse edit(
      @ApiParam("{\n"
          + "    \"id\":\"单位id\",\n"
          + "    \"unitCode\":\"单位编号\",\n"
          + "    \"unitName\":\"单位名称\",\n"
          + "    \"industry\":\"所属行业，取所属行业id\",\n"
          + "    \"imainUnitId\":\"主户选择，取选择的单位id\",\n"
          + "    \"areaCountry\":\"字典：area_country_code\",\n"
          + "    \"unitAddress\":\"地址\",\n"
          + "    \"zipAddress\":\"邮寄地址\",\n"
          + "    \"zipName\":\"邮寄名称\",\n"
          + "    \"department\":\"部门\",\n"
          + "    \"invoiceUnitName\":\"开票单位\",\n"
          + "    \"abnormal\":\"是否异常：0否，1是\",\n"
          + "    \"abnormalCause\":\"异常原因\",\n"
          + "    \"gisX\":\"Y轴\",\n"
          + "    \"gisY\":\"X轴\",\n"
          + "    \"remark\":\"备注\",\n"
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
          + "            \"main\":\"是否是主账号：0否，1是,3撤销\",\n"
          + "            \"otherBank\":\"银行代码\",\n"
          + "            \"signed\":\"是否签约，0否，1是\"\n"
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
          + "            \"subIndustry\":\"行业id\",\n"
          + "            \"product\":\"产品id\",\n"
          + "            \"quotaUnit\":\"定额单位\",\n"
          + "            \"quotaValue\":\"定额值\",\n"
          + "            \"amount\":\"数量\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"sysFile\":[\n"
          + "        {\n"
          + "            \"id\":\"附件id\",\n"
          + "            \"nodeCode\":\"节点编码\",\n"
          + "            \"createTime\":\"创建时间\",\n"
          + "            \"createrId\":\"创建者\",\n"
          + "            \"fileType\":\"文件内心\",\n"
          + "            \"size\":\"大小\",\n"
          + "            \"filePath\":\"文件路径\",\n"
          + "            \"fileName\":\"文件名\",\n"
          + "            \"businessId\":\"业务id\",\n"
          + "            \"deleted\":\"1\" \n"
          + "        }\n"
          + "    ],\n"
          + "    \"useWaterUnitRefList\":[\n"
          + "        {\n"
          + "            \"id\":\"关联单位id,这里没有修改，只有删除，传要删除的单位id\"\n"
          + "        }\n"
          + "    ],\n"
          + "    \"refEditData\":{\n"
          + "        \"refIds\":[\n"
          + "            \"要更新数据项的相关单位id\"\n"
          + "        ],\n"
          + "        \"useWaterUnitColumn\":[\n"
          + "            \"基本信息要更新的字段\"          \n"
          + "        ],\n"
          + "        \"bankColumn\":\"银行信息是否更新，true:更新，false:不更新\",\n"
          + "        \"contactsColumn\":\"联系人信息是否更新，true:更新，false:不更新\",\n"
          + "        \"fileColumn\":\"责任书信息是否更新，true:更新，false:不更新\",\n"
          + "        \"quotaFileColumn\":\"用水定额信息是否更新，true:更新，false:不更新\"\n"
          + "    }\n"
          + "}")
      @RequestBody UseWaterUnit useWaterUnit,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位修改，参数param==={" + useWaterUnit.toString() + "}");
    if (null != useWaterUnit && null != user) {
      ApiResponse result = useWaterUnitService.update(useWaterUnit, user);
      response=result;
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
      + "  \"nodeCode\":\"节点编码\",\n"
      + "  \"current\":\"页码，必填\",\n"
      + "  \"size\":\"显示数据条数，必填\",\n"
      + "  \"unitCode\":\"单位编号\",\n"
      + "  \"unitName\":\"单位名称\",\n"
      + "  \"waterMeterCode\":\"水表档案号\",\n"
      + "  \"unitCodeType\":\"用户类型\",\n"
      + "  \"bankAccount\":\"银行账号\",\n"
      + "  \"responsibilityCode\":\"责任书编号\",\n"
      + "  \"mobileMumber\":\"电话号码\",\n"
      + "  \"signed\":\"是否签约：0，否，1是\",\n"
      + "  \"contacts\":\"联系人\",\n"
      + "  \"industryName\":\"行业名称\",\n"
      + "  \"abnormal\":\"是否异常：0否，1是\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("分页，参数param==={" + jsonObject.toString() + "}");
    try {
      if (null != jsonObject && null != user) {
        String nodeCode = jsonObject.getString("nodeCode");
        if (StringUtils.isBlank(nodeCode)) {
          jsonObject.put("nodeCode", user.getNodeCode());
        }
        jsonObject.put("userId", user.getId());
        Map<String, Object> result = useWaterUnitService.queryPage(jsonObject);
        response.setData(result);
      } else {
        response.recordError(500);
      }
    } catch (Exception e) {
      log.error("分页查询错误,errMsg==={}", e.getMessage());
      response.recordError(500);
    }

    return response;
  }

  @RequestMapping(value = "addUnitCodePage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "新增界面：相关编号下拉回填数据")
  public ApiResponse addUnitCodePage(@ApiParam("{\n"
      + "\"current\":\"页码\",\n"
      + "\"size\":\"数据条数\"\n"
      + "\"keyword\":\"关键字，单位名称或单位编号\"\n"
      + "}") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位新增，参数param==={" + jsonObject.toString() + "}");
    if (null != jsonObject) {
      jsonObject.put("userId",user.getId());
      jsonObject.put("nodeCode",user.getNodeCode());
      Map<String, Object> result = useWaterUnitService
          .addUnitCodePage(jsonObject,user);
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
      Map<String, Object> result = useWaterUnitService
          .selectByUnitCode(jsonObject.getString("unitCode"), user);
      response.setData(result);
    } else {
      response.recordError(500);
    }

    return response;
  }

  /**
   * 导出账户审核表
   */
  @ResponseBody
  @ApiOperation(value = "导出账户审核表")
  @RequestMapping(value = "exportAccountAudit", method = RequestMethod.POST)
  public ApiResponse exportAccountAudit(
      HttpServletRequest request,
      HttpServletResponse response, @RequestHeader("token") String token) {
    JSONObject jsonObject = new JSONObject();
    log.info("导出账户审核表 ==== 参数{" + token != null ? token.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        apiResponse = useWaterUnitService.exportAccountAudit(user, jsonObject, request, response);
      } catch (Exception e) {
        log.error("导出账户审核表错误,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出开通格式")
  @RequestMapping(value = "exportForm", method = RequestMethod.POST)
  public ApiResponse exportForm
      (
          HttpServletRequest request,
          HttpServletResponse response, @RequestHeader("token") String token) {
    JSONObject jsonObject = new JSONObject();
    log.info("导出开通格式 ==== 参数{" + token != null ? token.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        apiResponse = useWaterUnitService.exportForm(user, jsonObject, request, response);
      } catch (Exception e) {
        log.error("导出开通格式错误,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出撤销格式")
  @RequestMapping(value = "exportRevoca", method = RequestMethod.POST)
  public ApiResponse exportRevoca
      (
          @ApiParam("{\n"
              + "  \"startTime\":\"开始时间：yyyy-MM-dd\",\n"
              + "  \"endTime\":\"结束时间：yyyy-MM-dd\"\n"
              + "}") @RequestBody JSONObject jsonObject,
          HttpServletRequest request,
          HttpServletResponse response, @RequestHeader("token") String token) {
    log.info("导出撤销格式 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        apiResponse = useWaterUnitService.exportRevoca(user, jsonObject, request, response);
      } catch (Exception e) {
        log.error("导出撤销格式错误,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出查询结果")
  @RequestMapping(value = "exportQueryData", method = RequestMethod.POST)
  public ApiResponse exportQueryData
      (
          @ApiParam("{\n"
              + "    \"exportQueryData\":\"单位编号\",\n"
              + "    \"unitName\":\"单位名称\",\n"
              + "    \"waterMeterCode\":\"水表档案号\",\n"
              + "    \"unitCodeType\":\"用户类型\",\n"
              + "    \"bankAccount\":\"银行账号\",\n"
              + "    \"responsibilityCode\":\"责任书编号\",\n"
              + "    \"mobileMumber\":\"电话号码\",\n"
              + "    \"signed\":\"是否签约：0，否，1是\",\n"
              + "  \"contacts\":\"联系人\",\n"
              + "  \"industryName\":\"行业名称\",\n"
              + "    \"abnormal\":\"是否异常：0，否，1是\"\n"
              + "}") @RequestBody JSONObject jsonObject,
          HttpServletRequest request,
          HttpServletResponse response, @RequestHeader("token") String token) {
    log.info("导出查询结果 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        apiResponse = useWaterUnitService.exportQueryData(user, jsonObject, request, response);
      } catch (Exception e) {
        log.error("导出查询结果错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ResponseBody
  @ApiOperation(value = "导出用水单位增减情况表")
  @RequestMapping(value = "exportMoreAndLess", method = RequestMethod.POST)
  public ApiResponse exportMoreAndLess
      (@RequestBody JSONObject jsonObject,
          HttpServletRequest request,
          HttpServletResponse response, @RequestHeader("token") String token) {
    log.info("导出用水单位增减情况 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        apiResponse = useWaterUnitService.exportMoreAndLess(user, jsonObject, request, response);
      } catch (Exception e) {
        log.error("导出用水单位增减情况错误,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ApiOperation(value = "根据单位名称查询单位编号")
  @RequestMapping(value = "selectCodeByName", method = RequestMethod.POST)
  public ApiResponse selectCodeByName
      (@ApiParam("{\n"
          + "\"unitName\":\"单位编号\"\n"
          + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("根据单位名称查询单位编号 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != user) {
      try {
        jsonObject.put("nodeCode", user.getNodeCode());
        jsonObject.put("userId", user.getId());
        List<Map<String, Object>> result = useWaterUnitService.selectCodeByName(jsonObject);
        apiResponse.setData(result);
      } catch (Exception e) {
        log.error("根据单位名称查询单位编号,errMsg==={}", e.getMessage());
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ApiOperation(value = "查询所有的用户类型")
  @RequestMapping(value = "selectAllType", method = RequestMethod.POST)
  public ApiResponse selectAllType(@ApiParam("{\n"
      + "\"nodeCode\":\"节点编码\"\n"
      + "}")
  @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse apiResponse = new ApiResponse();
    String nodeCode = jsonObject.getString("nodeCode");
    if (StringUtils.isBlank(nodeCode)) {
      apiResponse.recordError("该用户没有用户类型,请联系运维单位添加");
      return apiResponse;
    }
    try {
      List<String> result = useWaterUnitService.selectAllType(nodeCode);
      apiResponse.setData(result);
    } catch (Exception e) {
      log.error("查询所有的用户类型,errMsg==={}", e.getMessage());
      apiResponse.recordError(500);
    }
    return apiResponse;
  }



  @ResponseBody
  @ApiOperation(value = "查询所有的用水单位类型")
  @RequestMapping(value = "selectUnitCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse selectUnitCode(@ApiParam("{\n"
      + "    \"nodeCode\":\"节点编码\",\n"
      + "    \"condition\":\"用水单位编号、单位名称、人员名称\"\n"
      + "}")
      @RequestBody JSONObject jsonObject) {
    ApiResponse response = new ApiResponse();
    String nodeCode = jsonObject.getString("nodeCode");
    String condition = jsonObject.getString("condition");
    try {
      List<OrgTreeVO> list = useWaterUnitService.selectUnitCode(nodeCode,condition);
      response.setData(list);
      return response;
    } catch (Exception e) {
      log.error("查询所有的用水单位类型异常==" + e.getMessage());
    }
    return response;
  }
  @ApiOperation(value = "新增时，根据选择的相关编号，回填用水单位数据")
  @RequestMapping(value = "selectById", method = RequestMethod.POST)
  public ApiResponse selectById(@ApiParam("{\n"
      + "    \"id\":\"单位id\"\n"
      + "}")
      @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user=jwtUtil.getUserByToken(token);
    try {
      UseWaterUnitVo result = useWaterUnitService.selectById(jsonObject,user);
      response.setData(result);
      return response;
    } catch (Exception e) {
      log.error("查询所有的用水单位类型异常==" + e.getMessage());
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

}
