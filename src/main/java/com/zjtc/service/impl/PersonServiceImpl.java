package com.zjtc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.mapper.waterSys.PersonMapper;
import com.zjtc.model.Person;
import com.zjtc.service.PersonService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7
 */
@Service
@Slf4j
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements
    PersonService {


  @Override
  public ApiResponse selectPersonAll() {
    ApiResponse response = new ApiResponse();
    EntityWrapper<Person> wrapper = new EntityWrapper<>();
    wrapper.eq("deleted", "0");
    List<String> list = new ArrayList<>();
    List<Person> personList = this.baseMapper.selectList(wrapper);
    for (Person person : personList) {
      list.add(person.getUserName());
    }
    if (list.size() > 0) {
      response.setData(list);
      response.setCode(200);
      return response;
    }
    return null;
  }
}
