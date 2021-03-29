package com.zjtc.mapper.waterBiz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjtc.model.File;
import com.zjtc.model.vo.FileVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: yuyantian
 * @Date: 2020/12/7
 * @description
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

  /**
   * 根据业务Id集合查询（完成全路径的拼接）
   * @param businessIds
   * @param fileContext 附件地址上下文
   * @return
   */
  List<FileVO> findByBusinessIds(@Param("businessId") List<String> businessIds,@Param("path")String fileContext);
}
