package com.zjtc.base.response;

/**
 * @author yuchen
 * @date 2019/11/16
 */
public interface StatusConstant {


    //验证码已过期
    int SMS_10001 = 10001;

    //验证码有误
    int SMS_10002 = 10002;
    //手机号未注册过
    int SMS_10003 = 10003;
    //个人验证码次数达上限制，已经消费完获取验证码次数
    int SMS_10004 = 10004;

    //当前用户不存在
    int LOGIN_50001 = 50001;

    //用户名或密码错误
    int LOGIN_50002 = 50002;

    //当前用户没有审核
    int LOGIN_50003 = 50003;

    //匿名访问
    int LOGIN_50004 = 50004;

    //用户没有权限
    int LOGIN_50005 = 50005;

    //令牌过期，请重新登陆
    int LOGIN_50006 = 50006;

    //该账户被删除
    int LOGIN_50007 = 50007;

    //该账户未审核通过
    int LOGIN_50008 = 50008;

    //该账户被冻结
    int LOGIN_50009 = 50009;

    //密码和确认密码不一致
    int REGISTER_60001 = 60001;

    //新密码和原密码不能相同
    int REGISTER_60002 = 60002;
    
    //该角色正在使用中，无法删除
    int SYS_70001 = 70001;

    //系统错误，请重试。
    int SYS_70002 = 70002;

    //获取集成平台id失败。
    int SYS_70003 = 70003;

    //调用集成平台短信接口失败。
    int SYS_70004 = 70004;

    //打标人员（标签）已经工作过
    int MARK_80001 = 80001;

     //打标文件被锁住
    int MARKING_LOCK_80002 = 80002;
    //打标完整，最后一张图片
    int MARKING_COMPLETE_80003 = 80003;

    /**
     * 未抓拍到图片
     */
    int INSPECT_NOT_PIC = 81502;
    /**
     * 巡检线路无摄像头。
     */
    int INSPECT_NOT_CAMERA = 81501;


    //SQL 注入。
    int SYS_99999 = 99999;

}
