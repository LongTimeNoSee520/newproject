package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}