package com.zjtc.mapper.waterCountry;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/4/10
 */
@Mapper
public interface BusinessWorkAnalyseMapper {

  /**
   * 业务申请
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessApply(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);

  /**
   * 业务办理
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessTransaction(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);

  /**
   * 现场申报
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessSceneApply(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);

  /**
   * 公共服务平台申报
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessPublicApply(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);

  /**
   * 微信申报
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessWXApply(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);


  /**
   * 现场解决
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessSceneSolve(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);

  /**
   * 公共服务平台解决
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessPublicSolve(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);

  /**
   * 微信解决
   * @param nodeCode
   * @param planYear
   * @return
   */
  Integer businessWXSolve(@Param("nodeCode") String nodeCode ,@Param("planYear")Integer planYear);



















}
