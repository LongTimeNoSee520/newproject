package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.PayInfoPrint;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;

/**
 * @author yuyantian
 * @date 2021/5/17
 * @description
 */
public interface PayInfoPrintService  extends IService<PayInfoPrint> {

  /**
   * 批量删除
   * @param jsonObject
   * @return
   */
  boolean deleteBatch(JSONObject jsonObject);

  /**
   * 分页查询打印记录
   * @param jsonObject
   * @return
   */
  Map<String,Object> queryPage(JSONObject jsonObject);

  /**
   * 根据缴费id查询打印记录
   * @param payId 缴费id
   * @return
   */
  PayInfoPrint selectPrintMess(String payId);

  /**
   * 按规则生成打印编号
   * @param unitCode 单位编号
   * @param countYear 年份
   * @param countQuarter 季度
   * @return
   */
  String  createPrintNum(String unitCode, String countYear, String countQuarter);

  /**
   * 打印催缴通知成功后，修改打印状态
   * @param list
   * @return
   */
  boolean updatePrinted(List<String> list, User user);

}
