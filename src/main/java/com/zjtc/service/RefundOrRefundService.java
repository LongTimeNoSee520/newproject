package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.User;
import java.util.List;


/**
 * RefundOrRefund的服务接口
 * 
 * @author 
 *
 */
public interface RefundOrRefundService extends IService<RefundOrRefund> {

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
	* 分页所有
	* @return
	*/
	List<RefundOrRefund> queryAll(JSONObject jsonObject);

	/**
	 * 审核
	 * @param jsonObject
	 * @param user
	 * @return
	 */
	boolean audit(JSONObject jsonObject, User user);

	/**
	 * 撤销
	 * @param jsonObject
	 * @return
	 */
	boolean	revoke(JSONObject jsonObject);

	/**
	 * 判断当前数据是否有退缴费记录
	 * @param payId   缴费记录id
	 * @param nodeCode 节点编码
	 * @return
	 */
	boolean auditCount(String payId,String nodeCode);
}
