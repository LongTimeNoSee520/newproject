package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.SmsSendInfo;
import com.zjtc.model.vo.SendListVO;
import java.util.List;

/**
 * @author lianghao
 * @date 2020/12/01
 */

public interface SmsSendService extends IService<SmsSendInfo> {

  /**
   * 分页查询所有 同一单位、统一手机号，最新一条数据
   */
  List<SendListVO> queryAll(List<SendListVO> sendListVOS, JSONObject jsonObject);

  /**
   * 查询数据条数
   * @param sendListVOS
   * @param jsonObject
   * @return
   */
  long count(List<SendListVO> sendListVOS, JSONObject jsonObject);
}
