package com.zjtc.base.constant;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/11/30
 * <p>
 * 审核状态常量
 */
public class AuditStatusConfig {
  /**
   *短信创建
   */
  private static final String AWAIT_CREATE = "0";

  /**
   * 待审核
   */
  public  static final String AWAIT_APPROVED = "1";

  /**
   * 审核通过
   */
  public static final String GET_APPROVED = "2";

  /**
   * 审核不通过
   */
  public static final String NOT_APPROVED = "3";
}
