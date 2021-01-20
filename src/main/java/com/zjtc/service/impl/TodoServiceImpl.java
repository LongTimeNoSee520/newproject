package com.zjtc.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.mapper.TodoMapper;
import com.zjtc.model.Todo;
import com.zjtc.service.TodoService;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {


}