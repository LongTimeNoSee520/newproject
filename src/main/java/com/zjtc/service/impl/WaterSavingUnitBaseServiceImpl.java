package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.WaterSavingUnitBaseMapper;
import com.zjtc.model.WaterSavingUnitBase;
import com.zjtc.service.WaterSavingUnitBaseService;
import org.springframework.stereotype.Service;

/**
 * WaterSavingUnitBase的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class WaterSavingUnitBaseServiceImpl extends ServiceImpl<WaterSavingUnitBaseMapper, WaterSavingUnitBase> implements
		WaterSavingUnitBaseService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		WaterSavingUnitBase entity=jsonObject.toJavaObject(WaterSavingUnitBase.class);
		boolean result=this.insert(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		WaterSavingUnitBase entity=jsonObject.toJavaObject(WaterSavingUnitBase.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		WaterSavingUnitBase entity=jsonObject.toJavaObject(WaterSavingUnitBase.class);
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<WaterSavingUnitBase> queryPage(JSONObject jsonObject) {
		WaterSavingUnitBase entity=jsonObject.toJavaObject(WaterSavingUnitBase.class);
    	Page<WaterSavingUnitBase> page = new Page<WaterSavingUnitBase>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<WaterSavingUnitBase> eWrapper = new EntityWrapper<WaterSavingUnitBase>(entity);
		Page<WaterSavingUnitBase> result=this.selectPage(page,eWrapper);
		return result;
	}

}