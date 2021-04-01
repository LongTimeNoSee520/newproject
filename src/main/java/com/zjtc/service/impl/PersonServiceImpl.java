package com.zjtc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.HttpUtil;
import com.zjtc.mapper.waterSys.PersonMapper;
import com.zjtc.model.Person;
import com.zjtc.service.PersonService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

  @Value("${file.preViewRealPath}")
  private String ipPort;





  @Override
  public List<Person> selectPersonByResCode(String resCode,String nodeCode) throws Exception {
    String doPost = "";
    //    转换成json格式
    JSONObject jsonObject=new JSONObject();
    jsonObject.put("resCode",resCode);
    jsonObject.put("nodeCode",nodeCode);
    doPost = HttpUtil.doPost(ipPort+url, jsonObject.toJSONString());
    System.out.println(doPost);
    JSONObject json = JSON.parseObject(doPost);
    return json.getJSONArray("data").toJavaList(Person.class);
  }

  @Override
  public String selectByUserId(String operatorId) {
    if (StringUtils.isNotBlank(operatorId)){
      return this.baseMapper.selectByUserId(operatorId);
    }else {
      return null;
    }
  }
}
