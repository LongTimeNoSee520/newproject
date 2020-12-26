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
 * @Date: 2020/12/23 银行信息表
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements
    BankService {

  @Override
  public boolean deletedBank(String id) {
    if (StringUtils.isBlank(id)) {
      return false;
    }
    List<Bank> banks = this.baseMapper.queryByUnitId(id);
    List<String> ids = new ArrayList<>();
    for (Bank bank : banks) {
      ids.add(bank.getId());
    }
    if (ids.isEmpty()){
      return false;
    }
    return this.baseMapper.deletedBank(ids);
  }

  @Override
  public boolean deletedBank(List<String> ids) {
    if (ids.isEmpty()) {
      return false;
    }
    for (String id : ids) {
      this.deletedBank(id);
    }
    return true;
  }


  @Override
  public boolean insertBank(List<Bank> bank, String useWaterUnitId, String nodeCode) {
    if (bank.size() == 0 || StringUtils.isBlank(useWaterUnitId) || StringUtils.isBlank(nodeCode)) {
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
    if (bank.size() == 0 || StringUtils.isBlank(useWaterUnitId) || StringUtils.isBlank(nodeCode)) {
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
