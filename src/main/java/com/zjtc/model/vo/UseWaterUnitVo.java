package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.zjtc.model.Bank;
import com.zjtc.model.Contacts;
import com.zjtc.model.File;
import com.zjtc.model.UseWaterQuota;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.UseWaterUnitRef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yuyantian
 * @date 2020/12/24
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用水单位VO")
@Data
public class UseWaterUnitVo extends Model<UseWaterUnitVo> {
  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  private String nodeCode;

  @ApiModelProperty(value = "单位编码，必填")
  private String unitCode;

  @ApiModelProperty(value = "单位名称，必填")
  private String unitName;

  @ApiModelProperty(value = "单位地址")
  private String unitAddress;

  @ApiModelProperty(value = "所属行业id")
  private String industry;

  @ApiModelProperty(value = "邮寄名称")
  private String zipName;

  @ApiModelProperty(value = "邮寄地址")
  private String zipAddress;

  @ApiModelProperty(value = "开票单位")
  private String invoiceUnitName;

  @ApiModelProperty(value = "是否是节水型单位,0:否，1是")
  private String saveUnitType;

  @ApiModelProperty(value = "gisX")
  private Double gisX;

  @ApiModelProperty(value = "gisY")
  private Double gisY;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "部门")
  private String department;

  @ApiModelProperty(value = "所属区域")
  private String areaCountry;

  @ApiModelProperty(value = "是否异常")
  private String abnormal;

  @ApiModelProperty(value = "异常原因")
  private String abnormalCause;

  @ApiModelProperty(value = "是否为主户")
  private String imain;

  @ApiModelProperty(value = "水表信息")
  private List<UseWaterUnitMeter> meterList;

  @ApiModelProperty(value = "银行信息")
  private List<Bank> bankList;

  @ApiModelProperty(value = "联系人信息")
  private List<Contacts> contactsList;

  @ApiModelProperty(value = "责任书信息")
  private List<File> sysFile;

  @ApiModelProperty(value = "用水定额信息")
  @TableField( exist = false)
  private List<UseWaterQuota> quotaFile;

  @ApiModelProperty(value = "相关编号信息")
  private List<UseWaterUnitRefVo> useWaterUnitRefList;

  @ApiModelProperty(value = "相关编号")
  private String  useWaterUnitIdRef;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
