package com.zjtc.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.constant.AuditConstants;
import com.zjtc.mapper.TodoMapper;
import com.zjtc.model.Todo;
import com.zjtc.model.User;
import com.zjtc.service.TodoService;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * @author lianghao
 * @date 2021/01/19
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {


  @Override
  public void add(String businessId, User user, String auditorId, String auditorName,
      String todoContent, String businessJson, String detailConfig, String todoType) {

    Todo todo = new Todo();
    todo.setNodeCode(user.getNodeCode());
    todo.setExecutePersonId(auditorId);
    todo.setExecutePersonName(auditorName);
    todo.setTodoContent(todoContent);
    todo.setTodoType(todoType);
    if (AuditConstants.PAY_TODO_TYPE.equals(todoType)){
      todo.setTodoTitle(AuditConstants.PAY_TODO_TITLE);
      todo.setTableName(AuditConstants.PAY_TABLE);
    }else if(AuditConstants.END_PAPER_TODO_TYPE.equals(todoType)){
      todo.setTodoTitle(AuditConstants.END_PAPER_TODO_TITLE);
      todo.setTableName(AuditConstants.END_PAPER_TABLE);
    }
    todo.setBusinessJson(businessJson);
    todo.setBusinessId(businessId);
    todo.setRegistrant(user.getUsername());
    todo.setRegistrantId(user.getId());
    todo.setDetailConfig(detailConfig);
    todo.setStatus(AuditConstants.BEFORE_TODO_STATUS);
    todo.setCreateTime(new Date());
    this.baseMapper.insert(todo);
  }

  @Override
  public boolean edit(String businessId, String nodeCode,String executePersonId) {
    EntityWrapper wrapper=new EntityWrapper();
    wrapper.eq("business_id",businessId);
    wrapper.eq("node_code",nodeCode);
    wrapper.eq("execute_personId",executePersonId);
    Todo todo= this.selectOne(wrapper);
    todo.setStatus("1");
    todo.setOperationTime(new Date());
    return this.updateById(todo);
  }

  @Override
  public boolean deleteByBusinessId(String businessId) {
    return this.baseMapper.deleteByBusinessId(businessId);
  }
}