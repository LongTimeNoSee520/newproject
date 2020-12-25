package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Bank;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 银行信息
 */
@Mapper
public interface BankMapper extends BaseMapper<Bank> {

  /**
   * 判断账户在当前单位中是否已存在
   *
   * @param bankAccount    银行账户
   * @param useWaterUnitId 单位id
   * @param id             当前单位id
   * @return 匹配到的行数
   */
  int selectBankAccount(@Param("id") String id, @Param("bankAccount") String bankAccount,
      @Param("useWaterUnitId") String useWaterUnitId);

  /**
   * 批量删除银行信息
   *
   * @param ids 部门id
   * @return 是否成功
   */
  boolean deletedBank(@Param("ids") List<String> ids);

  /**
   * 通过部门id找到关联的银行信息
   * @param id 部门id
   * @return 银行信息集
   */
  List<Bank> queryByUnitId(@Param("id") String id);
}
