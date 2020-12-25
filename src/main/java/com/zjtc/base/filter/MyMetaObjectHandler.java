package com.zjtc.base.filter;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.zjtc.base.constant.SystemConstants;
import com.zjtc.base.util.JWTUtil;
import com.zjtc.model.User;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 自动填充处理
 * @author  yuchen
 */
@Component
public class MyMetaObjectHandler extends MetaObjectHandler {


  @Autowired
  private JWTUtil jwtUtil;

  private MyMetaObjectHandler() {

  }

  // 新增的时候自动填充
  @Override
  public void insertFill(MetaObject metaObject) {
    User user = null;
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes()).getRequest();
      String token = "";
      if (null != request.getSession().getAttribute("token")) {
        token = request.getSession().getAttribute("token").toString();
        user = jwtUtil.getUserByToken(token);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("===========自动填充器获取当前登录用户信息出错");
    }
    if (null == user) {
      user = new User();
      user.setId("1");
      user.setLoginId("admin");
      user.setUsername("超级管理员");
    }
    if (null == this.getFieldValByName("createTime", metaObject)) {
      this.setFieldValByName("createTime", new Date(), metaObject);
    }
//    this.setFieldValByName("createrName", user.getUsername(), metaObject);
//    this.setFieldValByName("creater", user.getId(), metaObject);
    this.setFieldValByName("deleted", SystemConstants.STATUS_NO, metaObject);
  }

  // 更新的时候后自动填充
  @Override
  public void updateFill(MetaObject metaObject) {
    String userId = "admin";
    User user = null;
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes()).getRequest();
      String token = "";
      if (null != request.getSession().getAttribute("token")) {
        token = request.getSession().getAttribute("token").toString();
        user = jwtUtil.getUserByToken(token);
        userId = user.getId();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
//    this.setFieldValByName("modifiedPerson", userId, metaObject);
    this.setFieldValByName("modifyTime", new Date(), metaObject);
  }


}
