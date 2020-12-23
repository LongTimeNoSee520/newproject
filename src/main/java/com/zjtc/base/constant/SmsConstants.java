package com.zjtc.base.constant;

public class SmsConstants {

  /**
   * 短信发送状态-发送成功
   */
  public  static final String SNED_STATUS_SUCCESS = "0";

  /**
   * 短信发送状态-发送失败
   */
  public  static final String SEND_STATUS_FAIL = "1";

  /**
   * 短信发送状态-等待回执
   */
  public  static final String SEND_STATUS_WAITING = "2";


  /**
   * 短信审核状态-短信创建
   */
  public  static final String AUDIT_STATUS_CREATE = "0";

  /**
   * 短信审核状态-待审核
   */
  public  static final String AUDIT_STATUS_WAITING = "1";

  /**
   * 短信审核状态-审核通过
   */
  public  static final String AUDIT_STATUS_PASS = "2";

  /**
   * 短信审核状态-审核不通过
   */
  public  static final String AUDIT_STATUS_FAIL = "3";

}
