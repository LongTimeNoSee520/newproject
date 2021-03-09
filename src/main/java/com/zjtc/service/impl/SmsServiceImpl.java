package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.constant.SmsConstants;
import com.zjtc.base.util.HttpUtil;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Contacts;
import com.zjtc.model.User;
import com.zjtc.model.vo.SendListVO;
import com.zjtc.service.ContactsService;
import com.zjtc.service.SmsService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/27
 */

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

  @Autowired
  private JWTUtil jwtUtil;

  @Value("${waterSms.sendUrl}")
  private String sendUrl;
  @Autowired
  private ContactsService contactsService;

  @Override
  public void sendMsgToUnit(User user, String unitCode, String messageContent, String messageType)
      throws Exception {
    /**通过单位编号查询主要联系人信息*/
    Contacts contacts = contactsService.selectByUnitCode(unitCode);
    this
        .sendMessages(user, messageContent, contacts.getMobileNumber(), null,
            contacts.getContacts(),
            messageType);
  }

  @Override
  public void sendMsgToPromoter(User user, String operatorId, String operator,
      String messageContent, String messageType) throws Exception {
    /**通过发起人id查询其电话号码*/
    String phoneNumber = contactsService.selectByUserId(operatorId);
    this.sendMessages(user, messageContent, phoneNumber, operatorId, operator, "审核通知");
  }

  @Override
  @Async("asyncExecutor")
  public void asyncSendMsg() {
    // TODO: 2021/3/6 批量发送短信

  }

  @Override
  @Async("asyncExecutor")
  public void sendNotification(User user, List<SendListVO> sendList, String notificationType,
      Integer year) throws Exception {
    JSONArray sendInfoList = new JSONArray();
    for (SendListVO send :sendList){
      String messageContent = "";//短信内容
      String messageType="";//短信类型
      if (notificationType.equals(SmsConstants.SEND_NOTIFICATION_PLAN)){
        messageContent = "计划下达通知：你单位【" + send.getUnitName() + "】" + year
            + "年用水计划已经下达，请你单位于7个工作日内前往公共服务平台或者微信公众号“成都市微管家”中办理计划自平。";
        messageType="计划通知";
      }else if (notificationType.equals(SmsConstants.SEND_NOTIFICATION_PAY)){
        messageContent = "催缴通知：你单位【" + send.getUnitName() + "】" + send.getCountYear() + "年第【" + send
            .getCountQuarter() + "】季度，超计划用水【" + send.getExceedWater() + "】吨，产生加价费用【" + send
            .getIncreaseMoney() + "】元，请你单位收到本通知手7个工作日内到我中心完成缴费。";
        messageType="催缴通知";
      }
      JSONObject sendInfo = new JSONObject();
      sendInfo.put("content", messageContent);
      sendInfo.put("messageType", messageType);
      sendInfo.put("businessId",send.getId());
      List<JSONObject> sendTo = new ArrayList<>();
      JSONObject sendToInfo = new JSONObject();
      sendToInfo.put("unitCode", send.getUnitCode());
      sendToInfo.put("unitName", send.getUnitName());
      sendToInfo.put("receiverName", send.getReceiverName());
      sendToInfo.put("phoneNumber", send.getMobileNumber());
      sendTo.add(sendToInfo);
      sendInfo.put("sendTo", sendTo);
      sendInfoList.add(sendInfo);
    }
    JSONObject postJson = new JSONObject();
    postJson.put("msgList",sendInfoList);
    String publicKey = jwtUtil.getPublicKey();
    String token = jwtUtil.creatToken(user, publicKey);
    HttpUtil.doPost(token, sendUrl, postJson.toJSONString());
  }

  private void sendMessages(User user, String messageContent, String phoneNumber, String receiverId,
      String receiverName, String messageType) throws Exception {
    JSONObject jsonObject = new JSONObject();
    String publicKey = jwtUtil.getPublicKey();
    String token = jwtUtil.creatToken(user, publicKey);

    jsonObject.put("content", messageContent);
    jsonObject.put("messageType", messageType);
    List<JSONObject> sendTo = new ArrayList<>();
    JSONObject sendToInfo = new JSONObject();
    if (StringUtils.isNotBlank(receiverId)) {
      sendToInfo.put("receiverId", receiverId);
    }
    sendToInfo.put("receiverName", receiverName);
    sendToInfo.put("phoneNumber", phoneNumber);
    sendTo.add(sendToInfo);
    jsonObject.put("sendTo", sendTo);
    JSONArray msgList = new JSONArray();
    msgList.add(jsonObject);
    JSONObject postJson = new JSONObject();
    postJson.put("msgList",msgList);
    HttpUtil.doPost(token, sendUrl, postJson.toJSONString());
  }

}
