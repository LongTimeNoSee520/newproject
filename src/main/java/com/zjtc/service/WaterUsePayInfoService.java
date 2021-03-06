package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.User;
import com.zjtc.model.WaterUsePayInfo;
import com.zjtc.model.vo.WaterUsePayInfoVo;
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
  ApiResponse updateModel(JSONObject jsonObject, User user);


  /**
   * 分页查询
   */
  Map<String, Object> queryPage(JSONObject jsonObject,User user);

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
   */
  boolean updateActualAmount(String id, double actualAmount);

  /**
   * 查询退缴费第一个提交流程的角色信息
   */
  List<Map<String, Object>> firstRole(User user);

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
   * 查询催缴通知列表
   */
  Map<String, Object> selectPayNotice(JSONObject jsonObject);

  /**
   * 短信发送
   */
  boolean send(JSONObject jsonObject, User user) throws Exception;

  /**
   * 导出查询结果
   */
  ApiResponse exportQueryData(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response);

  /**
   * 导出用户信息
   */
  ApiResponse exportUser(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException;

  /**
   * 导出计划用水户超计划情况汇总
   */
  ApiResponse exportPayInfo(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response);

  /**
   * 导出本行数据
   */
  ApiResponse exportBankInfo(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException;

  /**
   * 导出他行数据
   */
  ApiResponse exportOtherBankInfo(User user, JSONObject jsonObject, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException;

  /**
   * 修改发票信息
   *
   * @param id 缴费记录id
   * @param invoiceId 发票id
   * @param invoiceNumber 发票号
   * @return boolean
   */
  boolean editInvoiceInfo(String id, String invoiceId, String invoiceNumber);

  /**
   * 打印汇总表1
   */
  Map<String, Object> printExPlan1(JSONObject jsonObject, User user);

  /**
   * 打印汇总表2
   */
  Map<String, Object> printExPlan2(JSONObject jsonObject, User user);

  /**
   * 打印催缴通知--预置打印编号
   */
  List<WaterUsePayInfoVo> printAdvice(List<WaterUsePayInfoVo> list, User user);

  /**
   * 打印催缴通知成功后，修改打印状态
   */
  boolean prinSuccess(JSONObject jsonObject,User user);

}
