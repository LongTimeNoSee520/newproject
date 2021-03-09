package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterSys.ResourcesFormRefMapper;
import com.zjtc.model.ResourcesFormRef;
import com.zjtc.service.ResourcesFormRefService;
import org.springframework.stereotype.Service;

/**
 * ResourcesFormRef的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class ResourcesFormRefServiceImpl extends ServiceImpl<ResourcesFormRefMapper, ResourcesFormRef> implements ResourcesFormRefService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		ResourcesFormRef entity=jsonObject.toJavaObject(ResourcesFormRef.class);
		boolean result=this.insert(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		ResourcesFormRef entity=jsonObject.toJavaObject(ResourcesFormRef.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		ResourcesFormRef entity=jsonObject.toJavaObject(ResourcesFormRef.class);
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<ResourcesFormRef> queryPage(JSONObject jsonObject) {
		ResourcesFormRef entity=jsonObject.toJavaObject(ResourcesFormRef.class);
    	Page<ResourcesFormRef> page = new Page<ResourcesFormRef>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<ResourcesFormRef> eWrapper = new EntityWrapper<ResourcesFormRef>(entity);
		Page<ResourcesFormRef> result=this.selectPage(page,eWrapper);
		return result;
	}

}