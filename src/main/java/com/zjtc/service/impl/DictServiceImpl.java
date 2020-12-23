package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.base.util.RedisUtil;
import com.zjtc.mapper.DictMapper;
import com.zjtc.model.Dict;
import com.zjtc.model.DictItem;
import com.zjtc.service.DictItemService;
import com.zjtc.service.DictService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

  @Autowired
  private RedisUtil redisUtil;
  @Autowired
  private DictItemService dictItemService;
  @Autowired
  private JWTUtil jwtUtil;

  @Override
  public boolean save(Dict dict, String nodeCode) {
    /**Todo nodecode*/
    dict.setNodeCode(nodeCode);
    dict.setDeleted("0");
    dict.setCreateTime(new Date());
    boolean result = this.insert(dict);
    return result;

  }

  @Override
  public boolean edit(Dict dict, String nodeCode) {
    boolean result = this.updateById(dict);
    return result;
  }

  @Override
  public boolean delete(JSONObject jsonObject, String nodeCode) {
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    if (!ids.isEmpty()) {
      /**逻辑删除字典*/
      return baseMapper.updateBatch(ids);
    }
    return false;
  }

  @Override
  public Map<String, Object> queryPage(JSONObject jsonObject) {
    Map<String, Object> page = new LinkedHashMap<>();
    List<Map<String, Object>> result = baseMapper.queryPage(jsonObject);
    page.put("records", result);
    page.put("current", jsonObject.getInteger("current"));
    page.put("size", jsonObject.getInteger("size"));
    //查询总数据条数
    long total = baseMapper.queryListTotal(jsonObject);
    page.put("total", total);
    long pageSize = jsonObject.getInteger("size");
    page.put("page", total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
    return page;
  }

  @Override
  @SneakyThrows
  public void refreshDictData(String nodeCode) {
    System.out.println("=========刷新数据字典缓存");
    EntityWrapper entity = new EntityWrapper();
    entity.eq("node_code", nodeCode);
    entity.eq("deleted", "0");
    List<Dict> list = this.selectList(entity);
    if (null != list && list.size() > 0) {
      /** 删除原来的数据字典缓存数据 key: dict+_+nodeCode **/
      redisUtil.del("dict_"+nodeCode);
      Map<String, String> dictMap = new HashMap();
      for (Dict dict : list) {
        EntityWrapper wrapper = new EntityWrapper<>();
        dict.setDictItems(
            dictItemService.selectList(
                wrapper.eq("dict_id", dict.getId()).eq("node_code", nodeCode).eq("deleted", "0")
                    .orderAsc(
                        Collections.singleton("dict_item_rank"))));
        if (null != dict.getDictItems() && dict.getDictItems().size() > 0) {
          dictMap.put(dict.getDictCode(), JSONObject.toJSONString(dict.getDictItems()));
        }
      }
      redisUtil.hmset("dict_"+nodeCode, dictMap);
    } else {
      log.debug("缓存数据字典失败，数据字典查询结果为空!");
    }
  }

  @Override
  public ApiResponse checkDictCode(Dict dict, String nodeCode) {
    ApiResponse apiResponse = new ApiResponse();
    //字典id
    String id = dict.getId();
    /**验证字典编码*/
    Wrapper<Dict> wrapper1 = new EntityWrapper<>();
    if (StringUtils.isNotBlank(id)) {
      //排己
      wrapper1.notIn("id", id);
    }
    wrapper1.eq("dict_code", dict.getDictCode());
    wrapper1.eq("deleted", "0");
    wrapper1.eq("node_code", nodeCode);
    int result1 = this.selectCount(wrapper1);
    if (result1 > 0) {
      apiResponse.recordError("字典编码不能重复");
      return apiResponse;
    }
    /**验证字典排序号*/
    Wrapper<Dict> wrapper2 = new EntityWrapper<>();
    if (StringUtils.isNotBlank(id)) {
      //排己
      wrapper2.notIn("id", id);
    }
    wrapper2.eq("dict_rank", dict.getDictRank());
    wrapper2.eq("deleted", "0");
    wrapper2.eq("node_code", nodeCode);
    int result2 = this.selectCount(wrapper2);
    if (result2 > 0) {
      apiResponse.recordError("字典排序号不能重复");
      return apiResponse;
    }
    return apiResponse;
  }

  @Override
  public ApiResponse removeBeforeValidate(JSONObject jsonObject, String nodeCode) {
    ApiResponse apiResponse = new ApiResponse();
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    EntityWrapper entityWrapper = new EntityWrapper();
    entityWrapper.in("dict_id", ids);
    entityWrapper.eq("deleted", "0");
    entityWrapper.eq("node_code", nodeCode);
    int result = dictItemService.selectCount(entityWrapper);
    if (result > 0) {
      apiResponse.recordError("当前删除的字典中存在字典项，不能删除");
    }
    return apiResponse;
  }

  @Override
  public Map<String, List<Map<String, Object>>> findByDictCodes(String dictCodes,String nodeCode) {
    String[] dictCodeArr = dictCodes.split(",");
    /** 数据字典缓存数据（json字符串） **/
    Map<String, String> dictCacheMap = redisUtil.hgetAll("dict"+"_"+nodeCode);
    if (dictCodeArr != null && dictCodeArr.length > 0 && null != dictCacheMap) {
      Map<String, List<Map<String, Object>>> dict = new HashMap<>();
      for (String dictCode : dictCodeArr) {
        List<DictItem> dictItemList = JSONObject
            .parseArray(dictCacheMap.get(dictCode), DictItem.class);
        if (null != dictItemList && dictItemList.size() > 0) {
          List<Map<String, Object>> _dict = new ArrayList<>();
          for (DictItem dictItem : dictItemList) {
            try {
              Map<String, Object> item = new HashMap<>();
              item.put("value", dictItem.getDictItemCode());
              item.put("name", dictItem.getDictItemName());
              item.put("rank", dictItem.getDictItemRank());
              _dict.add(item);
            } catch (Exception e) {
              continue;
            }
          }
          dict.put(dictCode, _dict);
        }
      }
      return dict;
    }
    return null;
  }

}
