package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.ImportLog;
import com.zjtc.model.User;
import java.util.Map;

/**
 * 文件导入日志接口
 * @author lianghao
 * @date 2020/12/23
 */
public interface ImportLogService extends IService<ImportLog> {

	/**
	* 新增
	* @param importLog
	* @return
	*/
	boolean add(ImportLog importLog);

	/**
	 * 分页查询
	 * @param user，jsonObject
	 * @return
	 */
	Map<String,Object> queryPage(User user, JSONObject jsonObject);
}
