package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.model.UseWaterUnit;
import com.zjtc.model.User;
import java.util.List;
import java.util.Map;
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
  List<UseWaterUnit> queryAll(JSONObject jsonObject);

  /**
   * 新增用水单位时，查询相关编号
   */
  List<Map<String, Object>> findUnitCode(JSONObject jsonObject);

  /**
   * 验证单位编号是否重复，如果重复，返回一个不重复的编号
   *
   * @param unitCode 单位编号
   * @param nodeCode 节点编码
   */
  String unitCodeValidate(String unitCode, String nodeCode);

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
   * 新增界面：相关编号下拉回填数据
   * @param user
   * @return
   */
  List<Map<String ,Object>> addUnitCodeList(User user);

  /**
   * 修改界面：相关编号下拉回填数据
   * @param user
   * @param id 用水单位id
   * @return
   */
  List<Map<String ,Object>> editUnitCodeList(User user,String id);

  /**
   * 关联修改(数据同步)
   * todo:是当前当前单位编码下，修改其他单位编码数据
   */
  boolean synData(JSONObject jsonObject);
}
