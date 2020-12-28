package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.Finance;
import com.zjtc.model.UseWaterUnit;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/24
 */
public interface FinanceService extends IService<Finance> {


  /**
   * 批量新增单位
   *
   * @param finances 加价开票记录实体
   * @param nodeCode 节点编码
   * @return 响应状态
   */
  ApiResponse insertFinance(List<Finance> finances, String nodeCode);

  /**
   * 修改单位
   *
   * @param finances 加价开票记录实体
   * @return 响应状态
   */
  ApiResponse updateFinance(List<Finance> finances);


  /**
   * 批量删除单位
   *
   * @param id 单位id
   * @return 响应状态
   */
  ApiResponse   deletedFinance(List<String> id);

  /**
   * 单位分页查询
   *
   * @param jsonObject 分页数据
   * @param nodeCode   节点编码
   * @return 分页查询结果
   */
  ApiResponse queryPageFinance(JSONObject jsonObject, String nodeCode);

  /**
   * 统计已开票和未开票金额
   *
   * @param nodeCode 节点编码
   * @return 响应结果
   */
  ApiResponse countMoney(JSONObject jsonObject,String nodeCode);
}
