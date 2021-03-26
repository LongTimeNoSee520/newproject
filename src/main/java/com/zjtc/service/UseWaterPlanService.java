package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.UseWaterPlan;

/**
 * TWUseWaterPlan的服务接口
 * 
 * @author 
 *
 */
public interface UseWaterPlanService extends IService<UseWaterPlan> {

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
	Page<UseWaterPlan> queryPage(JSONObject jsonObject);

  /**
   * 查询单位原始数据
   * @param nodeCode 节点编码
   * @param planYear 年份
   * @param waterUnitId 单位id
   * @return 结果集
   */
  UseWaterPlan selectUseWaterPlanAll(String nodeCode,Integer planYear,String waterUnitId,String unitCode);
}
