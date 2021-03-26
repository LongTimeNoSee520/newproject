package com.zjtc.mapper.waterSys;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.DictItem;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: yuyantian
 * @Date: 2020/12/7
 * @description
 */
@Mapper
public interface DictItemMapper extends BaseMapper<DictItem> {

  /**
   * 批量逻辑删除
   *
   * @param dictId 字典id
   */
  boolean updateByDictId(@Param("dictIdList") List<String> dictId);

  /**
   * 根据字典编码查询字典项集合
   */
  List<DictItem> getDictItem(@Param("dictCode") String dictCode, @Param("nodeCode") String nodeCode);

  /**
   * 根据字典编码查询指定字典项
   *
   * @param dictCode 字典编码
   * @param dictItemCode 字典项编码
   */
  DictItem findByDictCode(@Param("dictCode") String dictCode,
      @Param("dictItemCode") String dictItemCode, @Param("nodeCode") String nodeCode);

  /**
   * 根据字典名称查询指定字典项
   *
   * @param dictCode 字典编码
   * @param dictItemName 字典项名称
   */
  DictItem findByDictName(@Param("dictCode") String dictCode,
      @Param("dictItemName") String dictItemName, @Param("nodeCode") String nodeCode);

  /**
   * 分页
   */
  List<Map<String, Object>> queryPage(JSONObject jsonObject);

  /**
   * 分页查询出的数据总条数
   */
  long queryListTotal(JSONObject jsonObject);

}
