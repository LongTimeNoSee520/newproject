package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.DictItem;
import java.util.List;
import java.util.Map;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
public interface DictItemService extends IService<DictItem> {

  /**
   * 新增
   */
  boolean save(DictItem dictItem, String nodeCode);

  /**
   * 修改
   */
  boolean edit(DictItem dictItem, String nodeCode);

  /**
   * 删除
   */
  boolean delete(JSONObject jsonObject, String nodeCode);
  /**
   * 根据字典id分页查询字典项数据
   */
  Map<String,Object> queryPage(JSONObject jsonObject);

  /**
   * 根据字典id,逻辑删除相关的字典项
   * 批量删除
   * @param dictIdList 字典id集合
   * @return
   */
  boolean updateByDictId(List<String> dictIdList);

  /**
   * 根据字典编码获取指定字典项信息
   * @param dictCode 字典编码
   * @param dictItemCode 指定字典项编码
   * @return
   */
  DictItem findByDictCode(String dictCode, String dictItemCode, String nodeCode);

  /**
   *根据字典编码获取对应字典项数据
   * @param dictCode 字典编码
   * @param nodeCode 节点编码
   * @return
   */
  List<DictItem> getDictItem(String dictCode, String nodeCode);

  /**
   * 新增、修改 检查数据字典项code名称是否重复
   * @param dictItem
   * @param nodeCode  节点编码
   * @return
   */
  ApiResponse checkDictCode(DictItem dictItem, String nodeCode);
}
