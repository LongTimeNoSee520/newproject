package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.ImportLog;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lianghao
 * @date 2020/12/25
 */

@Mapper
public interface ImportLogMapper extends BaseMapper<ImportLog> {

  int queryNum(Map<String, Object> map);

  List<ImportLog> queryPage(Map<String, Object> map);
}
