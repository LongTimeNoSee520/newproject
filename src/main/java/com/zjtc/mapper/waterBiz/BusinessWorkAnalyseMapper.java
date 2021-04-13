package com.zjtc.mapper.waterBiz;

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
   * @param year
   * @return
   */
  Integer businessApply(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);

  /**
   * 业务办理
   * @param nodeCode
   * @param year
   * @return
   */
  Integer businessTransaction(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);

  /**
   * 现场申报
   * @param nodeCode
   * @param year
   * @return
   */
  Integer businessSceneApply(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);

  /**
   * 公共服务平台申报
   * @param nodeCode
   * @param year
   * @return
   */
  Integer businessPublicApply(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);

  /**
   * 微信申报
   * @param nodeCode
   * @param year
   * @return
   */
  Integer businessWXApply(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);


  /**
   * 现场解决
   * @param nodeCode
   * @param year
   * @return
   */
  Integer businessSceneSolve(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);

  /**
   * 公共服务平台解决
   * @param nodeCode
   * @param year
   * @return
   */
  Integer businessPublicSolve(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);

  /**
   * 微信解决
   * @param nodeCode
   * @param year
   * @return
   */
  Integer businessWXSolve(@Param("nodeCode") String nodeCode ,@Param("year")Integer year);



















}
