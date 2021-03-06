package com.zjtc.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import com.zjtc.service.ImportLogService;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

/**
 * @author lianghao
 * @date 2020/12/23
 */

@RestController
@RequestMapping("/importLog")
@Api(tags = "导入日志")
@Slf4j
public class ImportLogController {

	@Autowired
	private ImportLogService importLogService;

	@Autowired
	private JWTUtil jwtUtil;

	@RequestMapping(value = "queryPage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "分页查询导入日志内容")
	public ApiResponse queryPage(@RequestHeader("token") String token,
			@ApiParam("{\"current\":\"当前页,数字类型\",\n"
					+ "\"size\":\"每页条数,数字类型\",\n"
					+ "\"nodeCode\":\"节点编码\"\n"
					+ "}") @RequestBody JSONObject jsonObject) {
		log.info("分页查询导入日志 ==== 参数{" + jsonObject.toJSONString() + "}");
		ApiResponse response = new ApiResponse();
		if (null != jsonObject) {
			try {
				User user =  jwtUtil.getUserByToken(token);
				Map<String, Object> result = importLogService.queryPage(user,jsonObject);
				response.setData(result);
			} catch (Exception e) {
				log.error("导入日志分页条件查询失败,errMsg==={}" + e.getMessage());
				response.recordError(500);
			}
		} else {
			response.recordError("分页查询参数不能为空");
		}
		return response;
    }

}
