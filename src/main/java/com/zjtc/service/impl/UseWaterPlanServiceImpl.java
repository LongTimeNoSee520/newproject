package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.UseWaterPlanMapper;
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
		boolean result=this.save(entity);
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
		boolean result=this.removeById(entity);
		return result;
	}

	@Override
	public Page<UseWaterPlan> queryPage(JSONObject jsonObject) {

		return null;
	}

  @Override
  public UseWaterPlan selectUseWaterPlanAll(String nodeCode, Integer planYear, String waterUnitId,String unitCode) {
	  return this.baseMapper.selectUseWaterPlanAll(nodeCode,planYear,waterUnitId,unitCode);
  }
}