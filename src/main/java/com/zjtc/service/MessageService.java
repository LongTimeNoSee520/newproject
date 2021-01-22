package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Message;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface MessageService extends IService<Message> {

  /**
   * @param nodeCode 节点编码
   * @param operatorId 发起人id
   * @param operator 发起人姓名
   * @param messageType 消息类型
   * @param messageContent 消息内容
   */
  void add(String nodeCode, String operatorId, String operator, String messageType,
      String messageContent);

  void updateStatus(String id);
}
