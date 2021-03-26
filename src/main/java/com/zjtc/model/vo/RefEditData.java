package com.zjtc.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yuyantian
 * @date 2020/12/26
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用水单位关联修改信息VO")
@Data
public class RefEditData extends Model<RefEditData> {

  private List<String> refIds;

  /**
   * 用水单位
   */
  private List<String> useWaterUnitColumn;


  /**
   * 银行信息
   */
  private String bankColumn;

  /**
   * 联系人
   */
  private String contactsColumn;

  /**责任书*/
  private String fileColumn;

  /**用水定额*/
  private String quotaFileColumn;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
