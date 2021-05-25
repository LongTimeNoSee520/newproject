package com.zjtc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/5/25
 */
@Data
@ApiModel("第一水量和第二水量Vo")
public class WaterVo {

  @ApiModelProperty("第一水量")
  private Double firstWater;

  @ApiModelProperty("第二水量")
  private Double secondWater;

}
