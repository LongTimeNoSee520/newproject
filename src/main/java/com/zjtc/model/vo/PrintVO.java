package com.zjtc.model.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("打印")
@Data
@EqualsAndHashCode(callSuper = true)
public class PrintVO extends Model<PrintVO> {

  @ApiModelProperty("主键")
  private String id;

  @ApiModelProperty("打印的类型0为主数据，1为展开列表的调整数据")
  private String printType;

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
