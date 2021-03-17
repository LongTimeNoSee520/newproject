package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
  ApiResponse updateModel(JSONObject jsonObject,User user);

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
   * 修改实收
   * @param id
   * @param actualAmount
   * @return
   */
  boolean updateActualAmount(String id,double actualAmount);
  /**
   * 查询退缴费第一个提交流程的角色信息
   */
  List<Map<String, Object>> firstRole( User user);

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

  /**
   *查询催缴通知列表
   * @param jsonObject
   * @return
   */
  Map<String,Object> selectPayNotice(JSONObject jsonObject);

  /**
   * 短信发送
   * @param jsonObject
   * @return
   */
  boolean send(JSONObject jsonObject,User user) throws Exception;

  /**
   * 导出查询结果
   * @param jsonObject
   * @param request
   * @param response
   */
  void exportQueryData(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

  /**
   * 导出用户信息
   * @param jsonObject
   * @param request
   * @param response
   */
  void exportUser(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response)
      throws IOException;

  /**
   *导出计划用水户超计划情况汇总
   * @param jsonObject
   * @param request
   * @param response
   */
  void exportPayInfo(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

  /**
   * 导出本行数据
   * @param jsonObject
   * @param request
   * @param response
   */
  ApiResponse exportBankInfo(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response)
      throws IOException;

  /**
   * 导出他行数据
   * @param jsonObject
   * @param request
   * @param response
   */
  ApiResponse exportOtherBankInfo(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response)
      throws IOException;
}
