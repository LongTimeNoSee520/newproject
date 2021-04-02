package com.zjtc.mapper.waterBiz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.FlowProcess;
import com.zjtc.model.RefundOrRefund;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * RefundOrRefund的Dao接口
 *
 * @author
 */
@Mapper
public interface RefundOrRefundMapper extends BaseMapper<RefundOrRefund> {

  List<RefundOrRefund> queryAll(JSONObject jsonObject);

  /**
   * 分页
   */
  List<RefundOrRefund> queryPage(@Param("json") JSONObject jsonObject, @Param("list") List<FlowProcess> list);

  /**
   * 分页查询出的数据总条数
   */
  long queryListTotal(@Param("json")JSONObject jsonObject, @Param("list") List<FlowProcess> list);
/**
 * 查询某条缴费记录是否有未走完的退减免单
 */
 long auditCount(@Param("payId") String payId,@Param("nodeCode") String nodeCode);
}