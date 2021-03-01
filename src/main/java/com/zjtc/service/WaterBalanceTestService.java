package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.User;
import com.zjtc.model.WaterBalanceTest;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lianghao
 * @date 2021/02/25
 */
public interface WaterBalanceTestService extends IService<WaterBalanceTest> {

	/**
	* 修改
	* @param user,jsonObject
	* @return
	*/
	boolean updateModel(User user,JSONObject jsonObject);

	/**
	* 删除
	* @param ids
	* @return
	*/
	boolean deleteModel(List<String> ids);

	/**
	* 分页查询
	* @return
	*/
	Map<String, Object> queryPage(User user, JSONObject jsonObject);

	/**
	 * 数据导入
	 * @param user
	 * @param filePath
	 * @throws Exception
	 */
	void importData(User user, String filePath) throws Exception;

	/**
	 * 模板下载
	 * @param request
	 * @param response
	 */
	void downloadTemplate(HttpServletRequest request, HttpServletResponse response);
}
