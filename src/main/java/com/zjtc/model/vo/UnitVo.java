package com.zjtc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/5/22
 */
@Data
@ApiModel("单位Vo")
public class UnitVo {

  @ApiModelProperty(value = "id")
  private String id;

  @ApiModelProperty(value = "节点编码")
  private String nodeCode;

  @ApiModelProperty(value = "单位名称")
  private String unitName;

  @ApiModelProperty(value = "单位编码")
  private String unitCode;

  @ApiModelProperty(value = "类型")
  private String unitType;

  @ApiModelProperty(value = "经办人")
  List<OperatorVo> operatorVos;
}
