package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.Algorithm;
import org.apache.ibatis.annotations.Mapper;


/**
 * Algorithm的Dao接口
 * 
 * @author 
 *
 */
@Mapper
public interface AlgorithmMapper extends BaseMapper<Algorithm> {

  /**
   * 新增
   * @param algorithm
   * @return
   */
  long insertNotNull(Algorithm algorithm);

  /**
   * 修改
   * @param algorithm
   * @return
   */
  long updateNotNull(Algorithm algorithm);
}