package com.zjtc.mapper;

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
   * @param beginYear 开始年份
   * @param endYear 结束年份
   * @param nodeCode 单位编码
   * @param loginId 登录人id
   * @return 匹配条数
   */
  Integer selectCount(
      @Param("unitName") String unitName,
      @Param("beginYear")Integer beginYear,
      @Param("endYear")Integer endYear,
      @Param("nodeCode")String nodeCode,
      @Param("loginId")String loginId);

  /**
   * 分页查询
   * @param currPage 条数
   * @param pageSize 页数
   * @param unitName 单位名称
   * @param beginYear 开始年份
   * @param endYear 结束年份
   * @param nodeCode 单位编码
   * @param loginId 登录人id
   * @return 结果集
   */
  List<WaterUnitAssessVO> queryList(
      @Param("currPage")Integer currPage,
      @Param("pageSize")Integer pageSize,
      @Param("unitName")String unitName,
      @Param("beginYear")Integer beginYear,
      @Param("endYear")Integer endYear,
      @Param("nodeCode")String nodeCode,
      @Param("loginId")String loginId);

  /**
   * 按条件查询
   * @param unitName
   * @param beginYear
   * @param endYear
   * @return
   */
  List<WaterUnitAssessVO> selectList(String unitName, Integer beginYear, Integer endYear);
}
