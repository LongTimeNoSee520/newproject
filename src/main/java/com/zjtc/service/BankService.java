package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Bank;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
public interface BankService extends IService<Bank> {


  /**
   * 删除部门时删除关联的银行
   *
   * @param id 部门id
   * @return 是否删除成功
   */
  boolean deletedBank(String id);

  /**
   * 批量删除银行信息
   *
   * @param ids 部门id
   * @return 是否删除成功
   */
  boolean deletedBank(List<String> ids);


  /**
   * 批量添加银行
   *
   * @param bank 银行实体
   * @return 是否添加成功
   */
  boolean insertBank(List<Bank> bank, String useWaterUnitId, String nodeCode);

  /**
   * 批量修改银行
   *
   * @param bank 银行实体
   * @return 是否修改成功
   */
  boolean updateBank(List<Bank> bank, String useWaterUnitId, String nodeCode);

  /**
   * 银行信息查询
   *
   * @param useWaterUnitId 当前单位id
   * @return 银行结果集
   */
  List<Bank> selectBank(String useWaterUnitId, String nodeCode);
}
