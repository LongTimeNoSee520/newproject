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
 * @author yuyantian
 * @date 2020/12/23
 * @description 水使用量月数据
 */

@Data
@TableName(value = "t_w_water_month_use_data")
@Accessors(chain = true)
@ApiModel(value = "水使用量月数据", description = "水使用量月数据")
@EqualsAndHashCode(callSuper = true)
public class WaterMonthUseData extends Model<WaterMonthUseData> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty(value = "单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "使用年份")
  @TableField(value = "use_year", exist = true)
  private String useYear;

  @ApiModelProperty(value = "水表档案号")
  @TableField(value = "water_meter_code", exist = true)
  private String waterMeterCode;

  @ApiModelProperty(value = "一月水量")
  @TableField(value = "january_count", exist = true)
  private double januaryCount;

  @ApiModelProperty(value = "二月水量")
  @TableField(value = "february_count", exist = true)
  private double februaryCount;

  @ApiModelProperty(value = "三月水量")
  @TableField(value = "march_count", exist = true)
  private double marchCount;

  @ApiModelProperty(value = "四月水量")
  @TableField(value = "april_count", exist = true)
  private double aprilCount;

  @ApiModelProperty(value = "五月水量")
  @TableField(value = "may_count", exist = true)
  private double mayCount;

  @ApiModelProperty(value = "六月水量")
  @TableField(value = "june_count", exist = true)
  private double juneCount;

  @ApiModelProperty(value = "七月水量")
  @TableField(value = "july_count", exist = true)
  private double julyCount;

  @ApiModelProperty(value = "八月水量")
  @TableField(value = "august_count", exist = true)
  private double augustCount;

  @ApiModelProperty(value = "九月水量")
  @TableField(value = "september_count", exist = true)
  private double septemberCount;

  @ApiModelProperty(value = "十月水量")
  @TableField(value = "october_count", exist = true)
  private double octoberCount;

  @ApiModelProperty(value = "十一月水量")
  @TableField(value = "november_count", exist = true)
  private double novemberCount;

  @ApiModelProperty(value = "十二月水量")
  @TableField(value = "december_count", exist = true)
  private double decemberCount;

  @ApiModelProperty(value = "当前水价")
  @TableField(value = "now_price", exist = true)
  private double nowPrice;

  @ApiModelProperty(value = "是否警告")
  @TableField(value = "is_warning", exist = true)
  private int isWarning;

  @ApiModelProperty(value = "口径")
  @TableField(value = "caliber", exist = true)
  private int caliber; //口径

  @ApiModelProperty(value = "区段")
  @TableField(value = "sector", exist = true)
  private String sector; //区段

  @ApiModelProperty(value = "用水性质")
  @TableField(value = "water_use_kinds", exist = true)
  private String waterUseKinds; //用水性质

  @ApiModelProperty(value = "水表公司用户单位名称")
  @TableField(value = "unit_names", exist = true)
  private String unitNames;//水表公司用户单位名称

  @ApiModelProperty(value = "水表公司用户单位地址")
  @TableField(value = "unit_address", exist = true)
  private String unitAddress;//水表公司用户单位地址

  @ApiModelProperty(value = "水量")
  @TableField(exist = false)
  private String waterNumber;

  @ApiModelProperty(value = "单位编号")
  @TableField( exist = false)
  private String unitCode;


  @Override
  protected Serializable pkVal() {
    return null;
  }
}
