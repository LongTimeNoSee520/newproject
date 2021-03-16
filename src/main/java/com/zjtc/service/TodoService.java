package com.zjtc.service;

import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.Todo;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;

/**
 * @author lianghao
 * @date 2021/01/19
 */
public interface TodoService extends IService<Todo> {

  /**
   *
   * @param businessId
   * @param user 登录人员
   * @param auditorId 审核人id
   * @param auditorName 审核人
   * @param todoContent 待办内容
   * @param businessJson 业务json数据
   * @param detailConfig
   * @param todoType 待办类型
   */
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

  void report(User user, Todo todo);

  void edit(User user,String businessId, String pass) throws Exception;

  List<Map<String,Object>> queryList(User user);
}
