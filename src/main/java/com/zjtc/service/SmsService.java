package com.zjtc.service;


import com.zjtc.model.User;

/**
 * @author lianghao
 * @date 2021/01/27
 */

public interface SmsService {

  /**
   * 单位
   */
  void sendMsgToUnit(User user, String unitCode, String messageContent, String messageType)
      throws Exception;

  /**
   * 人
   */
  void sendMsgToPromoter(User user, String operatorId, String operator, String messageContent,
      String messageType) throws Exception;

  /**
   * 异步发短信
   */
  void asyncSendMsg();
}
