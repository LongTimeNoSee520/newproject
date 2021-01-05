package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterPlanMapper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.service.UseWaterPlanService;
import org.springframework.stereotype.Service;

/**
 * TWUseWaterPlan的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class UseWaterPlanServiceImpl extends ServiceImpl<UseWaterPlanMapper, UseWaterPlan> implements
		UseWaterPlanService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		UseWaterPlan entity=jsonObject.toJavaObject(UseWaterPlan.class);
		boolean result=this.insert(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		UseWaterPlan entity=jsonObject.toJavaObject(UseWaterPlan.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		UseWaterPlan entity=jsonObject.toJavaObject(UseWaterPlan.class);
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<UseWaterPlan> queryPage(JSONObject jsonObject) {
		UseWaterPlan entity=jsonObject.toJavaObject(UseWaterPlan.class);
    	Page<UseWaterPlan> page = new Page<UseWaterPlan>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<UseWaterPlan> eWrapper = new EntityWrapper<UseWaterPlan>(entity);
		Page<UseWaterPlan> result=this.selectPage(page,eWrapper);
		return result;
	}

}