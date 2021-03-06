package com.zjtc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.WaterBalanceTestProductMapper;
import com.zjtc.model.User;
import com.zjtc.model.WaterBalanceTestProduct;
import com.zjtc.model.vo.WaterBalanceTestProductVO;
import com.zjtc.service.WaterBalanceTestProductService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/02/26
 */
@Service
public class WaterBalanceTestProductServiceImpl extends
		ServiceImpl<WaterBalanceTestProductMapper, WaterBalanceTestProduct> implements
		WaterBalanceTestProductService {


	@Override
	public void updateNotNull(WaterBalanceTestProduct product) {
   this.baseMapper.updateNotNull(product);
	}

	@Override
	public void add(List<WaterBalanceTestProductVO> products, User user, String balanceTestId) {
		for (WaterBalanceTestProductVO productVO : products){
         WaterBalanceTestProduct product = new WaterBalanceTestProduct();
         product.setProductName(productVO.getProductName());
         product.setDiffToQuota(productVO.getDiffToQuota());
         product.setPerUseAmount(productVO.getPerUseAmount());
         product.setYearPlan(productVO.getYearPlan());
         product.setNodeCode(user.getNodeCode());
         product.setCreateTime(new Date());
         product.setBalanceTestId(balanceTestId);
         product.setDeleted("0");
         this.insert(product);
		}
	}
}