package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Message;
import java.util.Date;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

  void updateStatus(@Param("id") String id, @Param("operationTime") Date operationTime);
}