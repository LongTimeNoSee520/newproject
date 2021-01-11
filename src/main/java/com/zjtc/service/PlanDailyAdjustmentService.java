package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;
/**
 * @author lianghao
 * @date 2021/01/04
 */
public interface PlanDailyAdjustmentService extends IService<UseWaterPlan> {

	/**
	 * 分页查询
	 * @param user，jsonObject
	 * @return
	 */
	Map<String,Object> queryPage(User user, JSONObject jsonObject);

	/**
	 * 修改备注
	 * @param id,editType,remarks
	 * @return
	 */
  ApiResponse editRemarks(String id,String editType,String remarks);

	/**
	 * 计算增加计划的水量
	 * @param jsonObject
	 * @return
	 */
	ApiResponse numberAfterCalculation(JSONObject jsonObject);

	/**
	 * 计算调整
	 * @param jsonObject
	 * @return
	 */
	ApiResponse adjustPlan(User user, JSONObject jsonObject);

	/**
	 * 行内编辑修改调整计划
	 * @param useWaterPlanAdds
	 * @return
	 */
	ApiResponse editPlanAdd(User user, List<UseWaterPlanAdd> useWaterPlanAdds);

	/**
	 * 累加
	 * @param useWaterPlanAdd
	 * @return
	 */
	ApiResponse accumulate(User user,UseWaterPlanAdd useWaterPlanAdd);
	/**
	 * 修改打印状态
	 * @param printList
	 */
	boolean signPrinted(List<String> printList);

	/**
	 * 根据单位编号，查询单位信息和一、二水量前端回填
	 * @param unitCode
	 */
	List<Map<String,Object>> queryMessage(User user, String unitCode);

	ApiResponse initiateSettlement(User user,JSONObject jsonObject);

	/**
	 * 更新是否存在办结单状态
	 * @param
	 */
	void updateExistSettlement(String existSettlement, String unitCode, String nodeCode, Integer planYear);
}
