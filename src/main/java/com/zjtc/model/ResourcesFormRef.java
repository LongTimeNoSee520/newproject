package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * t_resources_form_ref实体类
 * 
 * @author 
 *
 */
@ApiModel("角色资源表单管理表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_resources_form_ref")
public class ResourcesFormRef extends Model<ResourcesFormRef>{
    /**
    * 
    */
    @ApiModelProperty("id")
    @TableField(value = "id",exist = true)
    private String id;
    /**
    * 
    */
    @ApiModelProperty("节点编码")
    @TableField(value = "node_code",exist = true)
    private String nodeCode;
    /**
    * 
    */
    @ApiModelProperty("资源id")
    @TableField(value = "res_id",exist = true)
    private String resId;
    /**
    * 
    */
    @ApiModelProperty("角色id")
    @TableField(value = "role_id",exist = true)
    private String roleId;
    /**
    * 
    */
    @ApiModelProperty("表单id")
    @TableField(value = "form_id",exist = true)
    private String formId;
	/**
	 * 实例化
	 */
	public ResourcesFormRef() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
