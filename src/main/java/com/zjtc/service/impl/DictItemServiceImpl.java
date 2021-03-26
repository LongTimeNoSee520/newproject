package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.mapper.waterSys.DictItemMapper;
import com.zjtc.model.DictItem;
import com.zjtc.service.DictItemService;
import com.zjtc.service.DictService;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements
    DictItemService {

  @Autowired
  private JWTUtil jwtUtil;
  @Autowired
  private DictService dictService;

  @Override
  public boolean save(DictItem dictItem, String nodeCode) {
    /**Todo nodecode*/
    dictItem.setNodeCode(nodeCode);
    dictItem.setDeleted("0");
    dictItem.setCreateTime(new Date());
    return this.save(dictItem);
  }

  @Override
  public boolean edit(DictItem dictItem, String nodeCode) {
    return this.updateById(dictItem);
  }

  @Override
  public boolean delete(JSONObject jsonObject, String nodeCode) {
    List<String> ids = jsonObject.getJSONArray("ids").toJavaList(String.class);
    List<DictItem> dictItem = new ArrayList<>(this.listByIds(ids));
    if (!dictItem.isEmpty()) {
      /**逻辑删除字典项*/
      for (DictItem item : dictItem) {
        item.setDeleted("1");
      }
      return this.updateBatchById(dictItem);
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
  public boolean updateByDictId(List<String> dictId) {
    return baseMapper.updateByDictId(dictId);
  }

  @Override
  public DictItem findByDictCode(String dictCode, String dictItemCode, String nodeCode) {
    return baseMapper.findByDictCode(dictCode, dictItemCode, nodeCode);
  }

  @Override
  public DictItem findByDictName(String dictCode, String dictItemName, String nodeCode) {
    return baseMapper.findByDictName(dictCode, dictItemName, nodeCode);
  }

  @Override
  public List<DictItem> getDictItem(String dictCode, String nodeCode) {
    List<DictItem> result = baseMapper.getDictItem(dictCode, nodeCode);
    return result;
  }

  @Override
  public ApiResponse checkDictCode(DictItem dictItem, String nodeCode) {
    ApiResponse apiResponse = new ApiResponse();
    //字典项id
    String id = dictItem.getId();
    /**验证字典项编码*/
    QueryWrapper<DictItem> wrapper1 = new QueryWrapper<>();
    if (StringUtils.isNotBlank(id)) {
      //排己
      wrapper1.notIn("id", id);
    }
    wrapper1.eq("dict_id", dictItem.getDictId());
    wrapper1.eq("dict_item_code", dictItem.getDictItemCode());
    wrapper1.eq("deleted", "0");
    wrapper1.eq("node_code", nodeCode);
    int result1 = this.count(wrapper1);
    if (result1 > 0) {
      apiResponse.recordError("字典项编码不能重复");
      return apiResponse;
    }
    /**验证字典项排序号*/
    QueryWrapper<DictItem> wrapper2 = new QueryWrapper<>();
    if (StringUtils.isNotBlank(id)) {
      //排己
      wrapper2.notIn("id", id);
    }
    wrapper2.eq("dict_id", dictItem.getDictId());
    wrapper2.eq("dict_item_rank", dictItem.getDictItemRank());
    wrapper2.eq("deleted", "0");
    wrapper2.eq("node_code", nodeCode);
    int result2 = this.count(wrapper2);
    if (result2 > 0) {
      apiResponse.recordError("字典项排序号不能重复");
      return apiResponse;
    }
    return apiResponse;
  }
}
