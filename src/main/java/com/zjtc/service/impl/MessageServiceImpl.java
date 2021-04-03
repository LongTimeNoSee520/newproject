package com.zjtc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.mapper.waterBiz.MessageMapper;
import com.zjtc.model.Message;
import com.zjtc.service.MessageService;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;


/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements
    MessageService {


  @Override
  public void add(String nodeCode, String operatorId, String operator, String messageType,
      String messageContent, String businessId) {
    Message message = new Message();
    message.setCreateTime(new Date());
    message.setNodeCode(nodeCode);
    message.setMsgPersonId(operatorId);
    message.setMsgPersonName(operator);
    message.setMsgType(messageType);
    message.setCreateCode(nodeCode);
    message.setBusinessId(businessId);
    if (AuditConstants.END_PAPER_MESSAGE_TYPE.equals(messageType)) {
      message.setMsgTitle(AuditConstants.END_PAPER_MESSAGE_TITLE);
    }
    if (AuditConstants.PAY_MESSAGE_TYPE.equals(messageType)) {
      message.setMsgTitle(AuditConstants.PAY_MESSAGE_TITLE);
    }
    message.setMsgContent(messageContent);
    message.setMsgStatus("0");//未读
    this.baseMapper.insert(message);
  }

  @Override
  public void messageToUnit(String unitCode, String messageContent, String msgTitle) {
    Message message = new Message();
    message.setCreateTime(new Date());
    message.setUnitCode(unitCode);
    message.setMsgType(AuditConstants.EXTERNALS_MESSAGE_TYPE);
    message.setMsgTitle(msgTitle);
    message.setMsgContent(messageContent);
    message.setMsgStatus("0");//未读
    this.baseMapper.insert(message);
  }

  @Override
  public boolean updateStatus(String id) {
    Date operationTime = new Date();
   return baseMapper.updateStatus(id, operationTime);
  }

  @Override
  public boolean updateMsgStatusAll(String mobileNumber) {
    return baseMapper.updateMsgStatusAll(mobileNumber,new Date());
  }

  @Override
  public List<Message> messageInfo(String userId) {
    return baseMapper.messageInfo(userId);
  }
}