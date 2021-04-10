package com.zjtc.mapper.waterCountry;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author lianghao
 * @date 2020/12/25
 */

@Mapper
@Component
public interface CountyIndustryUseWaterMapper {

  List<Map<String,Object>> queryList(@Param("nodeCode")String nodeCode,@Param("year")Integer year);

  List<Map<String, Object>> queryRankData(@Param("nodeCode") String nodeCode,
      @Param("year") Integer year, @Param("id") String id);
}
