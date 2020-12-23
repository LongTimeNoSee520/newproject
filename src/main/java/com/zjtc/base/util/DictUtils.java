package com.zjtc.base.util;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.model.DictItem;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zengqingsong on 2019/6/19.
 */
@Component
public class DictUtils {

  @Autowired
  private RedisUtil redisUtil;

  /**
   * 获取数据字典项名称
   *
   * @param dictCode 数据字典code
   * @param dictItemCode 数据字典项code
   * @param nodeCode  节点编码
   */
  public String getDictItemName(String dictCode, String dictItemCode,String nodeCode) {
    /** 数据字典缓存数据（json字符串） **/
    Map<String, String> dictCacheMap = redisUtil.hgetAll("dict_"+nodeCode);
    try {
      if (null != dictCacheMap && null != dictCacheMap.get(dictCode)) {
        List<DictItem> dictItemList = JSONObject
            .parseArray(dictCacheMap.get(dictCode), DictItem.class);
        if (null != dictItemList && dictItemList.size() > 0) {
          for (DictItem dictItem : dictItemList) {
            if (dictItemCode.equals(dictItem.getDictItemCode())) {
              return dictItem.getDictItemName();
            }
          }
        }
      }
    } catch (Exception e) {
      return "未知";
    }
    return "未知";
  }

  /**
   * 获取数据字典项值
   *
   * @param dictCode 数据字典code
   * @param dictDataName 数据字典项name
   * @param nodeCode  节点编码
   */
  public String getDictItemCode(String dictCode, String dictDataName,String nodeCode) {
    /** 数据字典缓存数据（json字符串） **/
    Map<String, String> dictCacheMap = redisUtil.hgetAll("dict_"+nodeCode);
    try {
      if (null != dictCacheMap && null != dictCacheMap.get(dictCode)) {
        List<DictItem> dictItemList = JSONObject
            .parseArray(dictCacheMap.get(dictCode), DictItem.class);
        if (null != dictItemList && dictItemList.size() > 0) {
          for (DictItem dictItem : dictItemList) {
            if (dictDataName.equals(dictItem.getDictItemName())) {
              return dictItem.getDictItemCode();
            }
          }
        }
      }
    } catch (Exception e) {
      return "未知";
    }
    return "未知";
  }

}
