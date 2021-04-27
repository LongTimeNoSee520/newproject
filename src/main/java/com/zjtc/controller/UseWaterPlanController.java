package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjtc.base.constant.ResponseMsgConstants;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.User;
import com.zjtc.service.UseWaterPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
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
 * TWUseWaterPlan的路由接口服务
 * 
 * @author zengqingsong
 *
 */
@RestController
@RequestMapping("tWUseWaterPlan")
@Api(tags = "用水计划管理")
@Slf4j
public class UseWaterPlanController {

	/** TWUseWaterPlanService服务 */
	@Autowired
	private UseWaterPlanService useWaterPlanService;

	@RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "分页查询xxx内容")
	public ApiResponse queryPage(@RequestBody JSONObject jsonObject) {
		log.info("分页查询 ==== 参数{" + jsonObject.toJSONString() + "}");
		ApiResponse apiResponse = new ApiResponse();
		if (null != jsonObject) {
			try {
				Page<UseWaterPlan> result = useWaterPlanService.queryPage(jsonObject);
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
	 * 插入TWUseWaterPlan属性不为空的数据方法
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
				boolean result = useWaterPlanService.saveModel(jsonObject);
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
				boolean result = useWaterPlanService.updateModel(jsonObject);
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
	 * 通过id删除TWUseWaterPlan数据方法
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
				boolean result = useWaterPlanService.deleteModel(jsonObject);
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


  @RequestMapping(value = "selectNowPlan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation("用水计划调整 查看当前计划")
  public ApiResponse selectNowPlan(@ApiParam("   {\n"
      + "     \"id\":\"用水计划调整审核id\"\n"
      + "   }") @RequestBody JSONObject jsonObject,
      @RequestHeader("token") String token) {
    log.info("用水计划调整 查看当前计划,参数param==={" + jsonObject.toJSONString() + "}");
    ApiResponse response = new ApiResponse();

    if (null == jsonObject) {
      response.recordError("系统异常");
      return response;
    }
    String id = jsonObject.getString("id");
    try {
      UseWaterPlan useWaterPlan = useWaterPlanService.selectNowPlan(id);
     response.setData(useWaterPlan);
      return response;
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("用水计划调整 查看当前计划异常");
      log.error("用水计划调整 查看当前计划错误,errMsg==={}", e.getMessage());
      e.printStackTrace();
    }
    return response;
  }
}
