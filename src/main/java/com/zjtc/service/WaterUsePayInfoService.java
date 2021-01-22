package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import java.util.List;
import java.util.Map;

/**
 * WaterUsePayInfo的服务接口
 *
 * @author
 */
public interface WaterUsePayInfoService extends IService<WaterUsePayInfo> {

  /**
   * 保存
   */
  boolean saveModel(JSONObject jsonObject);

  /**
   * 修改
   */
  boolean updateModel(JSONObject jsonObject);

  /**
   * 删除
   */
  boolean deleteModel(JSONObject jsonObject);

  /**
   * 分页查询
   */
  Map<String, Object> queryPage(JSONObject jsonObject);

  /**
   * 重算加价
   */
  boolean initPayInfo(JSONObject jsonObject);

  /**
   * 发起退款单
   */
  ApiResponse toStartRefund(JSONObject jsonObject, User user);

  /**
   * 发起减免单
   */
  ApiResponse toStartReduction(JSONObject jsonObject, User user);

  /**
   * 发票号作废，清除发票号，开票时间
   */
  boolean updateinvoiceNumRef(WaterUsePayInfo waterUsePayInfo);

  /**
   * 修改缴费记录
   *
   * @param id 主键id
   * @param moeny 退减免金额
   */
  boolean updateMoney(String id, double moeny);

  /**
   * 查询退缴费第一个提交流程的角色信息
   */
  List<Map<String, Object>> firstRole(JSONObject jsonObject, User user);

  /**
   * 查询当前单位所有未缴费记录
   *
   * @param unitId 单位id
   */
  List<Map<String, Object>> findPayBefor(String unitId);

  /**
   * 查询近3年加价记录
   *
   * @param unitId 单位id
   */
  List<Map<String, Object>> ThreePayMess(String unitId);
}
