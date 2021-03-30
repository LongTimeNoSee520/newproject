package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.Todo;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {
  boolean deleteByBusinessId(@Param("businessId") String businessId);

  List<Map<String,Object>> queryList(@Param("userId")String userId,@Param("nodeCode")String nodeCode);

  Todo selectTodoModel(@Param("businessId") String businessId,@Param("nodeCode") String nodeCode,@Param("executePersonId") String executePersonId);
}