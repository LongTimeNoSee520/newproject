package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

/**
 * @author yuyantian
 * @date 2020/12/23
 * @description
 */
@Service
public interface UseWaterUnitService extends IService<UseWaterUnit> {

  /**
   * 新增
   */
  ApiResponse save(UseWaterUnit useWaterUnit,User user);

  /**
   * 修改
   */
  ApiResponse update(UseWaterUnit useWaterUnit,User user);

  /**
   * 删除
   */
  boolean delete(JSONObject jsonObject,User user);

  /**
   * 分页
   */
  Map<String, Object> queryPage(JSONObject jsonObject);

  /**
   * 根据单位id查询详情
   */
  UseWaterUnit selectById(JSONObject jsonObject,User user);

  /**
   * 新增用水单位时，查询相关编号
   */
  List<Map<String, Object>> findUnitCode(JSONObject jsonObject);


  /**
   * 根据节点编码、批次、类型生成新增用水单位编号需要的排序号
   *
   * @param user 用户对象
   * @param unitCode 单位编号  节点编码+批次+类型
   */
  ApiResponse createunitCode(User user, String unitCode,String id);

  /**
   * 生成新增用水单位编号需要的区域码
   *
   * @param nodeCode 节点编码
   */
  String createAreaCode(String nodeCode);

  /**
   * 新增[修改]界面：相关编号下拉回填数据
   * @param user
   * @return
   */
  List<Map<String ,Object>> addUnitCodeList(User user);


  /**
   * 通过单位编号查询单位信息
   * @param unitCode  单位编号
   * @param user
   * @return
   */
  Map<String,Object> selectByUnitCode(String unitCode,User user);

  /**
   *导出账户审核表
   * @param jsonObject
   * @param request
   * @param response
   */
  void exportAccountAudit(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

  /**
   * 导出开通格式
   * @param jsonObject startTime开始时间,endTime结束时间
   * @param request
   * @param response
   */
  void exportForm(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

  /**
   * 导出撤销格式
   * @param jsonObject
   * @param request
   * @param response
   */
  void exportRevoca(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

  /**
   * 导出查询结果
   * @param jsonObject
   * @param request
   * @param response
   */
  void exportQueryData(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

  /**
   * 导出用水单位增减情况表
   * @param jsonObject
   * @param request
   * @param response
   */
  void exportMoreAndLess(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);

  /**
   *根据单位名称查询单位编码
   */
  List<Map<String,Object>> selectCodeByName(JSONObject jsonObject);
}
