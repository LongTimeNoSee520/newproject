package com.zjtc.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.SystemLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * 实时日志
 *
 * @author lianghao
 * @date 2020/12/07
 */
@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLog> {

}