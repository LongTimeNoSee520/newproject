package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.User;
import com.zjtc.model.WaterSavingEfficiencyManage;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuyantian
 * @date 2021/3/25
 * @description
 */
public interface WaterSavingEfficiencyManageService extends IService<WaterSavingEfficiencyManage> {

  /**
   * 新增或修改
   */
  boolean addOrUpdate(JSONObject jsonObject, User user);

  /**
   * 分页查询
   */
  Map<String, Object> queryPage(JSONObject jsonObject);

  /**
   * 导入
   * @param file
   * @param user
   * @param bussiesId 水平衡测试单位管理表id
   * @return
   * @throws Exception
   */
  ApiResponse importExcel(MultipartFile file, User user,String bussiesId)
      throws Exception;

  /**
   * 模版下载
   */
  void downloadTemplate(HttpServletRequest request, HttpServletResponse response);
}
