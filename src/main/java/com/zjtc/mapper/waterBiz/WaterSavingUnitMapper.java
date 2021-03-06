package com.zjtc.mapper.waterBiz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.WaterSavingUnit;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * WaterSavingUnit的Dao接口
 * 
 * @author 
 *
 */
@Mapper
public interface WaterSavingUnitMapper extends BaseMapper<WaterSavingUnit> {

  /**
   * 分页
   * @param jsonObject
   * @return
   */
  List<WaterSavingUnit> queryPage(JSONObject jsonObject);

  /**
   * 数据总条数
   * @param jsonObject
   * @return
   */
  long queryListTotal(JSONObject jsonObject);
}