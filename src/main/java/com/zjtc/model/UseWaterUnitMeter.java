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
@TableName(value = "t_w_use_water_unit_meter")
@Accessors(chain = true)
@ApiModel(value = "用水单位水表", description = "用水单位水表")
@EqualsAndHashCode(callSuper = true)
public class UseWaterUnitMeter extends Model<UseWaterUnitMeter> {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "单位id")
  @TableField(value = "use_water_unit_id", exist = true)
  private String useWaterUnitId;

  @ApiModelProperty(value = "水表档案号")
  @TableField(value = "water_meter_code", exist = true)
  private String waterMeterCode;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
