package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.ImportLog;
import com.zjtc.model.WaterUseData;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2020/12/26
 */

@Mapper
public interface WaterQuantityManageMapper extends BaseMapper<WaterUseData> {

  int queryNum(Map<String, Object> map);

  List<Map<String,Object>> queryPage(Map<String, Object> map);

  void insertOrUpdate(@Param("waterUseData") WaterUseData waterUseData);
}
