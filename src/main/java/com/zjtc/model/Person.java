package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/7
 */
@Data
@TableName(value = "t_user")
@Accessors(chain = true)
@ApiModel(value = "人员信息", description = "人员信息")
@EqualsAndHashCode(callSuper = true)
public class Person extends Model<Person> {

  @ApiModelProperty(value = "主键",allowableValues = "32",dataType = "String",required = false)
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码",allowableValues = "16",dataType = "String")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "部门id",allowableValues = "32",dataType = "String")
  @TableField(value = "org_id", exist = true)
  private String orgId;

  @ApiModelProperty(value = "姓名",allowableValues = "16",dataType = "String")
  @TableField(value = "user_name", exist = true)
  private String userName;

  @ApiModelProperty(value = "移动电话",allowableValues = "32",dataType = "String")
  @TableField(value = "mobile_phone", exist = true)
  private String mobilePhone;

  @ApiModelProperty(value = "排序号",dataType = "int")
  @TableField(value = "user_rank", exist = true)
  private int userRank;

  @ApiModelProperty(value = "账号",allowableValues = "32",dataType = "String")
  @TableField(value = "login_id", exist = true)
  private String loginId;

  @ApiModelProperty(value = "密码",allowableValues = "32",dataType = "String")
  @TableField(value = "password", exist = true)
  private String password;

  @ApiModelProperty(value = "明文密码",allowableValues = "64",dataType = "String")
  @TableField(value = "unpassword", exist = true)
  private String unpassword;

  @ApiModelProperty(value = "办公电话",allowableValues = "16",dataType = "String")
  @TableField(value = "office_tel", exist = true)
  private String officeTel;

  @ApiModelProperty(value = "办公地址",allowableValues = "64",dataType = "String")
  @TableField(value = "office_address", exist = true)
  private String officeAddress;

  @ApiModelProperty(value = "传真",allowableValues = "16",dataType = "String")
  @TableField(value = "fax", exist = true)
  private String fax;

  @ApiModelProperty(value = "邮箱",allowableValues = "32",dataType = "String")
  @TableField(value = "email", exist = true)
  private String email;

  @ApiModelProperty(value = "创建时间",dataType = "Date")
    @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "修改时间",dataType = "Date")
  @TableField(value = "edit_time", exist = true)
  private Date editTime;

  @ApiModelProperty(value = "微信id",allowableValues = "32",dataType = "String")
  @TableField(value = "open_id", exist = true)
  private String openId;

  @ApiModelProperty(value = "是否删除",allowableValues = "1",dataType = "String")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @ApiModelProperty(value = "是否被锁定(0:否,1是)",allowableValues = "1",dataType = "String")
  @TableField(value = "locked",exist = true)
  private String locked;

  @ApiModelProperty(value = "密码输入错误次数",dataType = "int")
  @TableField(value = "wrong_times",exist = true)
  private int wrongTimes;

  @ApiModelProperty(value = "职位")
  @TableField(value = "roleName", exist = false)
  private String roleName;

  @ApiModelProperty(value = "职位id")
  @TableField(value = "roleId", exist = false)
  private String roleId;

  @ApiModelProperty(value = "密码需要修改,1:需要修改")
  @TableField(value = "need_edited", exist = true)
  private String needEdited;



  @Override
  protected Serializable pkVal() {
    return null;
  }
}
