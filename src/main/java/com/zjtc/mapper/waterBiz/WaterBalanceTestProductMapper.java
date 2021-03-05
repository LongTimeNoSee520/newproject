package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.WaterBalanceTestProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lianghao
 * @date 2021/02/26
 */
@Mapper
public interface WaterBalanceTestProductMapper extends BaseMapper<WaterBalanceTestProduct> {

  void updateNotNull(WaterBalanceTestProduct product);
}