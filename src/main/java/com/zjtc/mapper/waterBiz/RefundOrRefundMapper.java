package com.zjtc.mapper.waterBiz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.RefundOrRefund;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * RefundOrRefund的Dao接口
 * 
 * @author 
 *
 */
@Mapper
public interface RefundOrRefundMapper extends BaseMapper<RefundOrRefund> {
  List<RefundOrRefund> queryAll(JSONObject jsonObject);

  /**
   * 分页
   */
  List<RefundOrRefund> queryPage(JSONObject jsonObject);

  /**
   * 分页查询出的数据总条数
   * @param jsonObject
   * @return
   */
  long queryListTotal(JSONObject jsonObject);

}