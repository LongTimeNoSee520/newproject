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
 * @author lianghao
 * @date 2020/12/07
 */
@Data
//@TableName(value = "water_sys.dbo.t_system_log")
@TableName(value = "t_system_log")
@Accessors(chain = true)
@ApiModel(value = "系统日志表", description = "系统日志表信息")
@EqualsAndHashCode(callSuper = true)
public class SystemLog extends Model<SystemLog> {

  @ApiModelProperty("主键")
  @TableId(value = "id", type = IdType.UUID)
  private String id;

  @ApiModelProperty("节点编码")
  @TableField(value = "node_code", exist = true)
  private String nodeCode;

  @ApiModelProperty("操作账号")
  @TableField(value = "login_id", exist = true)
  private String loginId;

  @ApiModelProperty("用户名")
  @TableField(value = "user_name", exist = true)
  private String userName;

  @ApiModelProperty("用户id")
  @TableField(value = "user_id", exist = true)
  private String userId;

  @ApiModelProperty("操作模块")
  @TableField(value = "operate_module", exist = true)
  private String operateModule;

  @ApiModelProperty("操作内容")
  @TableField(value = "operate_content", exist = true)
  private String operateContent;

  @ApiModelProperty("创建时间")
  @TableField(value = "create_time", exist = true)
  private Date createTime;

  @ApiModelProperty("ip")
  @TableField(value = "ip", exist = true)
  private String ip;

  @ApiModelProperty("微信端操作时的openId")
  @TableField(value = "open_id", exist = true)
  private String openId;

  @ApiModelProperty("公共服务平台操作用户的手机号")
  @TableField(value = "phone_number", exist = true)
  private String phoneNumber;

  @ApiModelProperty("操作的系统模块(微信端/公共服务服务平台/节水业务端/系统服务端/短信平台)")
  @TableField(value = "sys_module", exist = true)
  private String sysModule;


  @Override
  protected Serializable pkVal() {
    return null;
  }
}
