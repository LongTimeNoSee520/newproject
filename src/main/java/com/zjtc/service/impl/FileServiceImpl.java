package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.zjtc.base.util.TimeUtil;
import com.zjtc.mapper.FileMapper;
import com.zjtc.model.File;
import com.zjtc.service.FileService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuyantian
 * @date 2020/12/7
 * @description
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

  @Override
  public boolean saveModel(JSONObject jsonObject) {
    File entity = jsonObject.toJavaObject(File.class);
    boolean result = this.insert(entity);
    return result;
  }

  @Override
  public boolean updateModel(JSONObject jsonObject) {
    File entity = jsonObject.toJavaObject(File.class);
    boolean result = this.updateById(entity);
    return result;
  }

  @Override
  public boolean deleteModel(JSONObject jsonObject) {
    File entity = jsonObject.toJavaObject(File.class);
    boolean result = this.deleteById(entity);
    return result;
  }

  @Override
  public Page<File> queryPage(JSONObject jsonObject) {
    File entity = jsonObject.toJavaObject(File.class);
    Page<File> page = new Page<File>(jsonObject.getInteger("current"),
        jsonObject.getInteger("size"));
    page.setSearchCount(true);
    page.setOptimizeCountSql(true);
    EntityWrapper<File> eWrapper = new EntityWrapper<File>(entity);
    Page<File> result = this.selectPage(page, eWrapper);
    return result;
  }

  @Override
  public String uploadFile(MultipartFile file, String path) {
    String result = "";
    try {
      //判断保存文件路径是否存在，如果不存在，则创建
      java.io.File dir = new java.io.File(path);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      String uploadFileName = file.getOriginalFilename();//上传附件名
      String suffix = uploadFileName
          .substring(uploadFileName.lastIndexOf("."), uploadFileName.length());//文件后缀
      //获取附件上传路径
      String fileName = UUID.randomUUID().toString().replace("-", "") + TimeUtil
          .formatTimeyyyyMMdd() + suffix;//文件名重新命名，避免重复
      //文件路径在上需要封装号
      java.io.File f = new java.io.File(path + fileName);
      file.transferTo(f); //保存文件
      result = fileName;
    } catch (IOException e) {
      log.error("附件上传失败,errMsg{" + e.getMessage() + "}");
    } catch (Exception e) {
      log.error("附件上传失败,errMsg{" + e.getMessage() + "}");
    }
    return result;
  }

  /**
   * 更新附件表的业务ID
   */
  @Override
  public boolean updateBusinessId(String businessId, List<File> sysAttrFiles) {
    boolean result = false;
    List<File> updatelist = new ArrayList<>();
    List<String> deleteList = new ArrayList<>();
    if (null != sysAttrFiles) {
      for (File sysAttrFile : sysAttrFiles) {
        if (null != sysAttrFile.getId() && sysAttrFile.getDeleted().equals("0")) {
          sysAttrFile = this.selectById(sysAttrFile.getId());
          sysAttrFile.setBusinessId(businessId);
          updatelist.add(sysAttrFile);
        } else if (null != sysAttrFile.getId() && sysAttrFile.getDeleted().equals("1")) {
          deleteList.add(sysAttrFile.getId());
        }
      }
      if (updatelist.size() > 0) {
        result = this.updateBatchById(updatelist);
      }
      if (deleteList.size() > 0) {
        result = this.deleteBatchIds(deleteList);
      }

    }
    return result;
  }

  @Override
  public boolean removeByBusinessId(String businessId) {
    EntityWrapper entityWrapper=new EntityWrapper();
    entityWrapper.eq("business_id",businessId);
    File file=new File();
    file.setDeleted("1");
    file.setBusinessId("");
    return update(file,entityWrapper);
  }

  @Override
  public boolean removeByBusinessIds(List<String> businessIds) {
    EntityWrapper entityWrapper=new EntityWrapper();
    entityWrapper.eq("business_id",businessIds);
    File file=new File();
    file.setDeleted("1");
    file.setBusinessId("");
    return update(file,entityWrapper);
  }


  @Override
  public boolean removeBusinessId(String fileId) {
    boolean result = false;
    if (StringUtils.isNotEmpty(fileId)) {
      Wrapper<File> wrapper = new EntityWrapper<>();
      if (fileId.indexOf(",") > 0) {
        String[] ids = fileId.split(",");
        List<String> idsList = Arrays.asList(ids);
        result = this.deleteBatchIds(idsList);

      } else {
        result = this.deleteById(fileId);
      }
    }

    return result;
  }
}
