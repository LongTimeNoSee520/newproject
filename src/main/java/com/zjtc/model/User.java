package com.zjtc.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7
 */
@Data
public class User {

  @ApiModelProperty(value = "主键")
  private String id;

  @ApiModelProperty(value = "节点编码")
  private String nodeCode;

  @ApiModelProperty(value = "姓名")
  private String username;

  @ApiModelProperty(value = "账号")
  private String loginId;

  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "是否被锁定(0:否,1是)")
  private String locked;

  @ApiModelProperty(value = "密码输入错误次数")
  private int wrongTimes;

  @ApiModelProperty(value = "密码是否修改")
  private String needEdited;

  //------

  @ApiModelProperty("单位id")
  private String useWaterUnitId;

  @ApiModelProperty(value = "用水单位编号")
  private String unitCode;

  @ApiModelProperty(value = "联系人")
  private String  contacts;

  @ApiModelProperty(value = "手机号")
  private String  mobileNumber;

  @ApiModelProperty(value = "座机号")
  private String  phoneNumber;

  @ApiModelProperty(value = "联系人级别:是否是主联系人")
  private String  main;

  @ApiModelProperty(value = "微信唯一标识")
  private String  openId;

  @ApiModelProperty(value = "是否删除")
  private String  deleted;

}