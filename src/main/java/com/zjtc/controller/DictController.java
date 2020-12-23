package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.conf.DictConf;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Dict;
import com.zjtc.model.User;
import com.zjtc.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
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
 * @date 2020/12/07
 */
@Api(tags = "数据字典")
@RestController
@RequestMapping("dict/")
@Slf4j
public class DictController {

  @Autowired
  private DictService dictService;
  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private DictConf dictConf;

  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "字典新增")
  public ApiResponse save(@ApiParam("{\n"
      + "  \"dictName\":\"字典名称\",\n"
      + "  \"dictCode\":\"字典编码\",\n"
      + "  \"dictRank\":\"字典排序，int\",\n"
      + "  \"dictDesc\":\"字典描述\"\n"
      + "}") @RequestBody Dict dict, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("字典新增，参数param==={" + dict.toString() + "}");
    if (null != dict) {
      try {
        //验证字段名称
        //判断字典编码、排序号是否重复
        ApiResponse validate = dictService.checkDictCode(dict, user.getNodeCode());
        if (500 == validate.getCode()) {
          return validate;
        }
        boolean resultAdd = dictService.save(dict, user.getNodeCode());
        if (resultAdd) {
          //异步刷新字典缓存
          dictConf.asyncRefresh(user.getNodeCode());
        } else {
          response.recordError(500);
        }
      } catch (Exception e) {
        log.error("字典新增失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
    }
    return response;
  }

  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "字典修改")
  public ApiResponse edit(@ApiParam("{\n"
      + "  \"id\":\"字典id\",\n"
      + "  \"dictName\":\"字典名称\",\n"
      + "  \"dictCode\":\"字典编码\",\n"
      + "  \"dictRank\":\"字典排序\",\n"
      + "  \"dictDesc\":\"字典描述\"\n"
      + "}") @RequestBody Dict dict, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("字典修改，参数param==={" + dict.toString() + "}");
    if (null != dict) {
      try {
        ApiResponse validate = dictService.checkDictCode(dict, user.getNodeCode());//判断字典编码是否重复
        if (500 == validate.getCode()) {
          return validate;
        }
        boolean resultAdd = dictService.edit(dict, user.getNodeCode());
        if (resultAdd) {
          //异步刷新字典缓存
          dictConf.asyncRefresh(user.getNodeCode());
        } else {
          response.recordError(500);
        }
      } catch (Exception e) {
        log.error("字典修改失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
    }
    return response;
  }

  @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "字典批量删除")
  public ApiResponse remove(@ApiParam("{\n"
      + "  \"ids\":[\"字典id集合，批量删除\"]\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("字典批量删除，参数param==={" + jsonObject.toString() + "}");
    if (StringUtils.isNotBlank(jsonObject.getString("ids"))) {
      try {
        //删除前验证是否有字典项
        ApiResponse validate = dictService.removeBeforeValidate(jsonObject, user.getNodeCode());
        if (500 == validate.getCode()) {
          return validate;
        }
        boolean result = dictService.delete(jsonObject, user.getNodeCode());
        if (result) {
          //异步刷新字典缓存
          dictConf.asyncRefresh(user.getNodeCode());
        } else {
          response.recordError(500);
        }
      } catch (Exception e) {
        log.error("字典批量删除失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
    }
    return response;
  }

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "字典分页查询")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "  \"current\":\"当前页，必传\",\n"
      + "  \"size\":\"显示数据条数，必传\",\n"
      + "  \"dictCode\":\"字典编码\",\n"
      + "  \"dictName\":\"字典名称\"\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    log.debug("字典分页查询，参数param==={" + jsonObject.toString() + "}");
    if (null != jsonObject) {
      try {
        if (null != user) {
          jsonObject.put("nodeCode", user.getNodeCode());
        }
        Map<String, Object> result = dictService.queryPage(jsonObject);
        if (null != result) {
          response.setData(result);
          response.setCode(200);
        } else {
          response.recordError(500);
        }
      } catch (Exception e) {
        log.error("字典分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
    }
    return response;
  }

  @ApiOperation("根据多个字典编码获取字典信息")
  @ResponseBody
  @RequestMapping(value = "findByDictCodes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findByDictCodes(@ApiParam("{\n"
      + "    \"dictCode\":\"多个字典编码用逗号隔开\",\n"
      + "  \n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("数据字典，根据多个字典编码获取字典信息 ==== 参数 {" + jsonObject.toString() + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    try {
      String dictCode = jsonObject.getString("dictCode");
      if (StringUtils.isNotBlank(dictCode)) {
        Map<String, List<Map<String, Object>>> result = dictService
            .findByDictCodes(dictCode, user.getNodeCode());
        apiResponse.setData(result);
      } else {
        apiResponse.recordError(500);
      }
    } catch (Exception e) {
      log.error("数据字典，根据多个字典编码获取字典信息错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(500);
    }
    return apiResponse;
  }
}
