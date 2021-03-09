package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.waterSys.SmsSendMapper;
import com.zjtc.model.SmsSendInfo;
import com.zjtc.model.vo.SendListVO;
import com.zjtc.service.SmsSendService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2020/12/01
 */

@Service
public class SmsSendServiceImpl extends ServiceImpl<SmsSendMapper, SmsSendInfo> implements
    SmsSendService {


  @Override
  public List<SendListVO> queryAll(List<SendListVO> sendListVOS, JSONObject jsonObject) {
    return baseMapper.queryAll(sendListVOS,jsonObject);
  }

  @Override
  public long count(List<SendListVO> sendListVOS, JSONObject jsonObject) {
    return baseMapper.count(sendListVOS,jsonObject);
  }
}
