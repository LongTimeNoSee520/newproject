package com.zjtc.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.MessageMapper;
import com.zjtc.model.Message;
import com.zjtc.service.MessageService;
import org.springframework.stereotype.Service;


/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements
    MessageService {


}