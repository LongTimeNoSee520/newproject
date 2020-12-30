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
 * @Author: ZhouDaBo
 * @Date: 2020/12/23
 */
@Data
@TableName(value = "t_w_water_use_data")
@Accessors(chain = true)
@ApiModel(value = "水使用量数据", description = "水使用量数据")
@EqualsAndHashCode(callSuper = true)
public class WaterUseData extends Model<WaterUseData> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;//单位id

  @ApiModelProperty(value = "单位编码")
  @TableField(value = "unit_code", exist = true)
  private String unitCode;//单位编码

  @ApiModelProperty(value = "单位名称")
  @TableField(value = "unit_names", exist = true)
  private String unitNames;//单位名称

  @ApiModelProperty(value = "单位地址")
  @TableField(value = "unit_address", exist = true)
  private String unitAddress;//单位地址

  @ApiModelProperty(value = "使用年份")
  @TableField(value = "use_year", exist = true)
  private int useYear;//使用年份

  @ApiModelProperty(value = "使用月份")
  @TableField(value = "use_month", exist = true)
  private int useMonth;//使用月份

  @ApiModelProperty(value = "水表档案号")
  @TableField(value = "water_meter_code", exist = true)
  private String waterMeterCode;//水表档案号

  @ApiModelProperty(value = "水表起始数")
  @TableField(value = "water_begin", exist = true)
  private float waterBegin;//水表起始数

  @ApiModelProperty(value = "水表结束数")
  @TableField(value = "water_end", exist = true)
  private float waterEnd;//水表结束数

  @ApiModelProperty(value = "水量")
  @TableField(value = "water_number", exist = true)
  private float waterNumber;//水量   水量=水表结束数-水表起始数

  @ApiModelProperty(value = "水价")
  @TableField(value = "price", exist = true)
  private float price;//水价

  @ApiModelProperty(value = "口径")
  @TableField(value = "caliber", exist = true)
  private int caliber; //口径

  @ApiModelProperty(value = "区段")
  @TableField(value = "sector", exist = true)
  private String sector; //区段

  @ApiModelProperty(value = "用水性质")
  @TableField(value = "water_use_kinds", exist = true)
  private String waterUseKinds; //用水性质

  @ApiModelProperty(value = "人员id")
  @TableField(value = "uid", exist = true)
  private String uid;//人员id

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;//节点编码

  @ApiModelProperty(value = "类型")
  @TableField(value = "type", exist = true)
  private String type;//类型

  @ApiModelProperty(value = "备注")
  @TableField(value = "remarks", exist = true)
  private String remarks;//类型

  private Integer num;//mybatis mapping中用到的keyproperty 需要有setter方法

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
