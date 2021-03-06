package com.zjtc.service;


import com.nimbusds.jose.JOSEException;
import com.zjtc.model.User;
import com.zjtc.model.vo.SendListVO;
import java.util.List;

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

  /**
   * * 异步发送缴费通知/自平计划调整通知
   * @param user
   * @param sendList
   * @param notificationType 通知类型
   * @param year
   */
  void sendNotification(User user, List<SendListVO> sendList, String notificationType,
      Integer year) throws Exception;
}
