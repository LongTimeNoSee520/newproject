package com.zjtc.mapper.waterBiz;

import com.zjtc.model.vo.WaterUnitAssessVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/1/18
 */
@Mapper
public interface WaterUnitAssessMapper {

  /**
   * 总条数
   * @param unitName 单位名称
   * @param accessYear 开始年份
   * @param nodeCode 单位编码
   * @param loginId 登录人id
   * @return 匹配条数
   */
  Integer selectCount(
      @Param("unitName") String unitName,
      @Param("accessYear") Integer accessYear,
      @Param("nodeCode") String nodeCode,
      @Param("loginId") String loginId,
      @Param("unitCode") String unitCode
  );

  /**
   * 分页查询
   * @param currPage 条数
   * @param pageSize 页数
   * @param unitName 单位名称
   * @param accessYear 条件查询年度
   * @param nodeCode 单位编码
   * @param loginId 登录人id
   * @return 结果集
   */
  List<WaterUnitAssessVO> queryList(
      @Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize,
      @Param("unitName") String unitName,
      @Param("accessYear") Integer accessYear,
      @Param("nodeCode") String nodeCode,
      @Param("loginId") String loginId,
      @Param("unitCode") String unitCode
  );


}
