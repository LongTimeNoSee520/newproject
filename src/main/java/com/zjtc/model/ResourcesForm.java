package com.zjtc.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * t_resources_form实体类
 * 
 * @author 
 *
 */
@ApiModel("资源表单表")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_resources_form")
public class ResourcesForm extends Model<ResourcesForm>{
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
    @ApiModelProperty("菜单id")
    @TableField(value = "res_id",exist = true)
    private String resId;
    /**
    * 
    */
    @ApiModelProperty("表单字段")
    @TableField(value = "query_field",exist = true)
    private String queryField;
    /**
    * 
    */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",exist = true,fill = FieldFill.INSERT)
    private Date createTime;
    /**
    * 
    */
    @ApiModelProperty("修改时间")
    @TableField(value = "edit_time",exist = true)
    private Date editTime;
    /**
    * 
    */
    @ApiModelProperty("是否删除，0否1是")
    @TableField(value = "deleted",exist = true)
    private String deleted;
	/**
	 * 实例化
	 */
	public ResourcesForm() {
		super();
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
