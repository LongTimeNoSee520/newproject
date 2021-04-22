package com.zjtc.mapper.waterBiz;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


/**
 * @Author: lianghao
 * @Date: 2021/04/22
 */
@Mapper
@Component
public interface CommonMapper {

  /**
   * 更新打印状态
   * @param ids
   * @param tableName
   * @return
   */
  boolean updatePrintStatus(@Param("ids")List<String> ids, @Param("tableName")String tableName);
}