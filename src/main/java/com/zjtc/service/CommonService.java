package com.zjtc.service;


import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公共接口
 * @author lianghao
 * @date 2021/01/29
 */
public interface CommonService  {

  /**
   *
   * @param fileName 导出后的文件名称
   * @param template 模板路径
   * @param request
   * @param response
   * @param data 数据
   */
  boolean export(String fileName, String template, HttpServletRequest request,
      HttpServletResponse response, Map<String, Object> data);
}
