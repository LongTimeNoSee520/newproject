package com.zjtc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.util.FtpUtil;
import com.zjtc.mapper.waterBiz.FileMapper;
import com.zjtc.model.File;
import com.zjtc.model.vo.FileVO;
import com.zjtc.service.FileService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  FtpUtil ftpUtil;

  @Override
  public String uploadFile(MultipartFile file) {
    String result = "";
    try {
      result = ftpUtil.upload(file);
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
          sysAttrFile = this.getById(sysAttrFile.getId());
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
        File file = new File();
        file.setDeleted("1");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", deleteList);
        result = this.update(file, wrapper);
      }

    }
    return result;
  }

  @Override
  public boolean removeByBusinessId(String businessId) {
    QueryWrapper entityQueryWrapper = new QueryWrapper();
    entityQueryWrapper.eq("business_id", businessId);
    File file = new File();
    file.setDeleted("1");
    return update(file, entityQueryWrapper);
  }

  @Override
  public boolean removeByBusinessIds(List<String> businessIds) {
    QueryWrapper entityQueryWrapper = new QueryWrapper();
    entityQueryWrapper.in("business_id", businessIds);
    File file = new File();
    file.setDeleted("1");
    return update(file, entityQueryWrapper);
  }

  @Override
  public List<FileVO> findByBusinessIds(List<String> businessIds, String fileContext) {
    if (null != businessIds && businessIds.size() > 0) {

      return this.baseMapper.findByBusinessIds(businessIds, fileContext);
    }
    return null;
  }

  @Override
  public List<FileVO> findByIds(List<String> ids, String path) {
    if (null != ids && ids.size() > 0) {
      return this.baseMapper.findByIds(ids, path);
    }
    return null;
  }


  @Override
  public boolean removeBusinessId(String fileId) {
    boolean result = false;
    if (StringUtils.isNotEmpty(fileId)) {
      QueryWrapper<File> wrapper = new QueryWrapper<>();
      if (fileId.indexOf(",") > 0) {
        String[] ids = fileId.split(",");
        List<String> idsList = Arrays.asList(ids);
        result = this.removeByIds(idsList);

      } else {
        result = this.removeById(fileId);
      }
    }

    return result;
  }
}
