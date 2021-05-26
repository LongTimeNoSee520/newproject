package com.zjtc.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: ZhouDaBo
 * @Date: 2021/5/21
 */
@Data
@ApiModel("经办人信息")
public class OperatorVo {


  @ApiModelProperty(value = "姓名",allowableValues = "16",dataType = "String")
  private String userName;

  @ApiModelProperty(value = "办公电话",allowableValues = "16",dataType = "String")
  private String officeTel;

  @ApiModelProperty(value = "房间号")
  private String roomNumber;


}
