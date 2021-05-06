package com.zjtc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.mapper.waterBiz.BankMapper;
import com.zjtc.model.Bank;
import com.zjtc.service.BankService;
import com.zjtc.service.SystemLogService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 银行信息表
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements
    BankService {

  @Autowired
  private SystemLogService systemLogService;

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
    if (ids.isEmpty()) {
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
      if ("3".equals(bank1.getMain())) {
        bank1.setRevocationDate(new Date());
      }
      bank1.setId("");
      bank1.setUseWaterUnitId(useWaterUnitId);
      bank1.setDeleted("0");
      bank1.setNodeCode(nodeCode);
      bank1.setIsExport("0");
    }
//    判断是本行还是他行(默认为建设银行)未实现
    return this.saveBatch(bank);

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
    QueryWrapper<Bank> wrapper = new QueryWrapper<>();
    ArrayList<String> list = new ArrayList<>();
    wrapper.eq("deleted", 0);
    wrapper.eq("use_water_unit_Id", useWaterUnitId);
    wrapper.eq("node_code", nodeCode);
    wrapper.orderByDesc("main");
    return this.list(wrapper);
  }

  @Override
  public boolean updateIsExport(List<Map<String, Object>> list) {
    List<String> ids = new ArrayList<>();
    if (!list.isEmpty()) {
      for (Map map : list) {
        ids.add(map.get("id").toString());
      }
      QueryWrapper queryWrapper=new QueryWrapper();
      queryWrapper.in(ids);
      Bank bank=new Bank();
      bank.setIsExport("1");
      return this.update(bank,queryWrapper);
    }
    return false;
  }

}
