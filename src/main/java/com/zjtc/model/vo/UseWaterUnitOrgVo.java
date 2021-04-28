package com.zjtc.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zjtc.model.Bank;
import com.zjtc.model.Contacts;
import com.zjtc.model.File;
import com.zjtc.model.UseWaterQuota;
import com.zjtc.model.UseWaterUnitMeter;
import com.zjtc.model.UseWaterUnitModify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用水单位信息
 * @author zhoudabo
 * @date 2020/12/25
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用水单位VO")
@Data
public class UseWaterUnitOrgVo extends Model<UseWaterUnitOrgVo> {

  @ApiModelProperty("主键")
  private String id;

  @ApiModelProperty(value = "节点编码")
  private String nodeCode;

  @ApiModelProperty(value = "单位编码")
  private String unitCodeType;

  @ApiModelProperty(value = "单位名称，必填")
  private String unitName;

  @ApiModelProperty(value = "单位地址")
  private String unitAddress;

  @ApiModelProperty(value = "备注")
  private String remark;




  @Override
  protected Serializable pkVal() {
    return null;
  }
}
