package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.UseWaterPlanMapper;
import com.zjtc.model.UseWaterPlan;
import com.zjtc.model.UseWaterPlanAddWX;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.service.UseWaterPlanAddWXService;
import com.zjtc.service.UseWaterPlanService;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private UseWaterPlanAddWXService useWaterPlanAddWXService;

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

  @Override
  public UseWaterPlan selectUseWaterPlan(String nodeCode, String unitCode, Integer planYear) {

    return this.baseMapper.selectUseWaterPlan( nodeCode,  unitCode,  planYear);
  }

  @Override
  public UseWaterPlan selectNowPlan(String id) {
    UseWaterPlanAddWX useWaterPlanAddWX = useWaterPlanAddWXService.selectByIdAll(id);
    UseWaterPlan useWaterPlan = this.baseMapper
        .selectUseWaterPlanAll(useWaterPlanAddWX.getNodeCode(), getYear(),
            useWaterPlanAddWX.getUseWaterUnitId(), useWaterPlanAddWX.getUnitCode());
    return useWaterPlan;
  }

  //获取当前年份
  public static Integer getYear() {
    Calendar date = Calendar.getInstance();
    String year = String.valueOf(date.get(Calendar.YEAR));
    return Integer.valueOf(year);
  }
}