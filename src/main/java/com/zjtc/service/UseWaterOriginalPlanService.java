package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.Algorithm;
import com.zjtc.model.UseWaterOriginalPlan;
import com.zjtc.model.User;
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
	* 老户/新户保存
	* @param jsonObject
	* @return
	*/
	ApiResponse save(JSONObject jsonObject);
	/**
	 * 老户/新户编制
	 * @param jsonObject
	 * @return
	 */
	ApiResponse saveOriginal(JSONObject jsonObject, User user);
	/**
	 * 新户编制
	 * @param jsonObject
	 * @return
	 */
	ApiResponse saveOriginalNew(JSONObject jsonObject, User user);
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
	 * 获取本年度初始化编制信息老户
	 * @param jsonObject
	 * @return
	 */
  List<Map<String,Object>> goPlanningOld(JSONObject jsonObject);
	/**
	 * 获取本年度初始化编制信息新户
	 * @param jsonObject
	 * @return
	 */
	List<Map<String,Object>> goPlanningNew(JSONObject jsonObject);
	/**
	 * 老户调整【下年年终计划(基础)】,重新计算【各季度计划(基础)】
	 */
	Map<String,Object> getOldByNextYearBase(JSONObject jsonObject,User user);
	/**
	 * 老户调整【三年平均水量】时,重新计算【下年初始计划(基础)】
	 */
	Map<String,Object> getOldResultByThreeYearAvg(JSONObject jsonObject,User user);
	/**
	 *老户勾选【扣加价】，选择【水平衡】、【创建】，重新计算【下年年终计划(基础)】
	 */
	Map<String,Object> getResultBycheck(JSONObject jsonObject,User user);
	/**
	 * 新户调整【下年年终计划(基础)】,重新计算【各季度计划(基础)】
	 */
	Map<String,Object> getNewByNextYearBase(JSONObject jsonObject,User user);
	/**
	 *新户调整"三年平均水量"时,重新计算【下年初始计划(基础)】
	 */
	Map<String,Object> getNewResultByThreeYearAvg(JSONObject jsonObject,User user);

	/**
	 * 算法调整后，删除所有未编制的数据
	 * */
	boolean  deleteAllNotplaned(String nodeCode);
}
