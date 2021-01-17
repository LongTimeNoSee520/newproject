package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.RefundOrRefundMapper;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.service.RefundOrRefundService;
import org.springframework.stereotype.Service;

/**
 * RefundOrRefund的服务接口的实现类
 * 
 * @author 
 *
 */
@Service
public class RefundOrRefundServiceImpl extends ServiceImpl<RefundOrRefundMapper, RefundOrRefund> implements
		RefundOrRefundService {

	@Override
	public boolean saveModel(JSONObject jsonObject) {
		RefundOrRefund entity=jsonObject.toJavaObject(RefundOrRefund.class);
		boolean result=this.insert(entity);
		return result;
	}

	@Override
	public boolean updateModel(JSONObject jsonObject) {
		RefundOrRefund entity=jsonObject.toJavaObject(RefundOrRefund.class);
		boolean result=this.updateById(entity);
		return result;
	}

	@Override
	public boolean deleteModel(JSONObject jsonObject) {
		RefundOrRefund entity=jsonObject.toJavaObject(RefundOrRefund.class);
		boolean result=this.deleteById(entity);
		return result;
	}

	@Override
	public Page<RefundOrRefund> queryPage(JSONObject jsonObject) {
		RefundOrRefund entity=jsonObject.toJavaObject(RefundOrRefund.class);
    	Page<RefundOrRefund> page = new Page<RefundOrRefund>(jsonObject.getInteger("current"),jsonObject.getInteger("size"));
        page.setSearchCount(true);
        page.setOptimizeCountSql(true);
        EntityWrapper<RefundOrRefund> eWrapper = new EntityWrapper<RefundOrRefund>(entity);
		Page<RefundOrRefund> result=this.selectPage(page,eWrapper);
		return result;
	}

}