package service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import mapper.WaterUsePayInfoMapper;
import service.WaterUsePayInfoService;
import entity.WaterUsePayInfo;

/**
 * WaterUsePayInfo的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class WaterUsePayInfoServiceImpl extends ServiceImpl<WaterUsePayInfoMapper, WaterUsePayInfo> implements WaterUsePayInfoService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		WaterUsePayInfo entity=jsonObject.toJavaObject(WaterUsePayInfo.class);
		boolean result=this.insert(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		WaterUsePayInfo entity=jsonObject.toJavaObject(WaterUsePayInfo.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		WaterUsePayInfo entity=jsonObject.toJavaObject(WaterUsePayInfo.class);
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<WaterUsePayInfo> queryPage(JSONObject jsonObject) {
		WaterUsePayInfo entity=jsonObject.toJavaObject(WaterUsePayInfo.class);
    	Page<WaterUsePayInfo> page = new Page<WaterUsePayInfo>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<WaterUsePayInfo> eWrapper = new EntityWrapper<WaterUsePayInfo>(entity);
		Page<WaterUsePayInfo> result=this.selectPage(page,eWrapper);
		return result;
	}

}