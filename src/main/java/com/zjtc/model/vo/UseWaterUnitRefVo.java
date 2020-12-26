package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.jws.WebParam.Mode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

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

  @ApiModelProperty("水表档案号")
  private String waterMeterCode;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
