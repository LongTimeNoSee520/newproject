package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.ImportLogMapper;
import com.zjtc.model.ImportLog;
import com.zjtc.model.User;
import com.zjtc.service.ImportLogService;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/25
 */

@Service
public class ImportLogServiceImpl extends ServiceImpl<ImportLogMapper, ImportLog> implements
		ImportLogService {

	@Override
	public boolean saveModel(ImportLog importLog) {
		boolean result=this.insert(importLog);
		return result;
	}

	@Override
	public Map<String, Object> queryPage(User user, JSONObject jsonObject) {

		int current = jsonObject.getInteger("current");//当前页
		int size = jsonObject.getInteger("size");//每页条数
		String nodeCode = user.getNodeCode();
		Map<String, Object> map = new HashMap();
		map.put("current", current);
		map.put("size", size);
		map.put("nodeCode", nodeCode);

		/**查出满足条件的共有多少条*/
		int num = this.baseMapper.queryNum(map);
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("total", num);//满足条件的总条数
		result.put("size", size);//每页条数
		result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
		result.put("current", current);//当前页

		/**查出满足条件的数据*/
		List<ImportLog> importLogs = this.baseMapper.queryPage(map);
		result.put("records", importLogs);
		return result;
	}


}