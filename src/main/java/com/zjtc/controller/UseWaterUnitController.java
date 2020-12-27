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
  public ApiResponse save(@RequestBody UseWaterUnit useWaterUnit,
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
  public ApiResponse edit(@RequestBody JSONObject jsonObject,
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
  public ApiResponse queryPage(@Param ("{\n"
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
      + "}")@RequestBody JSONObject jsonObject,
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
  public ApiResponse selectById(@Param("  \"id\":\"单位id\" ")@RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("用水单位新增，参数param==={" + jsonObject.toString() + "}");
    if (null != jsonObject && null != user) {
      UseWaterUnit result = useWaterUnitService.selectById(jsonObject,user);
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

  @RequestMapping(value = "createunitCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "新增界面：新增用水单位编号需要的排序号")
  public ApiResponse createunitCode(@Param("  \"unitCode\":\"单位编号\", ")@RequestBody JSONObject jsonObject,
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
   *生成新增用水单位编号需要的区域码
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
      response.setData(response);
    } else {
      response.recordError(500);
    }

    return response;
  }
}
