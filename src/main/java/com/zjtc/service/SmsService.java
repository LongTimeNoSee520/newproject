package com.zjtc.service;


import com.zjtc.model.User;

/**
 * @author lianghao
 * @date 2021/01/27
 */

public interface SmsService  {

  /**
   * 单位
   * @param user
   * @param unitCode
   * @param messageContent
   * @param messageType
   * @throws Exception
   */
  void sendMsgToUnit(User user, String unitCode, String messageContent,String messageType) throws Exception;

  /**
   * 人
   * @param user
   * @param operatorId
   * @param operator
   * @param messageContent
   * @param messageType
   * @throws Exception
   */
  void sendMsgToPromoter(User user, String operatorId, String operator, String messageContent,
      String messageType) throws Exception;
}
