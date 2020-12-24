package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.QuotaInfo;
import com.zjtc.model.UseWaterQuota;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 定额信息关联
 * @author lianghao
 * @date 2020/12/24
 */
@Mapper
public interface UseWaterQuotaMapper extends BaseMapper<UseWaterQuota> {

  List<UseWaterQuota> queryByUnitId(@Param("useWaterUnitId") String useWaterUnitId);
}