package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterOriginalPlan;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	ApiResponse save(User user,JSONObject jsonObject);
	/**
	 * 老户/新户编制
	 * @param jsonObject
	 * @return
	 */
	ApiResponse saveOriginal(JSONObject jsonObject, User user);


	/**
	* 老户分页查询
	* @return
	*/
	Map<String,Object> queryPageOld(JSONObject jsonObject);

	/**
	 * 获取本年度初始化编制信息老户
	 * @param jsonObject
	 * @return
	 */
  List<Map<String,Object>> goPlanningOld(JSONObject jsonObject);
	/**
	 * 获取本年度初始化编制信息新户
	 * @param userId
	 * @param nodeCode
	 * @return
	 */
	List<Map<String,Object>> goPlanningNew(String userId,String nodeCode);
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
	 *新户调整第三年编制基础，重新计算【三年平均水量】
	 */
	Map<String,Object> getNewByBaseWaterAmount(JSONObject jsonObject,User user);
	/**
	 *新户调整水价，重新计算"下年初计划(基础)"
	 */
	Map<String,Object> getNewByNowPrice(JSONObject jsonObject,User user);
	/**
	 * 算法调整后，删除所有未编制的数据
	 * */
	boolean  deleteAllNotplaned(String nodeCode);

	/**
	 * 导出老户数据
	 * @param jsonObject
	 * @param request
	 * @param response
	 */
	void exportOldData(User user,JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 导出新户数据
	 * @param jsonObject
	 * @param request
	 * @param response
	 */
	void exportNewData(User user,JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
}
