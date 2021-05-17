package com.zjtc.mapper.waterBiz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.PayInfoPrint;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yuyantian
 * @date 2021/5/17
 * @description
 */
@Mapper
public interface PayInfoPrintMapper extends BaseMapper<PayInfoPrint> {

  /**
   *
   * @param payId 缴费id
   * @return
   */
  PayInfoPrint selectPrintMess(@Param("payId") String payId);

  boolean updatePrinted(@Param("ids") List<String> list, @Param("user") User user);

  /**
   * 分页
   */
  List<Map<String,Object>> queryPage(JSONObject jsonObject);

  /**
   * 分页查询出的数据总条数
   * @param jsonObject
   * @return
   */
  long queryListTotal(JSONObject jsonObject);

}
