package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lianghao
 * @date 2020/12/23
 */
@Data
@TableName(value = "t_w_use_water_unit_contacts")
@Accessors(chain = true)
@ApiModel(value = "用水单位联系人信息", description = "用水单位联系人信息")
@EqualsAndHashCode(callSuper = true)
public class Contacts extends Model<Contacts> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty("单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "用水单位编号")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;

  @ApiModelProperty(value = "联系人")
  @TableField(value = "contacts", exist = true)
  private String  contacts;

  @ApiModelProperty(value = "手机号")
  @TableField(value = "mobile_number", exist = true)
  private String  mobileNumber;

  @ApiModelProperty(value = "座机号")
  @TableField(value = "phone_number", exist = true)
  private String  phoneNumber;

  @ApiModelProperty(value = "联系人级别:是否是主联系人")
  @TableField(value = "main", exist = true)
  private String  main;

  @ApiModelProperty(value = "微信唯一标识")
  @TableField(value = "open_id", exist = true)
  private String  openId;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String  deleted;

  @ApiModelProperty(value = "是否被锁定(0:否,1是)")
  @TableField(value = "locked", exist = true)
  private String locked;

  @ApiModelProperty(value = "密码输入错误次数")
  @TableField(value = "wrong_times", exist = true)
  private int wrongTimes;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}