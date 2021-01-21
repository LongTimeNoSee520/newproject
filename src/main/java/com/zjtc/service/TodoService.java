package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Todo;
import com.zjtc.model.User;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface TodoService extends IService<Todo> {


  void add(String businessId, User user, String auditorId, String auditorName, String todoContent,
       String businessJson, String detailConfig,String todoType);

  /**
   * "待办"改为"已办"
   * @param businessId 业务id
   * @param nodeCode 节点编码
   * @param executePersonId 执行人员
   */
  boolean edit(String businessId,String nodeCode,String executePersonId);
  /**
   * 办结单/退减免单撤销时根据业务id删除待办数据
   * @param businessId 业务id
   */
  boolean deleteByBusinessId(String businessId);
}
