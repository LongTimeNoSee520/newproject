package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.User;
import com.zjtc.model.WaterSavingUnit;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * WaterSavingUnit的服务接口
 *
 * @author
 *
 */
public interface WaterSavingUnitService extends IService<WaterSavingUnit> {

	/**
	* 保存
	* @param jsonObject
	* @return
	*/
	boolean saveModel(JSONObject jsonObject);

	/**
	* 修改
	* @param jsonObject
	* @return
	*/
	ApiResponse updateModel(JSONObject jsonObject,User user);

	/**
	* 删除
	* @param id
	* @return
	*/
	boolean deleteModel(String id);

	/**
	* 分页查询
	* @return
	*/
	Map<String, Object> queryPage(JSONObject jsonObject);

	/**
	 * 导入
	 */
	ApiResponse importExcel(MultipartFile file, User user)
			throws Exception;

	/**
	 * 模版下载
	 */
	void downloadTemplate(HttpServletRequest request, HttpServletResponse response);
}
