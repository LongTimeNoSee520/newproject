package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Bank;
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
   * @param bankAccount 银行账户
   * @param useWaterUnitId 单位id
   * @return 匹配到的行数
   */
  int selectBankAccount(@Param("bankAccount") String bankAccount,@Param("useWaterUnitId") String useWaterUnitId);
}
