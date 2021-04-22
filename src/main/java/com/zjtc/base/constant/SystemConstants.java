package com.zjtc.base.constant;

/**
 * 系统通用敞亮
 *
 * @author yuchen
 * @date 2020/12/25
 */
public class SystemConstants {

  /**
   * 状态-是
   */
  public static final String STATUS_YES = "1";

  /**
   * 状态-否
   */
  public static final String STATUS_NO = "0";

  /**
   * 计划调整类型-调整
   */
  public static final String PLAN_CHANGE_TYPE_AJUST = "0";
  /**
   * 计划调整类型-增加
   */
  public static final String PLAN_CHANGE_TYPE_ADD = "1";
  /**
   * 市节点编码
   */
  public static final String  MUNICIPAL_NODE_CODE ="510100000000";
  /**
   * 计划超额审核人员id
   */
  public static final String  PLAN_EXCESS_AUDITOR_ID ="123";
  /**
   * 计划超额审核人员名称
   */
  public static final String  PLAN_EXCESS_AUDITOR_NAME ="测试";

  /**
   * 修改打印状态
   */
  public static final String  DAILY_ADJUST_PRINT ="ADJUST";//日常调整页面打印
  public static final String  DAILY_ADJUST_PRINT_TABLE ="t_w_use_water_plan";//日常调整页面打印修改状态的表
  public static final String  PAY_PRINT ="PAY";//缴费管理页面打印
  public static final String  PAY_PRINT_TABLE ="t_w_water_use_pay_info";//缴费管理页面打印修改状态的表
  public static final String  ADJUST_AUDIT_PRINT ="AUDIT";//计划调整审核页面打印
  public static final String  ADJUST_AUDIT_PRINT_TABLE ="t_wx_use_water_plan_add";//计划调整审核页面打印修改状态的表


}
