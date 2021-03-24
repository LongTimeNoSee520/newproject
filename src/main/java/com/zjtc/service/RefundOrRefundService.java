package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.RefundOrRefund;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;


/**
 * RefundOrRefund的服务接口
 * 
 * @author 
 *
 */
public interface RefundOrRefundService extends IService<RefundOrRefund> {


	/**
	* 修改
	* @param jsonObject
	* @return
	*/
	boolean updateModel(User user,JSONObject jsonObject);
	/**
	* 查询所有
	* @return
	*/
	List<RefundOrRefund> queryAll(JSONObject jsonObject);

	/**
	 * 分页查询
	 * @param jsonObject
	 * @return
	 */
	Map<String,Object> queryPage(JSONObject jsonObject);

	/**
	 * 审核
	 * @param jsonObject
	 * @param user
	 * @return
	 */
	boolean audit(JSONObject jsonObject, User user) throws Exception;

	/**
	 * 撤销
	 * @param jsonObject
	 * @return
	 */
	boolean	revoke(JSONObject jsonObject,User user);

	/**
	 * 判断当前数据是否有退缴费记录
	 * @param payId   缴费记录id
	 * @param nodeCode 节点编码
	 * @return
	 */
	boolean auditCount(String payId,String nodeCode);

	/**
	 * 查询下一环节可提交审核的角色人员
	 * @param id 当前退减免单id
	 * @param nodeCode 节点便阿门
	 * @param auditBtn 按钮 ：0 不同意，1 同意
	 * @return
	 */
	List<Map<String,Object>> nextAuditRole(String id,String nodeCode,String auditBtn);
}
