package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.UseWaterOriginalPlan;
import java.util.List;
import java.util.Map;

/**
 * UseWaterOriginalPlan的服务接口
 * 
 * @author 
 *
 */
public interface UseWaterOriginalPlanService extends IService<UseWaterOriginalPlan> {

	/**
	* 保存
	* @param jsonObject
	* @return
	*/
	boolean saveModel(JSONObject jsonObject);
	/**
	 * 编制
	 * @param jsonObject
	 * @return
	 */
	boolean saveOriginal(JSONObject jsonObject);
	/**
	* 修改
	* @param jsonObject
	* @return
	*/
	boolean updateModel(JSONObject jsonObject);

	/**
	* 删除
	* @param jsonObject
	* @return
	*/
	boolean deleteModel(JSONObject jsonObject);

	/**
	* 分页查询
	* @return
	*/
	Page<UseWaterOriginalPlan> queryPage(JSONObject jsonObject);

	/**
	 * 获取本年度初始化编制信息
	 * @param jsonObject
	 * @return
	 */
  List<Map<String,Object>> goPlanning(JSONObject jsonObject);

}
