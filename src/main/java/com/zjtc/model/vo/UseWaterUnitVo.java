package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zjtc.model.Bank;
import com.zjtc.model.Contacts;
import com.zjtc.model.File;
import com.zjtc.model.UseWaterQuota;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.UseWaterUnitModify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description 用水单位表vo
 */
@Data
@ApiModel("用水单位表vo")
public class UseWaterUnitVo extends Model<UseWaterUnitVo> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "单位编码，必填")
  @TableField(value = "unit_code", exist = false)
  private String unitCode;

  @ApiModelProperty(value = "单位名称，必填")
  @TableField(value = "unit_name", exist = false)
  private String unitName;

  @ApiModelProperty(value = "单位地址")
  @TableField(value = "unit_address", exist = false)
  private String unitAddress;

  @ApiModelProperty(value = "所属行业id")
  @TableField(value = "industry", exist = false)
  private String industry;

  @ApiModelProperty(value = "所属行业名称")
  @TableField(exist = false)
  private String industryName;

  @ApiModelProperty(value = "邮寄名称")
  @TableField(value = "zip_name", exist = false)
  private String zipName;

  @ApiModelProperty(value = "邮寄地址")
  @TableField(value = "zip_address", exist = false)
  private String zipAddress;

  @ApiModelProperty(value = "开票单位")
  @TableField(value = "invoice_unit_name", exist = false)
  private String invoiceUnitName;

  @ApiModelProperty(value = "是否是节水型单位,0:否，1是")
  @TableField(value = "save_unit_type", exist = false)
  private String saveUnitType;

  @ApiModelProperty(value = "是否已删除,0：否，1：是")
  @TableField(value = "deleted", exist = false)
  private String deleted;

  @ApiModelProperty(value = "删除时间")
  @TableField(value = "delete_time", exist = false)
  private Date deleteTime;

  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = false)
  private Date createTime;

  @ApiModelProperty(value = "gisX")
  @TableField(value = "gisX", exist = false)
  private Double gisX;
  @ApiModelProperty(value = "gisY")
  @TableField(value = "gisY", exist = false)
  private Double gisY;

  @ApiModelProperty(value = "是否为主户")
  @TableField(value = "imain", exist = false)
  private String imain;

  @ApiModelProperty(value = "备注")
  @TableField(value = "remark", exist = false)
  private String remark;

  @ApiModelProperty(value = "部门")
  @TableField(value = "department", exist = false)
  private String department;

  @ApiModelProperty(value = "所属区域")
  @TableField(value = "area_country", exist = true)
  private String areaCountry;

  @ApiModelProperty(value = "所属区域名称")
  @TableField(exist = false)
  private String areaCountryName;

  @ApiModelProperty(value = "是否异常")
  @TableField(value = "abnormal", exist = false)
  private String abnormal;

  @ApiModelProperty(value = "异常原因")
  @TableField(value = "abnormal_cause", exist = false)
  private String abnormalCause;

  @ApiModelProperty(value = "责任书编号")
  @TableField(value = "responsibility_code", exist = false)
  private String responsibilityCode;

  @ApiModelProperty(value = "批次,取unit_code 3-4 位")
  @TableField(value = "unit_code_group", exist = true)
  private String unitCodeGroup;

  @ApiModelProperty(value = "类型,取unit_code 5-6 位")
  @TableField(value = "unit_code_type", exist = false)
  private String unitCodeType;

  @ApiModelProperty(value = "水表信息")
  @TableField(exist = false)
  private List<UseWaterUnitMeter> meterList;

  @ApiModelProperty(value = "银行信息")
  @TableField(exist = false)
  private List<Bank> bankList;

  @ApiModelProperty(value = "联系人信息")
  @TableField(exist = false)
  private List<Contacts> contactsList;

  @ApiModelProperty(value = "责任书信息")
  @TableField(exist = false)
  private List<File> sysFile;

  @ApiModelProperty(value = "用水定额信息")
  @TableField(exist = false)
  private List<UseWaterQuota> quotaFile;

  @ApiModelProperty(value = "相关编号信息")
  @TableField(exist = false)
  private List<UseWaterUnitRefVo> useWaterUnitRefList;

  @ApiModelProperty(value = "相关编号")
  @TableField(exist = false)
  private String useWaterUnitIdRef;

  @ApiModelProperty(value = "关联修改数据")
  @TableField(exist = false)
  private RefEditData refEditData;

  @ApiModelProperty(value = "单位名称修改日志")
  @TableField(exist = false)
  private List<UseWaterUnitModify> ModifyList;

  @ApiModelProperty(value = "开户行名称")
  @TableField(value = "bank_of_deposit", exist = false)
  private String bankOfDeposit;

  @ApiModelProperty(value = "银行帐号")
  @TableField(value = "bank_account", exist = false)
  private String bankAccount;

  @ApiModelProperty(value = "是否签约")
  @TableField(value = "signed", exist = false)
  private String signed;

  @ApiModelProperty(value = "联系人")
  @TableField(value = "contacts", exist = false)
  private String  contacts;

  @ApiModelProperty(value = "手机号")
  @TableField(value = "mobile_number", exist = false)
  private String  mobileNumber;

  @ApiModelProperty(value = "座机号")
  @TableField(value = "phone_number", exist = false)
  private String  phoneNumber;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
