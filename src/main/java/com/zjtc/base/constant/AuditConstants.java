package com.zjtc.base.constant;

/**审核流程常量
 * @author yuyantian
 * @date 2021/1/21
 * @description
 */
public class AuditConstants {

/***************************************************************************
 * ****************************表名*****************************************
 * *************************************************************************
 */

  /**
   * 退减免单表名常量
   */
  public static final String PAY_TABLE = "t_w_refund_or_refund";

  /**
   * 办结单表名
   */
  public static final String END_PAPER_TABLE = "t_w_end_paper";

/***************************************************************************
 * ****************************流程流程节点**************************************
 * *************************************************************************
 */
  /**
   * 退减免单表名常量
   */
  public static final String PAY_FLOW_CODE = "payFlow";

  /**
   * 办结单(增加计划)流程code
   */
  public static final String END_PAPER_ADD_FLOW_CODE = "endPaperAddFlow";

  /**
   * 办结单(四季度间调整)流程code
   */
  public static final String END_PAPER_ADJUST_FLOW_CODE = "endPaperAdjustFlow";
/***************************************************************************
 * ****************************待办类型**************************************
 * *************************************************************************
 */

  /**
   * 用水计划超额
   */
  public static final String PLAN_EXCESS_TYPE = "3";
  /**
   * 办结单类型-调整
   */
  public static final String END_PAPER_TODO_TYPE_AJUST = "4";

  /**
   * 办结单类型-增加
   */
  public static final String END_PAPER_TODO_TYPE_ADD = "5";
  /**
  /**
   * 退、减免类型
   */
  public static final String PAY_TODO_TYPE = "6";
  /**
   *
   */
  public static final String END_PAPER_TODO_TYPE = "2";






/***************************************************************************
 * ****************************待办状态**************************************
 * *************************************************************************
 */
  /**
   * 未办
   */
  public static final String BEFORE_TODO_STATUS = "0";
  /**
   * 已办
   */
  public static final String AFTER_TODO_STATUS = "1";

  /***************************************************************************
   * ****************************待办标题**************************************
   * *************************************************************************
   */

  /**
   * 办结单待办标题
   */
  public static final String END_PAPER_TODO_TITLE = "计划调整申请";
  /**
   * 退、减免单标题
   */
  public static final String PAY_TODO_TITLE = "退、减免申请";

  /**
   * 用水计划超额标题
   */
  public static final String PLAN_EXCESS_TITLE = "用水计划超额审批";

  /**
   * 办结单标题：计划调整申请(公共服务平台)
   */
  public static final String PLAN_ADJUST_APPLY_TODO_TITLE = "计划调整申请(公共服务平台)";
  /**
   * 办结单标题：自平计划审核
   */
  public static final String SELF_ADJUST_AUDIT_TODO_TITLE = "自平计划审核(公共服务平台)";
  /***************************************************************************
   * ****************************审核状态**************************************
   * *************************************************************************
   */

  /**
   *短信/办结单/退减免单审核创建
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

  /***************************************************************************
   * ****************************审核后消息通知相关***************************
   * *************************************************************************
   */

  /**
   * 退、减免通知类型
   */
  public static final String PAY_MESSAGE_TYPE = "1";
  /**
   * 办结单通知类型
   */
  public static final String END_PAPER_MESSAGE_TYPE = "2";
  /**
   * 外部通知（用水单位）
   */
  public static final String EXTERNALS_MESSAGE_TYPE = "3";

  /**
   * 退、减免通知标题
   */
  public static final String PAY_MESSAGE_TITLE = "退减免单审核通知";
  /**
   * 办结单通知标题
   */
  public static final String END_PAPER_MESSAGE_TITLE = "办结单审核通知";

}
