package com.zjtc.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zjtc.model.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
public interface FileService extends IService<File> {

  /**
   * 保存
   */
  boolean saveModel(JSONObject jsonObject);

  /**
   * 修改
   */
  boolean updateModel(JSONObject jsonObject);

  /**
   * 删除
   */
  boolean deleteModel(JSONObject jsonObject);

  /**
   * 分页查询
   */
  Page<File> queryPage(JSONObject jsonObject);

  /**
   * 附件上传
   *
   * @param path 附件保存目录
   */
  String uploadFile(MultipartFile file, String path);

  /**
   * 删除附件
   */
  boolean removeBusinessId(String fileId);

  /**
   * 更新附件业务ID
   */
  boolean updateBusinessId(String businessId, List<File> sysAttrFiles);
  /**
   * 根据单个业务id批量删除附件
   */
  boolean removeByBusinessId(String businessId);
  /**
   * 根据多个业务id批量删除附件
   */
  boolean removeByBusinessIds(List<String> businessId);
}
