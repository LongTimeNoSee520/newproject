package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.model.Message;
import java.util.List;

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
   * @param businessId 业务id
   */
  void add(String nodeCode, String operatorId, String operator, String messageType,
      String messageContent, String businessId);

  /**
   * @param unitCode 单位编号
   * @param messageContent 消息内容
   * @param msgTitle 消息标题
   */
  void messageToUnit(String unitCode, String messageContent, String msgTitle);

  /**
   * 修改待办状态
   */
  boolean updateStatus(String id);

  /**
   * 批量修改
   * @param userId 用户id
   * @return
   */
  boolean updateMsgStatusAll(String userId);

  /**
   * 通知信息
   *
   * @param userId 用户id
   */
  List<Message> messageInfo(String userId);
}
