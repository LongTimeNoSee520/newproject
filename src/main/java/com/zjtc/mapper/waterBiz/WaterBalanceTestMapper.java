package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.WaterBalanceTest;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/02/25
 */
@Mapper
public interface WaterBalanceTestMapper extends BaseMapper<WaterBalanceTest> {

  int queryNum(Map<String, Object> map);

  List<Map<String,Object>> queryPage(Map<String, Object> map);

  boolean updateDeleted(@Param("ids") List<String> ids);
}