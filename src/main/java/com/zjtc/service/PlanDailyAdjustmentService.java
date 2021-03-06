package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAdd;
import com.zjtc.model.User;
import com.zjtc.model.vo.PlanDailyAdjustmentVO;
import com.zjtc.model.vo.PrintVO;
import com.zjtc.model.vo.SendListVO;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	boolean signPrinted(List<PrintVO> printList);

	/**
	 * 根据单位编号，查询单位信息和一、二水量前端回填
	 * @param unitCode
	 */
	List<Map<String,Object>> queryMessage(User user, String unitCode,String nodeCode);

	ApiResponse initiateSettlement(User user,JSONObject jsonObject) throws Exception;

	/**
	 * 更新是否存在办结单状态
	 * @param
	 */
	void updateExistSettlement(String existSettlement, String unitCode, String nodeCode, Integer planYear);
	/**
	 * 列表查询
	 * @param
	 */
	List<PlanDailyAdjustmentVO> queryList(User user, JSONObject jsonObject);

	/**
	 * 导出最新计划
	 * @param user
	 * @param planYear
	 * @param request
	 * @param response
	 * @return
	 */
	ApiResponse export(User user, Integer planYear,String nodeCode, HttpServletRequest request, HttpServletResponse response);


	/**
	 * 查询退缴费第一个提交流程的角色信息
	 */
	List<Map<String, Object>> secondAuditRole(String nodeCode,String changeType);

	/**
	 * 年计划自平通知
	 */
  void planAdjustNotification(User user, List<SendListVO> sendList, Integer year) throws Exception;
}
