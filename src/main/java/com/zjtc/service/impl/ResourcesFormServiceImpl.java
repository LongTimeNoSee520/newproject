package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterSys.ResourcesFormMapper;
import com.zjtc.model.ResourcesForm;
import com.zjtc.service.ResourcesFormService;
import org.springframework.stereotype.Service;

/**
 * ResourcesForm的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class ResourcesFormServiceImpl extends ServiceImpl<ResourcesFormMapper, ResourcesForm> implements ResourcesFormService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		ResourcesForm entity=jsonObject.toJavaObject(ResourcesForm.class);
		boolean result=this.save(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		ResourcesForm entity=jsonObject.toJavaObject(ResourcesForm.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		ResourcesForm entity=jsonObject.toJavaObject(ResourcesForm.class);
		boolean result=this.removeById(entity);
		return result;
	}

	@Override
	public Page<ResourcesForm> queryPage(JSONObject jsonObject) {

		return null;
	}

}