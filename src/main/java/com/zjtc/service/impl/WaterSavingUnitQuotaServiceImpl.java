package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.WaterSavingUnitQuotaMapper;
import com.zjtc.model.WaterSavingUnitQuota;
import com.zjtc.service.WaterSavingUnitQuotaService;
import org.springframework.stereotype.Service;
/**
 * WaterSavingUnitQuota的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class WaterSavingUnitQuotaServiceImpl extends
		ServiceImpl<WaterSavingUnitQuotaMapper, WaterSavingUnitQuota> implements
		WaterSavingUnitQuotaService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		WaterSavingUnitQuota entity=jsonObject.toJavaObject(WaterSavingUnitQuota.class);
		boolean result=this.insert(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		WaterSavingUnitQuota entity=jsonObject.toJavaObject(WaterSavingUnitQuota.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		WaterSavingUnitQuota entity=jsonObject.toJavaObject(WaterSavingUnitQuota.class);
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<WaterSavingUnitQuota> queryPage(JSONObject jsonObject) {
		WaterSavingUnitQuota entity=jsonObject.toJavaObject(WaterSavingUnitQuota.class);
    	Page<WaterSavingUnitQuota> page = new Page<WaterSavingUnitQuota>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<WaterSavingUnitQuota> eWrapper = new EntityWrapper<WaterSavingUnitQuota>(entity);
		Page<WaterSavingUnitQuota> result=this.selectPage(page,eWrapper);
		return result;
	}

}