package com.zjtc.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.zjtc.model.RefundOrRefund;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * RefundOrRefund的Dao接口
 * 
 * @author 
 *
 */
@Mapper
public interface RefundOrRefundMapper extends BaseMapper<RefundOrRefund> {
  List<Map<String,Object>> queryPage(JSONObject jsonObject);

}