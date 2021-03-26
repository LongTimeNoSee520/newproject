package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 相关编号信息
 * @author yuyantian
 * @date 2020/12/25
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用水单位VO")
@Data
public class UseWaterUnitRefVo  extends Model<UseWaterUnitRefVo> {
  @ApiModelProperty("主键")
  private String id;

  @ApiModelProperty("单位编号")
  private String unitCode;

  @ApiModelProperty("单位名称")
  private String unitName;

  @ApiModelProperty("是否主户")
  private String imain;

  @ApiModelProperty("水表档案号")
  private String waterMeterCode;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
