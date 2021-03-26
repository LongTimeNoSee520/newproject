package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yuyantian
 * @date 2021/3/8
 * @description
 */
@Data
@ApiModel(value = "短信状态vo", description = "短信状态vo")
public class SmsSendStatusVo extends Model<SmsSendStatusVo> {

  @ApiModelProperty("区域code")
  private String unitCode;

  @ApiModelProperty("接收人电话号")
  private String phoneNumber;

  @ApiModelProperty("短信状态")
  private String status;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
