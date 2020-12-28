package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Finance;
import com.zjtc.model.UseWaterUnit;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/24 加价费开票记录表
 */
@Mapper
public interface FinanceMapper extends BaseMapper<Finance> {

  /**
   * 根据id删除部门
   *
   * @param id 部门id
   * @return 影响行数
   */
  int updateFinanceDeleted(@Param("id") String id);

  /**
   * 根据条件查询总条数
   *
   * @param nodeCode          节点编码
   * @param unitName          单位名称
   * @param paymentDateBegin  开始时间
   * @param paymentDateFinish 结束时间
   * @param money             金额
   * @param invoiceState      是否开票
   * @param drawer            开票人
   * @return 总条数
   */
  Integer selectCountAll(@Param("nodeCode") String nodeCode, @Param("unitName") String unitName,
      @Param("paymentDateBegin") Date paymentDateBegin,
      @Param("paymentDateFinish") Date paymentDateFinish, @Param("money") String money,
      @Param("invoiceState") String invoiceState, @Param("drawer") String drawer);

  /**
   * 分页查询
   *
   * @param currPage          页数
   * @param pageSize          条数
   * @param nodeCode          节点编码
   * @param unitName          单位名称
   * @param paymentDateBegin  开始时间
   * @param paymentDateFinish 结束时间
   * @param money             金额
   * @param invoiceState      是否开票
   * @param drawer            开票人
   * @return 分页查询记录
   */
  List<Finance> queryList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize,
      @Param("nodeCode") String nodeCode, @Param("unitName") String unitName,
      @Param("paymentDateBegin") Date paymentDateBegin,
      @Param("paymentDateFinish") Date paymentDateFinish, @Param("money") String money,
      @Param("invoiceState") String invoiceState, @Param("drawer") String drawer);


  /**
   * 查询未开票金额和已开票金额
   * @param nodeCode          节点编码
   * @param unitName          单位名称
   * @param paymentDateBegin  开始时间
   * @param paymentDateFinish 结束时间
   * @param drawer            开票人
   * @return 查询结果
   */
  List<String> countMoney(@Param("unitName") String unitName,
      @Param("paymentDateBegin") Date paymentDateBegin,
      @Param("paymentDateFinish") Date paymentDateFinish,
      @Param("drawer") String drawer,
      @Param("nodeCode") String nodeCode);
}
