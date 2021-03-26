package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.Dict;
import java.util.List;
import java.util.Map;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
public interface DictService extends IService<Dict> {

  /**
   * 新增
   * @param dict 字典对象
   * @param nodeCode 节点编码
   * @return
   */
  boolean save(Dict dict, String nodeCode);

  /**
   * 修改
   */
  boolean edit(Dict dict, String nodeCode);

  /**
   * 批量删除
   */
  boolean delete(JSONObject jsonObject, String nodeCode);
  /**
   * 分页查询
   */
  Map<String,Object> queryPage(JSONObject jsonObject);

  /**
   * 刷新数据字典缓存，异步执行
   * @param nodeCode 节点编码，做数据字典数据隔离
   */
  void refreshDictData(String nodeCode);

  /**
   * 新增、修改 检查数据字典code名称是否重复
   * @param dict 字典对象
   * @param nodeCode 节点编码
   * @return
   */
  ApiResponse checkDictCode(Dict dict, String nodeCode);
  /**
   * 删除字典时，判断字典是否有字典项，有字典项不能删除
   * @param jsonObject
   * @param nodeCode  节点编码
   */
  ApiResponse removeBeforeValidate(JSONObject jsonObject, String nodeCode);

  /**
   * 根据一个或多个数据字典编码查询集合
   * @param dictCode,用逗号隔开
   * @param nodeCode 节点编码
   * @return
   */
  Map<String, List<Map<String,Object>>> findByDictCodes(String dictCode, String nodeCode);
}
