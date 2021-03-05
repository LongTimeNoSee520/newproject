package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.Person;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7
 * 角色服务层
 */
public interface PersonService extends IService<Person> {

  /**
   * 查询全部人员
   * @return 查询结果
   */
  ApiResponse selectPersonAll();
}
