package com.zjtc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.util.TimeUtil;
import com.zjtc.mapper.waterSys.SystemLogMapper;
import com.zjtc.model.SystemLog;
import com.zjtc.model.User;
import com.zjtc.service.SystemLogService;
import java.util.Date;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/09
 */

@Service
@Log4j
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements
    SystemLogService {



  @Override
  public boolean logInsert(User user,String operateModule,String operateContent,String ip) {
    boolean result = false;
    try {
      SystemLog log = new SystemLog();
      log.setNodeCode(user.getNodeCode());
      log.setLoginId(user.getLoginId());
      log.setUserName(user.getUsername());
      log.setUserId(user.getId());
      log.setOperateModule(operateModule);
      log.setOperateContent(operateContent);
      log.setCreateTime(new Date());
      log.setIp(ip);
//    log.setOpenId(openId);
//    log.setPhoneNumber(phoneNumber);
      log.setSysModule("节水业务端");
      result= this.save(log);
    }catch (Exception e){
      log.error("日志新增错误！操作详情:节水业务端" + operateModule + operateContent + "时间-" + TimeUtil
          .formatDateToTime(new Date()) + "===错误：" + e);
    }
   return  result;
  }

}
