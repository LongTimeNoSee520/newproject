package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import java.util.Map;

/**
 * WaterUsePayInfo的服务接口
 * 
 * @author 
 *
 */
public interface WaterUsePayInfoService extends IService<WaterUsePayInfo> {

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
	Map<String,Object> queryPage(JSONObject jsonObject);
	/**
	 *重算加价
	 */
	boolean initPayInfo(JSONObject jsonObject);

	/**
	 * 发起退款单
	 * @param jsonObject
	 * @return
	 */
	boolean  toStartRefund(JSONObject jsonObject, User user);
	/**
	 *发起减免单
	 * @param jsonObject
	 * @return
	 */
	boolean  toStartReduction(JSONObject jsonObject, User user);

	/**
	 * 发票号作废，清除发票号，开票时间
	 * @param waterUsePayInfo
	 * @return
	 */
	boolean updateinvoiceNumRef(WaterUsePayInfo waterUsePayInfo);
}
