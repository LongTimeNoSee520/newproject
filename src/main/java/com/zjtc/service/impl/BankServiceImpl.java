package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.BankMapper;
import com.zjtc.model.Bank;
import com.zjtc.service.BankService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements
    BankService {

  @Override
  public boolean deletedBank(String id) {
    Bank bank = new Bank();
    bank.setId(id);
    bank.setDeleted("1");
    bank.setUseWaterUnitId(null);
    int result = this.baseMapper.updateById(bank);
    if (result > 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean batchDeletedBank(List<String> ids) {
    List<Bank> list = new ArrayList<>();
    Bank bank = new Bank();
    boolean b = false;
    if (ids.size() == 0) {
      return false;
    }
    for (String id : ids) {
      bank.setId(id);
      bank.setUseWaterUnitId("");
      list.add(bank);
      b = this.updateBatchById(list);
    }
    if (b) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean insertBank(List<Bank> bank, String useWaterUnitId, String nodeCode) {
    if (null == bank || StringUtils.isBlank(useWaterUnitId) || StringUtils
        .isBlank(nodeCode)) {
      return false;
    }
    for (Bank bank1 : bank) {
      String bankAccount = bank1.getBankAccount();
//    判断当前单位的银行账号是否已存在
      int i = this.baseMapper.selectBankAccount("null", bankAccount, useWaterUnitId);
      if (i > 0) {
        return false;
      }
      bank1.setUseWaterUnitId(useWaterUnitId);
      bank1.setDeleted("0");
      bank1.setNodeCode(nodeCode);
    }
//    判断是本行还是他行(默认为建设银行)未实现
    return this.insertBatch(bank);

  }

  @Override
  public boolean updateBank(List<Bank> bank, String useWaterUnitId, String nodeCode) {
    if (null == bank) {
      return false;
    }
    for (Bank bank1 : bank) {
      String bankAccount = bank1.getBankAccount();
//    判断当前单位的银行账号是否已存在
      int i = this.baseMapper.selectBankAccount("null", bankAccount, useWaterUnitId);
      if (i > 0) {
        return false;
      }
    }
    return this.updateBatchById(bank);

  }

  @Override
  public List<Bank> selectBank(String useWaterUnitId, String nodeCode) {
    EntityWrapper<Bank> wrapper = new EntityWrapper<>();
    wrapper.eq("deleted", 0);
    wrapper.eq("use_water_unit_Id", useWaterUnitId);
    wrapper.eq("node_code", nodeCode);
    return this.selectList(wrapper);
  }

}
