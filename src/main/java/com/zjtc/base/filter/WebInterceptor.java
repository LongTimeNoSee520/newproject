package com.zjtc.base.filter;

import com.alibaba.fastjson.JSONObject;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.JWTUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yuyantian
 * @date 2020/8/23
 */
public class WebInterceptor implements HandlerInterceptor {

  @Autowired
  private JWTUtil jwtUtil;

  private static final Logger logger = LoggerFactory.getLogger(WebInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    try {
      System.out.println("请求:" + request.getRequestURL());
      String token = request.getHeader("token");
      //如果header中不存在token，则从参数中获取token
      if (StringUtils.isBlank(token)) {
        token = request.getParameter("token");
      }
      boolean success = jwtUtil.validateToken(token);
      if (success) {
        return true;
      } else {
        returnError(request, response);
        return false;
      }
    } catch (Exception e) {
      logger.error("web拦截器异常,errMsg==={}", e.getMessage());
      e.printStackTrace();
      returnError(request, response);
      return false;
    }
  }


  private void returnError(HttpServletRequest request, HttpServletResponse response) {
    PrintWriter writer = null;
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=utf-8");
    try {
      writer = response.getWriter();
      ApiResponse apiResponse = new ApiResponse();
      apiResponse.setCode(10011);
      apiResponse.setMessage("token无效");
      writer.print(JSONObject.toJSONString(apiResponse));

    } catch (IOException e) {
      logger.error("web拦截器异常,errMsg==={}", e.getMessage());
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object
      handler,
      ModelAndView modelAndView) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex)
      throws Exception {
    // TODO Auto-generated method stub

  }

}
