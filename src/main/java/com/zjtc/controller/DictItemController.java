package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.conf.DictConf;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.DictItem;
import com.zjtc.model.User;
import com.zjtc.service.DictItemService;
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
@Api(tags = "数据字典项")
@RestController
@RequestMapping("dictItem/")
@Slf4j
public class DictItemController {

  @Autowired
  private DictItemService dictItemService;
  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private DictConf dictConf;

  /**
   * 插入DictItem属性不为空的数据方法
   */
  @ResponseBody
  @ApiOperation(value = "字典项新增")
  @RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse save(@ApiParam("{\n"
      + "  \"dictId\":\"字典id\",\n"
      + "  \"dictItemName\":\"字典项名称\",\n"
      + "  \"dictItemCode\":\"字典项编码\",\n"
      + "  \"dictItemRank\":\"字典项排序\",\n"
      + "  \"dictItemDesc\":\"字典项描述\"\n"
      + "}") @RequestBody DictItem dictItem, @RequestHeader("token") String token) {
    log.info("新增==== 参数{" + dictItem != null ? dictItem.toString() : "null" + "}");
    ApiResponse apiResponse = new ApiResponse();
    User user = jwtUtil.getUserByToken(token);
    if (null != dictItem) {
      try {
        //验证字典项编码、排序号是否重复
        ApiResponse validate = dictItemService.checkDictCode(dictItem, user.getNodeCode());
        if (500 == validate.getCode()) {
          return validate;
        }
        boolean result = dictItemService.save(dictItem, user.getNodeCode());
        if (result) {
          //刷新数据字典缓存
          dictConf.asyncRefresh(user.getNodeCode());
        } else {
          apiResponse.recordError(500);
        }
      } catch (Exception e) {
        log.error("数据字典新增错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  /**
   * 修改
   */
  @ResponseBody
  @ApiOperation(value = "字典项修改")
  @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse edit(@ApiParam("{\n"
      + "  \"id\":\"字典项id,主键\",\n"
      + "  \"dictId\":\"字典id\",\n"
      + "  \"dictItemName\":\"字典项名称\",\n"
      + "  \"dictItemCode\":\"字典项编码\",\n"
      + "  \"dictItemRank\":\"字典项排序\",\n"
      + "  \"dictItemDesc\":\"字典项描述\"\n"
      + "}") @RequestBody DictItem dictItem, @RequestHeader("token") String token) {
    log.info("修改==== 参数{" + dictItem != null ? dictItem.toString() : "null" + "}");
    User user = jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != dictItem) {
      try {
        //验证字典项编码、排序号是否重复
        ApiResponse validate = dictItemService.checkDictCode(dictItem, user.getNodeCode());
        if (500 == validate.getCode()) {
          return validate;
        }
        boolean result = dictItemService.edit(dictItem, token);
        if (result) {
          //刷新数据字典缓存
          dictConf.asyncRefresh(user.getNodeCode());
        } else {
          apiResponse.recordError(500);
        }

      } catch (Exception e) {
        log.error("数据字典修改错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  /**
   * 通过id删除DictItem数据方法
   */
  @ResponseBody
  @ApiOperation(value = "字典项批量删除")
  @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ApiResponse remove(@ApiParam("{\n"
      + "  \"ids\":[\"字典项id集合\",\"85f883243d46446cb1238f894fb51344\",\"fefac9fb6cd942b49a6f5da37b6a9215\"]\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("删除 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
    User user=jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    if (null != jsonObject) {
      try {
        boolean result = dictItemService.delete(jsonObject, user.getNodeCode());
        if (result) {
          //刷新数据字典缓存
          dictConf.asyncRefresh(user.getNodeCode());
        } else {
          apiResponse.recordError(500);
        }
      } catch (Exception e) {
        log.error("删除错误,errMsg==={}", e.getMessage());
        e.printStackTrace();
        apiResponse.recordError(500);
      }
    } else {
      apiResponse.recordError(500);
    }
    return apiResponse;
  }


  @ApiOperation("根据字典编码获取字典项信息")
  @ResponseBody
  @RequestMapping(value = "findDictData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findDictData(@ApiParam("{\n"
      + "  \"dictCode\":\"字典编码\"\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("数据字典项，根据字典编码获取字典项 ==== 参数 {" + jsonObject.toString() + "}");
    User user=jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    try {
      String dictCode = jsonObject.getString("dictCode");
      if (StringUtils.isNotBlank(dictCode)) {
        List<DictItem> dictItem = dictItemService.getDictItem(dictCode, user.getNodeCode());
        if (null != dictItem) {
          apiResponse.setData(dictItem);
        } else {
          apiResponse.recordError(500);
        }
      } else {
        apiResponse.recordError(500);
      }
    } catch (Exception e) {
      log.error("数据字典项查询错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @ApiOperation("根据字典编码获取指定字典项信息")
  @ResponseBody
  @RequestMapping(value = "findByDictCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findByDictCode(@ApiParam("{\n"
      + "  \"dictCode\":\"字典编码\",\n"
      + "  \"dictItemCode\":\"字典项编码\"\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    log.info("数据字典项，根据字典编码获取字典项 ==== 参数 {" + jsonObject.toString() + "}");
    User user=jwtUtil.getUserByToken(token);
    ApiResponse apiResponse = new ApiResponse();
    try {
      String dictCode = jsonObject.getString("dictCode");
      String dictdataCode = jsonObject.getString("dictItemCode");
      DictItem dictItem = dictItemService.findByDictCode(dictCode, dictdataCode, user.getNodeCode());
      if (null != dictItem) {
        apiResponse.setData(dictItem);
      } else {
        apiResponse.recordError(500);
      }
    } catch (Exception e) {
      log.error("数据字典项查询错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
      apiResponse.recordError(500);
    }
    return apiResponse;
  }

  @RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "根据字典id分页查询字典项数据")
  public ApiResponse queryPage(@ApiParam("{\n"
      + "  \"current\":\"当前页码\",\n"
      + "  \"size\":\"当前页数据条数\",\n"
      + "  \"id\":\"取左侧字典id,\"\n"
      + "}") @RequestBody JSONObject jsonObject, @RequestHeader("token") String token) {
    ApiResponse response = new ApiResponse();
    log.debug("字典项分页查询，参数param==={" + jsonObject.toString() + "}");
    if (null != jsonObject) {
      try {
        User user = jwtUtil.getUserByToken(token);
        if (null != user) {
          jsonObject.put("nodeCode", user.getNodeCode());
        }
        Map<String, Object> result = dictItemService.queryPage(jsonObject);
        response.setData(result);
        response.setCode(200);
      } catch (Exception e) {
        log.error("字典项分页查询失败,errMsg==={}" + e.getMessage());
        response.recordError(500);
      }
    } else {
      response.recordError(500);
    }
    return response;
  }
}
