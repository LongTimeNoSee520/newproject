package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterUnitInvoice;
import com.zjtc.model.User;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 计划用水户发票表
 */
public interface UseWaterUnitInvoiceService extends IService<UseWaterUnitInvoice> {

	/**
	* 批量新增
	* @param list 数据集
	* @return 响应结果
	*/
	ApiResponse saveModel(List<String> list,User user);

	/**
	* 开票登记
	* @param unitInvoice 发票实体
	* @return 响应结果
	*/
  ApiResponse updateModel(UseWaterUnitInvoice unitInvoice,User user);

	/**
	* 删除
	* @param ids 发票id集
	* @return 响应结果
	*/
  ApiResponse deleteModel(List<String> ids,User user);

  /**
   * 作废
   * @param ids  发票id集
   * @return 响应结果
   */
  ApiResponse abolish(List<String> ids,User user);

  /**
   * 取消作废
   * @param ids  发票id集
   * @return 响应结果
   */
  ApiResponse cancelAbolish(List<String> ids,User user);

    /**
   * 重置发票
   * @param frontId 当前id
   * @param rearId 交换id
   * @return 响应结果
   */
  ApiResponse exchange(String frontId,String rearId,User user) throws Exception;

  /**
   * 移交票段
   * @param begin 开始票段
   * @param end 结束票段
   * @param personId 所领取人id
   * @return 响应结果
   */
  ApiResponse shift(String begin,String end,String personId,User user);

  /**
   * 发票标记
   * @param ids 发票id集
   * @return 响应结果
   */
  ApiResponse sign(List<String> ids, Date receiveTime);


  /**
   * 分页查询
   * @param jsonObject 参数
   * @param nodeCode 节点编码
   * @param loginId 登录人id
   * @return 响应结果
   */
  ApiResponse queryPage(JSONObject jsonObject,String nodeCode,String loginId);

  /**
   * 查询未被使用的发票编号
   * @return 集合
   */
  List<Map<String,Object>> selectInvoices(String loginId,String nodeCode,String invoiceNumber);

  /**
   * 更新发票的单位信息
   * @param useWaterUnitInvoice 发票实体
   * @param userName 经手人
   * @return 结果集
   */
  ApiResponse updateInvoicesUnitMessage(UseWaterUnitInvoice useWaterUnitInvoice,String userName,String nodeCode);

  /**
   * 导出发票
   * @param jsonObject 参数
   * @param request 请求
   * @param response 转发
   */
  void export(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response, User user);
}
