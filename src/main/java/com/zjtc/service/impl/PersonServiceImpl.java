package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.HttpUtil;
import com.zjtc.mapper.waterSys.PersonMapper;
import com.zjtc.model.Person;
import com.zjtc.service.PersonService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7
 */
@Service
@Slf4j
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements
    PersonService {
  @Value("${waterSys.selectPersonByResCode}")
  private String url;

  @Override
  public ApiResponse selectPersonAll() {
    ApiResponse response = new ApiResponse();
    EntityWrapper<Person> wrapper = new EntityWrapper<>();
    wrapper.eq("deleted", "0");
    List<Person> personList = this.baseMapper.queryAll();
      response.setData(personList);
      return response;
  }

  @Override
  public List<Person> selectPersonAll(String personId) throws Exception{
    return this.baseMapper.selectPersonAll(personId);
  }


  @Override
  public List<Person> selectPersonByResCode(String resCode,String nodeCode) throws Exception {
    String doPost = "";
    //    转换成json格式
    JSONObject jsonObject=new JSONObject();
    jsonObject.put("resCode",resCode);
    jsonObject.put("nodeCode",nodeCode);
    doPost = HttpUtil.doPost(url, jsonObject.toJSONString());
    System.out.println(doPost);
    return  null;
  }
}
