package com.zjtc.model;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName(value = "t_file")
@Accessors(chain = true)
@ApiModel(value = "附件", description = "附件")
@EqualsAndHashCode(callSuper = true)
public class File extends Model<File> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty(value = "节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
  @ApiModelProperty(value = "创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty(value = "创建人")
  @TableField(value = "creater_id", exist = true)
  private String createrId;

  @ApiModelProperty(value = "文件类型")
  @TableField(value = "file_type", exist = true)
  private String fileType;

  @ApiModelProperty(value = "文件大小")
  @TableField(value = "size", exist = true)
  private int size;

  @ApiModelProperty(value = "存储路径")
  @TableField(value = "file_path", exist = true)
  private String filePath;

  @ApiModelProperty(value = "文件名")
  @TableField(value = "file_name", exist = true)
  private String fileName;

  @ApiModelProperty(value = "业务id")
  @TableField(value = "business_id", exist = true)
  private String businessId;

  @ApiModelProperty(value = "是否删除")
  @TableField(value = "deleted", exist = true)
  private String deleted;
  
  /**
   * 附件加载地址，用于前端回现图片的情况
   */
  @ApiModelProperty("附件加载路径")
  @TableField(exist = false)
  private String url;
  @Override
  protected Serializable pkVal() {
    return null;
  }
}
