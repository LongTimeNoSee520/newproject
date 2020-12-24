package com.zjtc.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zjtc.model.Finance;
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
   *
   * @param nodeCode
   * @param unitName
   * @param paymentDateBegin
   * @param paymentDateFinish
   * @param money
   * @param invoiceState
   * @param drawer
   * @return
   */
  Integer selectCountAll(@Param("nodeCode") String nodeCode, @Param("unitName") String unitName,
      @Param("paymentDateBegin") Date paymentDateBegin,
      @Param("paymentDateFinish") Date paymentDateFinish, @Param("money") String money,
      @Param("invoiceState") String invoiceState, @Param("drawer") String drawer);

  /**
   *
   * @param currPage
   * @param pageSize
   * @param nodeCode
   * @param unitName
   * @param paymentDateBegin
   * @param paymentDateFinish
   * @param money
   * @param invoiceState
   * @param drawer
   * @return
   */
  List<Finance> queryList(@Param("currPage") Integer currPage,
      @Param("pageSize") Integer pageSize, @Param("nodeCode") String nodeCode,
      @Param("unitName") String unitName, @Param("paymentDateBegin") Date paymentDateBegin,
      @Param("paymentDateFinish") Date paymentDateFinish, @Param("money") String money,
      @Param("invoiceState") String invoiceState, @Param("drawer") String drawer);
}
