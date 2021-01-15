package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.RefundOrRefund;


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
	* 分页查询
	* @return
	*/
	Page<RefundOrRefund> queryPage(JSONObject jsonObject);
}
