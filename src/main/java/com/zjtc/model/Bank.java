package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Data
@TableName(value = "t_w_bank")
@Accessors(chain = true)
@ApiModel(value = "银行信息表", description = "银行信息表")
@EqualsAndHashCode(callSuper = true)
public class Bank extends Model<Bank> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "托收单位名称")
  @TableField(value = "entrust_unit_name", exist = true)
  private String entrustUnitName;

  @ApiModelProperty(value = "开户行名称")
  @TableField(value = "bank_of_deposit", exist = true)
  private String bankOfDeposit;

  @ApiModelProperty(value = "银行帐号")
  @TableField(value = "bank_account", exist = true)
  private String bankAccount;

  @ApiModelProperty(value = "开户行号")
  @TableField(value = "people_bank_pay_sys_number", exist = true)
  private String peopleBankPaySysNumber;

  @ApiModelProperty(value = "协议号")
  @TableField(value = "agreement_number", exist = true)
  private String agreementNumber;

  @ApiModelProperty(value = "是否他行")
  @TableField(value = "other_bank", exist = true)
  private char otherBank;

  @ApiModelProperty(value = "签约是否成功")
  @TableField(value = "signed", exist = true)
  private char signed;

  @ApiModelProperty(value = "是否主账号")
  @TableField(value = "main", exist = true)
  private String main;

  @ApiModelProperty(value = "撤销时间")
  @TableField(value = "revocation_date", exist = true)
  private Date revocationDate;

  @ApiModelProperty(value = "集中户备注")
  @TableField(value = "focus_user_remark", exist = true)
  private String focusUserRemark;

  @ApiModelProperty(value = "用水单位id")
  @TableField(value = "use_water_unit_Id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
