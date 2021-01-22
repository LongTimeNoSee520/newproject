package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.plugins.Page;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.service.WaterUsePayInfoService;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
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
 * WaterUsePayInfo的路由接口服务
 * 
 * @author zengqingsong
 *
 */
@RestController
@RequestMapping("waterUsePayInfo")
@Api(description = "xxx rest服务")
@Slf4j
public class WaterUsePayInfoController {
	@Autowired
	private JWTUtil jwtUtil;

	/** WaterUsePayInfoService服务 */
	@Autowired
	private WaterUsePayInfoService waterUsePayInfoService;

	@RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "分页查询xxx内容")
	public ApiResponse queryPage(@RequestBody JSONObject jsonObject,
			@ApiParam("{\n"
					+ "  \"current\":\"当前页，必填\",\n"
					+ "  \"size\":\"当前页数据条数，必填\",\n"
					+ "  \"unitName\":\"单位名称\",\n"
					+ "  \"unitCode\":\"单位编号\",\n"
					+ "  \"countYear\":\"年，必填\",\n"
					+ "  \"countQuarter\":\"季度，必填\",\n"
					+ "  \"payStatus\":\"缴费状态：0：未缴费，1已缴费\",\n"
					+ "  \"actualAmount\":\"金额\",\n"
					+ "  \"userType\":\"用户类型\",\n"
					+ "  \"waterMeterCode\":\"水表档案号\",\n"
					+ "  \"editedActual\":\"是否调整,0否，1是\",\n"
					+ "  \"payType\":\"付款方式：2现金,3转账\"\n"
					+ "}") @RequestHeader("token") String token) {
		log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
		ApiResponse apiResponse = new ApiResponse();
		User user=jwtUtil.getUserByToken(token);
		if (null != jsonObject && null !=user) {
			try {
				jsonObject.put("userId",user.getId());
				Map<String,Object> result = waterUsePayInfoService.queryPage(jsonObject);
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
	 * 插入WaterUsePayInfo属性不为空的数据方法
	 * @param
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "保存")
	@RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse add(@RequestHeader("token")String token,@ApiParam("{\n"
			+ "  \"描述：\"\n"
			+ "\n"
			+ "}") @RequestBody JSONObject jsonObject) {
		log.info("新增==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		if (null != jsonObject) {
			try {
				boolean result = waterUsePayInfoService.updateModel(jsonObject);
				if (result) {
					apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
				} else {
					apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
				}
			} catch (Exception e) {
				log.error("新增错误,errMsg==={}", e.getMessage());
				e.printStackTrace();
				apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
			}
		} else {
			apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
		}
		return apiResponse;
	}


	@ResponseBody
	@ApiOperation(value = "重算加价")
	@RequestMapping(value = "initPayInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse initPayInfo(@RequestHeader("token") String token,@ApiParam("{\n"
			+ "  \"countYear\":\"年，必填\",\n"
			+ "  \"countQuarter\":\"季度，必填\",\n"
			+ "  \"unitIds\":[单位id数组 取字段useWaterUnitId的值]\n"
			+ "  \n"
			+ "}") @RequestBody JSONObject jsonObject) {
		log.info("重算加价， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		User user=jwtUtil.getUserByToken(token);
		if (null != jsonObject && null !=user) {
			try {
				jsonObject.put("nodeCode",user.getNodeCode());
				boolean result = waterUsePayInfoService.initPayInfo(jsonObject);
				if (result) {
					apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
				} else {
					apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
				}
			} catch (Exception e) {
				log.error("重算加价错误,errMsg==={}", e.getMessage());
				e.printStackTrace();
				apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
			}
		} else {
			apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
		}
		return apiResponse;
	}

	@ResponseBody
	@ApiOperation(value = "发起减免单")
	@RequestMapping(value = "toStartReduction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse toStartReduction(@RequestHeader("token") String token,@ApiParam("") @RequestBody JSONObject jsonObject) {
		log.info("发起减免单， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		User user=jwtUtil.getUserByToken(token);
		if (null != jsonObject && null !=user) {
			try {
				jsonObject.put("nodeCode",user.getNodeCode());
				ApiResponse result = waterUsePayInfoService.toStartReduction(jsonObject,user);
				if (result.getCode()!=200) {
					return result;
				}
			} catch (Exception e) {
				log.error("发起减免单,errMsg==={}", e.getMessage());
				e.printStackTrace();
				apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
			}
		} else {
			apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
		}
		return apiResponse;
	}

	@ResponseBody
	@ApiOperation(value = "发起退款单")
	@RequestMapping(value = "toStartRefund", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse toStartRefund(@RequestHeader("token") String token,@ApiParam("") @RequestBody JSONObject jsonObject) {
		log.info("发起退款单， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		User user=jwtUtil.getUserByToken(token);
		if (null != jsonObject && null !=user) {
			try {
				ApiResponse result = waterUsePayInfoService.toStartRefund(jsonObject,user);
				if (result.getCode()!=200) {
					return result;
				}
			} catch (Exception e) {
				log.error("发起退款单,errMsg==={}", e.getMessage());
				e.printStackTrace();
				apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
			}
		} else {
			apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
		}
		return apiResponse;
	}

	@ResponseBody
	@ApiOperation(value = "查询退缴费第一个提交流程的角色信息")
	@RequestMapping(value = "firstRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse firstRole(@RequestHeader("token") String token,@ApiParam("") @RequestBody JSONObject jsonObject) {
		log.info("查询， 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		User user=jwtUtil.getUserByToken(token);
		if (null != jsonObject && null !=user) {
			try {
				List<Map<String, Object>> result = waterUsePayInfoService.firstRole(jsonObject,user);
				apiResponse.setData(result);
			} catch (Exception e) {
				log.error("查询,errMsg==={}", e.getMessage());
				e.printStackTrace();
				apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
			}
		} else {
			apiResponse.recordError(ResponseMsgConstants.OPERATE_FAIL);
		}
		return apiResponse;
	}
}
