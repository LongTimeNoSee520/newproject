package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.UseWaterOriginalPlanMapper;
import com.zjtc.model.UseWaterOriginalPlan;
import com.zjtc.service.UseWaterOriginalPlanService;
import org.springframework.stereotype.Service;
/**
 * TWUseWaterOriginalPlan的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class UseWaterOriginalPlanServiceImpl extends
		ServiceImpl<UseWaterOriginalPlanMapper, UseWaterOriginalPlan> implements
		UseWaterOriginalPlanService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		UseWaterOriginalPlan entity=jsonObject.toJavaObject(UseWaterOriginalPlan.class);
		boolean result=this.insert(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		UseWaterOriginalPlan entity=jsonObject.toJavaObject(UseWaterOriginalPlan.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		UseWaterOriginalPlan entity=jsonObject.toJavaObject(UseWaterOriginalPlan.class);
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<UseWaterOriginalPlan> queryPage(JSONObject jsonObject) {
		UseWaterOriginalPlan entity=jsonObject.toJavaObject(UseWaterOriginalPlan.class);
    	Page<UseWaterOriginalPlan> page = new Page<UseWaterOriginalPlan>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<UseWaterOriginalPlan> eWrapper = new EntityWrapper<UseWaterOriginalPlan>(entity);
		Page<UseWaterOriginalPlan> result=this.selectPage(page,eWrapper);
		return result;
	}

}