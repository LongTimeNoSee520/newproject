package com.zjtc.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.util.HttpUtil;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.Contacts;
import com.zjtc.model.User;
import com.zjtc.service.ContactsService;
import com.zjtc.service.SmsService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/27
 */

@Service
@Slf4j
public class SmsServiceImpl  implements SmsService {

  @Autowired
  private JWTUtil jwtUtil;

  @Value("${waterSms.sendUrl}")
  private String sendUrl;
  @Autowired
  private ContactsService contactsService;

  @Override
  public void sendMsgToUnit(User user, String unitCode, String messageContent,String messageType) throws Exception {
    /**通过单位编号查询主要联系人信息*/
    Contacts contacts = contactsService.selectByUnitCode(unitCode);
    this
        .sendMessages(user, messageContent, contacts.getMobileNumber(), null,contacts.getContacts(),
            messageType);
  }

  @Override
  public void sendMsgToPromoter(User user, String operatorId, String operator,
      String messageContent, String messageType) throws Exception {
    /**通过发起人id查询其电话号码*/
    String phoneNumber = contactsService.selectByUserId(operatorId);
    this.sendMessages(user,messageContent,phoneNumber,operatorId,operator,"审核通知");
  }

  private void sendMessages(User user, String messageContent, String phoneNumber,String receiverId,
      String receiverName, String messageType) throws Exception {
    JSONObject jsonObject = new JSONObject();
    String publicKey = jwtUtil.getPublicKey();
    String token = jwtUtil.creatToken(user,publicKey);

    jsonObject.put("content",messageContent);
    jsonObject.put("messageType",messageType);
    List<JSONObject> sendTo  =new ArrayList<>();
    JSONObject sendToInfo =new JSONObject();
    if (StringUtils.isNotBlank(receiverId)){
      sendToInfo.put("receiverId",receiverId);
    }
    sendToInfo.put("receiverName",receiverName);
    sendToInfo.put("phoneNumber",phoneNumber);
    sendTo.add(sendToInfo);
    jsonObject.put("sendTo",sendTo);
     HttpUtil.doPost(token, sendUrl, jsonObject.toJSONString());
  }

}
