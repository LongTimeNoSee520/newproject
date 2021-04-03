package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.Message;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

  /**
   * 修改已读
   * @param id
   * @param operationTime
   * @return
   */
  boolean updateStatus(@Param("id") String id, @Param("operationTime") Date operationTime);

  /**
   * 批量修改已读
   * @param mobileNumber
   * @param operationTime
   * @return
   */
  boolean updateMsgStatusAll(@Param("userId") String mobileNumber,@Param("operationTime") Date operationTime);

  /**
   * 查看通知消息
   * @param userId
   * @return
   */
  List<Message> messageInfo(@Param("userId") String userId);
}