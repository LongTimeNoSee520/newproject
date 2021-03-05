package com.zjtc.mapper.waterBiz;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Mapper
public interface TodoMapper extends BaseMapper<Todo> {
  boolean deleteByBusinessId(@Param("businessId") String businessId);
}