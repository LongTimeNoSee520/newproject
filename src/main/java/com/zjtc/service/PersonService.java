package com.zjtc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.Person;
import java.util.List;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7
 * 角色服务层
 */
public interface PersonService extends IService<Person> {

  /**
   * 根据资源code查询,资源下所有角色的所有人
   * @param resCode
   * @return
   */
  List<Person> selectPersonByResCode(String resCode,String nodeCode) throws Exception;


  /**
   * 通过发起人id查询其电话号码
   * */
  String selectByUserId(String operatorId);
}
