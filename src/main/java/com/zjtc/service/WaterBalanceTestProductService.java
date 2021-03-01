package com.zjtc.service;


import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.User;
import com.zjtc.model.WaterBalanceTestProduct;
import com.zjtc.model.vo.WaterBalanceTestProductVO;
import java.util.List;

/**
 * @author lianghao
 * @date 2021/02/26
 */
public interface WaterBalanceTestProductService extends IService<WaterBalanceTestProduct> {
  /**更新*/
	void updateNotNull(WaterBalanceTestProduct product);
  /**导入时新增*/
  void add(List<WaterBalanceTestProductVO> products, User user, String balanceTestId);
}
