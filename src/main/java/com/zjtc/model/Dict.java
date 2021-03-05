package com.zjtc.model;

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
 * @author yuyantian
 * @date 2020/12/7
 * @description 数据字典
 */
@Data
@TableName(value = "t_dict")
@Accessors(chain = true)
@ApiModel(value = "数据字典", description = "数据字典")
@EqualsAndHashCode(callSuper = true)
public class Dict extends Model<Dict> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "字典名称")
  @TableField(value = "dict_name", exist = true)
  private String dictName;

  @ApiModelProperty(value = "字典编码")
  @TableField(value = "dict_code", exist = true)
  private String dictCode;

  @ApiModelProperty(value = "字典排序")
  @TableField(value = "dict_rank", exist = true)
  private int dictRank;

  @ApiModelProperty(value = "字典描述")
  @TableField(value = "dict_desc", exist = true)
  private String dictDesc;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;

  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @Override
  protected Serializable pkVal() {
    return null;
  }
}
