package com.zjtc.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Dict;
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
public interface DictMapper extends BaseMapper<Dict> {

  /**
   * 分页
   */
  List<Map<String, Object>> queryPage(JSONObject jsonObject);

  /**
   * 分页查询出的数据总条数
   * @param jsonObject
   * @return
   */
  long queryListTotal(JSONObject jsonObject);

  /**
   * 批量逻辑删除字典
   */
  boolean updateBatch(@Param("idList") List<String> idList);
}
