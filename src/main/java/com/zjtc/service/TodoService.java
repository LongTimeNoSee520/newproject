package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Todo;
import com.zjtc.model.User;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface TodoService extends IService<Todo> {


  void add(String businessId, User user, String auditorId, String auditorName, String todoContent,
       String businessJson, String detailConfig,String todoType);
}
