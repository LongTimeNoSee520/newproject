package com.zjtc.model;

import com.baomidou.mybatisplus.annotations.TableField;
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

  @ApiModelProperty(value = "是否修改")
  private String isUpdate;

}