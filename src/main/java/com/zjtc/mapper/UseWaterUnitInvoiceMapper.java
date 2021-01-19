package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.UseWaterUnitInvoice;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * UseWaterUnitInvoice的Dao接口
 *
 * @Author: ZhouDaBo
 * @Date: 2020/12/23 计划用水户发票表
 */
@Mapper
public interface UseWaterUnitInvoiceMapper extends BaseMapper<UseWaterUnitInvoice> {


  /**
   * 新增时查询发票号是否已存在
   *
   * @param invoiceNumber 发票号
   * @param nodeCode      节点编码
   * @return 匹配的条数
   */
  int selectInvoiceNumber(
      @Param("invoiceNumber") String invoiceNumber,
      @Param("nodeCode") String nodeCode);

  /**
   * 根据发票id查看是否被领取
   *
   * @param id 发票id
   * @return 匹配的结果
   */
  int selectReceived(@Param("id") String id);

  /**
   * 根据id查询实体
   *
   * @param id 实体id
   * @return 实体
   */
  UseWaterUnitInvoice selectUseWaterUnitInvoice(
      @Param("id") String id,
      @Param("nodeCode") String nodeCode);

  /**
   * 移交票段,修改领取人id
   *
   * @param begin    开始票段
   * @param end      结束票段
   * @param personId 移交人id
   * @param loginId  登录人id
   * @return 影响的行数
   */
  int updateUid(
      @Param("begin") int begin,
      @Param("end") int end,
      @Param("personId") String personId,
      @Param("loginId") String loginId);

  /**
   * 条件查询总条数
   *
   * @param invoiceNumber 发票号
   * @param begin         开始票段
   * @param end           结束票段
   * @param enabled       是否作废
   * @param received      是否领取
   * @param nodeCode      节点编码
   * @param loginId       登录者id
   * @return 匹配的条数
   */
  Integer selectCount(
      @Param("invoiceNumber") String invoiceNumber,
      @Param("begin") Integer begin,
      @Param("end") Integer end,
      @Param("enabled") String enabled,
      @Param("received") String received,
      @Param("nodeCode") String nodeCode,
      @Param("loginId") String loginId);

  /**
   * 条件查询分页数据
   *
   * @param currPage      页数
   * @param pageSize      条数
   * @param invoiceNumber 发票号
   * @param begin         开始票段
   * @param end           结束票段
   * @param enabled       是否作废
   * @param received      是否领取
   * @param nodeCode      节点编码
   * @param loginId       登录者id
   * @return 实体结果集
   */
  List<UseWaterUnitInvoice> queryList(
      @Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize,
      @Param("invoiceNumber") String invoiceNumber,
      @Param("begin") Integer begin,
      @Param("end") Integer end,
      @Param("enabled") String enabled,
      @Param("received") String received,
      @Param("nodeCode") String nodeCode,
      @Param("loginId") String loginId);

  /**
   * 查询未被使用的发票编号
   * @return 集合
   */
  List<Map<String,Object>> selectInvoices();

  /**
   * 单位信息关联发票
   * @param id 主键id
   * @param payInfoId 单位id
   * @param invoiceUnitName 单位名称
   * @param invoiceUnitCode 单位编号
   * @return 影响行数
   */
  int updateInvoicesUnitMessage(
      @Param("id") String id,
      @Param("payInfoId") String payInfoId,
      @Param("invoiceUnitName") String invoiceUnitName,
      @Param("invoiceUnitCode") String invoiceUnitCode);
}