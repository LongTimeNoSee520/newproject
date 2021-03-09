package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
		boolean result=this.insert(entity);
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
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<ResourcesForm> queryPage(JSONObject jsonObject) {
		ResourcesForm entity=jsonObject.toJavaObject(ResourcesForm.class);
    	Page<ResourcesForm> page = new Page<ResourcesForm>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<ResourcesForm> eWrapper = new EntityWrapper<ResourcesForm>(entity);
		Page<ResourcesForm> result=this.selectPage(page,eWrapper);
		return result;
	}

}