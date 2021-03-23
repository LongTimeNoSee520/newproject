package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.WaterSavingUnitBase;
import java.util.List;

/**
 * WaterSavingUnitBase的服务接口
 * 
 * @author 
 *
 */
public interface WaterSavingUnitBaseService extends IService<WaterSavingUnitBase> {

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
	Page<WaterSavingUnitBase> queryPage(JSONObject jsonObject);
	/**
	 * 编辑或删除
	 */
	boolean updateOrDelete (List<WaterSavingUnitBase> list,String savingId );
}
