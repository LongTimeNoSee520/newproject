package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.ResourcesForm;
import com.zjtc.service.ResourcesFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ResourcesForm的路由接口服务
 * 
 * @author zengqingsong
 *
 */
@RestController
@RequestMapping("resourcesForm")
@Api(tags = "资源表单api")
@Slf4j
public class ResourcesFormController {

	/** ResourcesFormService服务 */
	@Autowired
	private ResourcesFormService resourcesFormService;

	@RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "分页查询xxx内容")
	public ApiResponse queryPage(@RequestBody JSONObject jsonObject) {
		log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
		ApiResponse apiResponse = new ApiResponse();
		if (null != jsonObject) {
			try {
				Page<ResourcesForm> result = resourcesFormService.queryPage(jsonObject);
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
	 * 插入ResourcesForm属性不为空的数据方法
	 * @param
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "新增")
	@RequestMapping(value = "add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse add(@RequestHeader("token")String token,@RequestBody JSONObject jsonObject) {
		log.info("新增==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		if (null != jsonObject) {
			try {
				boolean result = resourcesFormService.saveModel(jsonObject);
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

	/**
	 * 修改
	 * @param
	 * @return
	 */
    @ResponseBody
    @ApiOperation(value = "修改")
    @RequestMapping(value = "edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse edit(@RequestHeader("token")String token,@RequestBody JSONObject jsonObject) {
		log.info("修改==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		if (null != jsonObject) {
			try {
				boolean result = resourcesFormService.updateModel(jsonObject);
				if (result) {
					apiResponse.setMessage(ResponseMsgConstants.OPERATE_SUCCESS);
				} else {
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
	 * 通过id删除ResourcesForm数据方法
	 * @param
	 * @return
	 */
    @ResponseBody
    @ApiOperation(value = "删除")
    @RequestMapping(value = "remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ApiResponse remove(@RequestHeader("token") String token, @RequestBody JSONObject jsonObject) {
		log.info("删除 ==== 参数{" + jsonObject != null ? jsonObject.toString() : "null" + "}");
		ApiResponse apiResponse = new ApiResponse();
		if (null != jsonObject) {
			try {
				boolean result = resourcesFormService.deleteModel(jsonObject);
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
}
