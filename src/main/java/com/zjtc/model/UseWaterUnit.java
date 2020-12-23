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
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description 用水单位表
 */
@Data
@TableName(value = "t_w_use_water_unit")
@Accessors(chain = true)
@ApiModel(value = "用水单位表", description = "用水单位表")
@EqualsAndHashCode(callSuper = true)
public class UseWaterUnit extends Model<UseWaterUnit> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "单位编码，必填")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;

  @ApiModelProperty(value = "单位名称，必填")
  @TableField(value = "unit_name", exist = true)
  private String unitName;

  @ApiModelProperty(value = "单位地址")
  @TableField(value = "unit_address", exist = true)
  private String unitAddress;

  @ApiModelProperty(value = "所属行业")
  @TableField(value = "industry", exist = true)
  private String industry;

  @ApiModelProperty(value = "邮编")
  @TableField(value = "zip_code", exist = true)
  private String zipCode;

  @ApiModelProperty(value = "是否是节水型单位")
  @TableField(value = "save_unit_type", exist = true)
  private String saveUnitType;

  @ApiModelProperty(value = "是否已删除,0：否，1：是")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @ApiModelProperty(value = "删除时间")
  @TableField(value = "delete_time", exist = true)
  private Date deleteTime;

  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "gisX")
  @TableField(value = "gisX", exist = true)
  private Double gisX;
  @ApiModelProperty(value = "gisY")
  @TableField(value = "gisY", exist = true)
  private Double gisY;

  @ApiModelProperty(value = "是否为主户")
  @TableField(value = "imain", exist = true)
  private String imain;

  @ApiModelProperty(value = "备注")
  @TableField(value = "remark", exist = true)
  private String remark;

  @ApiModelProperty(value = "部门")
  @TableField(value = "department", exist = true)
  private String department;

  @ApiModelProperty(value = "所属区域")
  @TableField(value = "area_country", exist = true)
  private String areaCountry;

  @ApiModelProperty(value = "是否异常")
  @TableField(value = "abnormal", exist = true)
  private String abnormal;

  @ApiModelProperty(value = "异常原因")
  @TableField(value = "abnormal_cause", exist = true)
  private String abnormalCause;

  @ApiModelProperty(value = "责任书编号")
  @TableField(value = "responsibility_code", exist = true)
  private String responsibilityCode;

  @ApiModelProperty(value = "批次,取unit_code 3-4 位")
  @TableField(value = "unit_code_group", exist = true)
  private String unitCodeGroup;

  @ApiModelProperty(value = "类型,取unit_code 5-6 位")
  @TableField(value = "unit_code_type", exist = true)
  private String unitCodeType;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
