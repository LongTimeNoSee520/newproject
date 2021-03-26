package com.zjtc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * t_diy_menu实体类
 * 
 * @author 
 *
 */
@ApiModel("自定义菜单表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_diy_menu")
public class DiyMenu extends Model<DiyMenu>{
    /**
    * 
    */
    @ApiModelProperty("自定义菜单id")
    @TableId(value = "id ",type = IdType.UUID)
    private String id ;
    /**
    * 
    */
    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;
    /**
    * 
    */
    @ApiModelProperty("登录人id")
    @TableField(value = "login_id",exist = true)
    private String loginId;
    /**
    * 
    */
    @ApiModelProperty("菜单id")
    @TableField(value = "menu_id",exist = true)
    private String menuId;
	/**
	 * 实例化
	 */
	public DiyMenu() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
