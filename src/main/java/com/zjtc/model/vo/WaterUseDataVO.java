package com.zjtc.model.vo;

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
 * @author lianghao
 * @date 2020/12/28
 */
@Data
@ApiModel(value = "水使用量数据", description = "水使用量数据")
@EqualsAndHashCode(callSuper = true)
public class WaterUseDataVO extends Model<WaterUseDataVO> {

  @ApiModelProperty(value = "区段")
  private String sector; //区段

  @ApiModelProperty(value = "查表日期")
  private String date;

  @ApiModelProperty(value = "水表档案号(用户号)")
  private String waterMeterCode;

  @ApiModelProperty(value = "户名")
  private String userName;

  @ApiModelProperty(value = "地址")
  private String address;

  @ApiModelProperty(value = "口径")
  private String caliber;

  @ApiModelProperty(value = "水表起始数(起度)")
  private String waterBegin;

  @ApiModelProperty(value = "水表结束数(止度)")
  private String waterEnd;

  @ApiModelProperty(value = "水量")
  private String waterNumber;//水量   水量=水表结束数-水表起始数

  @ApiModelProperty(value = "用水性质")
  private String waterUseKinds;

  @ApiModelProperty(value = "水价(单价)")
  private String price;//水价


  @Override
  protected Serializable pkVal() {
    return null;
  }
}
