package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.ImportLogMapper;
import com.zjtc.model.ImportLog;
import com.zjtc.model.User;
import com.zjtc.service.ImportLogService;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/25
 */

@Service
public class ImportLogServiceImpl extends ServiceImpl<ImportLogMapper, ImportLog> implements
		ImportLogService {

	/**
	 * 附件存储目录
	 */
	@Value("${server.servlet-path}")
	private String contextPath;

	/**
	 * 上下文
	 */
	@Value("${file.preViewRealPath}")
	private String preViewRealPath;

	@Override
	public boolean add(ImportLog importLog) {
		boolean result=this.insert(importLog);
		return result;
	}

	@Override
	public Map<String, Object> queryPage(User user, JSONObject jsonObject) {

		int current = jsonObject.getInteger("current");//当前页
		int size = jsonObject.getInteger("size");//每页条数
		Map<String, Object> map = new HashMap();
		map.put("current", current);
		map.put("size", size);
		if (StringUtils.isNotBlank(jsonObject.getString("nodeCode"))) {
			map.put("nodeCode", jsonObject.getString("nodeCode"));
		}else{
			map.put("nodeCode", user.getNodeCode());
		}
		map.put("preViewRealPath",preViewRealPath + contextPath +"/" );

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