package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
  private String januaryCount;

  @ApiModelProperty(value = "二月水量")
  @TableField(value = "february_count", exist = true)
  private String februaryCount;

  @ApiModelProperty(value = "三月水量")
  @TableField(value = "march_count", exist = true)
  private String marchCount;

  @ApiModelProperty(value = "四月水量")
  @TableField(value = "april_count", exist = true)
  private String aprilCount;

  @ApiModelProperty(value = "五月水量")
  @TableField(value = "may_count", exist = true)
  private String mayCount;

  @ApiModelProperty(value = "六月水量")
  @TableField(value = "june_count", exist = true)
  private String juneCount;

  @ApiModelProperty(value = "七月水量")
  @TableField(value = "july_count", exist = true)
  private String julyCount;

  @ApiModelProperty(value = "八月水量")
  @TableField(value = "august_count", exist = true)
  private String augustCount;

  @ApiModelProperty(value = "九月水量")
  @TableField(value = "september_count", exist = true)
  private String septemberCount;

  @ApiModelProperty(value = "十月水量")
  @TableField(value = "october_count", exist = true)
  private String octoberCount;

  @ApiModelProperty(value = "十一月水量")
  @TableField(value = "november_count", exist = true)
  private String novemberCount;

  @ApiModelProperty(value = "十二月水量")
  @TableField(value = "december_count", exist = true)
  private String decemberCount;

  @ApiModelProperty(value = "当前水价")
  @TableField(value = "now_price", exist = true)
  private String nowPrice;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
